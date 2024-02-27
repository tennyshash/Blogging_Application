package com.shashwat.blog_app_youtube.Controllers;

import com.shashwat.blog_app_youtube.Dtos_Payloads.*;
import com.shashwat.blog_app_youtube.Exception.ApiException;
import com.shashwat.blog_app_youtube.Models.User;
import com.shashwat.blog_app_youtube.Security.JwtTokenHelper;
import com.shashwat.blog_app_youtube.Services.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
        authenticationManager.authenticate(authenticationToken);
    }

    //register new user api
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto)  {

        UserDto registerUser=userService.registerNewUSer(userDto);
        return new ResponseEntity<UserDto>(registerUser,HttpStatus.CREATED);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/register/createUsers")
    public ResponseEntity<ApiResponseDto> createUserInBulk(String urll) throws IOException {
        String url="C:\\Users\\Lenovo\\Onerive\\Desktop\\ReadExcel.xlsx";
        ApiResponseDto response= null;
        //try {
            response = userService.registerUsersInBulk(url);
//        } catch (IOException e) {
//            throw new ApiException("Error in Connecting with EXCEL");
//        }  either use try catch & handle exception or use throws in signature and catch globally.
        return new ResponseEntity<ApiResponseDto>(response,HttpStatus.OK);
    }
}
