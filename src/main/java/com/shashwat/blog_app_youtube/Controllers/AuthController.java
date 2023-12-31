package com.shashwat.blog_app_youtube.Controllers;

import com.shashwat.blog_app_youtube.Dtos_Payloads.JwtAuthRequest;
import com.shashwat.blog_app_youtube.Dtos_Payloads.JwtAuthResponse;
import com.shashwat.blog_app_youtube.Dtos_Payloads.UserDto;
import com.shashwat.blog_app_youtube.Exception.ApiException;
import com.shashwat.blog_app_youtube.Models.User;
import com.shashwat.blog_app_youtube.Security.JwtTokenHelper;
import com.shashwat.blog_app_youtube.Services.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;


@RestController
@RequestMapping("/home/auth/")

public class AuthController {

    private JwtTokenHelper jwtTokenHelper;

    private UserDetailsService userDetailsService;

    private AuthenticationManager authenticationManager; // to authenticate username/ password
    private UserService userService;
    private ModelMapper mapper;

    @Autowired
    public AuthController(JwtTokenHelper jwtTokenHelper, UserDetailsService userDetailsService,
                          AuthenticationManager authenticationManager, UserService userService,
                          ModelMapper mapper) {
        this.jwtTokenHelper=jwtTokenHelper;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.userService=userService;
        this.mapper=mapper;
    }

    // TO GET CURRENT USER
//    @GetMapping("/current-user")
//    public String getLoggedInUser(Principal principal){
//        return principal.getName();
//    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(
            @RequestBody JwtAuthRequest request
            )  {

        this.authenticate(request.getUsername(), request.getPassword());

        UserDetails userDetails=userDetailsService.loadUserByUsername(request.getUsername());
        //System.out.println("------------------------>USER IS AUTHENTICATED ");

        String token=jwtTokenHelper.generateToken(userDetails);

        JwtAuthResponse response= new JwtAuthResponse();
        response.setToken(token);
        response.setCreatedAt(new Date());
        response.setUser(mapper.map( (User)userDetails,UserDto.class ));

        return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);

    }
    private void authenticate(String email, String password)  {

        UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(email,password);
        try {
            authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException e){
            throw  new ApiException("Invalid Credentials");
        }
    }

    //register new user api
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto){

        UserDto registerUser=userService.registerNewUSer(userDto);
        return new ResponseEntity<UserDto>(registerUser,HttpStatus.CREATED);

    }
}
