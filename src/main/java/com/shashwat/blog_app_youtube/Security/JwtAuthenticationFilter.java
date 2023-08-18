package com.shashwat.blog_app_youtube.Security;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.shashwat.blog_app_youtube.Exception.ApiException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    public JwtAuthenticationFilter(UserDetailsService userDetailsService, JwtTokenHelper jwtTokenHelper) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. get token
        String requestHeader = request.getHeader("Authorization"); // Authorization is key token is mapped to this
            //Bearer 2354sd  -> how token looks

            String username=null;
            String token=null;

            if(requestHeader!=null && requestHeader.startsWith("Bearer")){

                token=requestHeader.substring(7);

                try{
                    username=jwtTokenHelper.getUsernameFromToken(token);

                }catch (IllegalArgumentException e){

                    System.out.println("Unable to get Jwt token");
                    throw new ApiException("Unable to get Jwt token");

                }catch (ExpiredJwtException e){

                    System.out.println("Token is Expired");
                    throw new ApiException("Token is Expired");

                }catch (MalformedJwtException e) {

                    System.out.println("Invalid JwT Token");
                    throw new ApiException("Invalid JwT Token");
                }catch (Exception e){
                    System.out.println("Invalid JwT Token");
                    throw new ApiException("Something wrong with token");
                }

            }else{
                System.out.println("Jwt token does not begin with bearer");
               //throw new ApiException("Jwt token does not begin with bearer");
            }

        //  We have the token now Validate

            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

                UserDetails userDetails=userDetailsService.loadUserByUsername(username);
                Boolean validateToken=jwtTokenHelper.validateToken(token,userDetails);

                if(validateToken){
                    //All good Now we have to set authentication

                    UsernamePasswordAuthenticationToken authenticationToken=
                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                    /*
                        we can't directly set details in constructor, we have to use  builder method to build details and
                        builder method is part of class WebAuthenticationDetailsSource().
                     */
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }else {
                    System.out.println("Invalid JwT Token");
                   //throw new ApiException("Invalid JwT Token");
                }
            }else{
                System.out.println("UserName is null or context is not null ");
                //throw new ApiException("UserName is null or context is not null ");
            }

            filterChain.doFilter(request,response);

    }
}
