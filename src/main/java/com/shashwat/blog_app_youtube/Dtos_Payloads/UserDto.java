package com.shashwat.blog_app_youtube.Dtos_Payloads;

import com.shashwat.blog_app_youtube.Models.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class UserDto {

    private Long userId;

    @NotEmpty
    @Size(min = 4, message = "Username Must be of minimum of 4 characters ..!!")
    private String name;

    @Email(message = "Your Email Address is Not Valid ..!!")
    private String email;

    @NotEmpty
    @Size(min = 3, max = 10 ,message = "Password must be in of minimum 3 characters and maximum of 10 characters ..!! ")
    //@Pattern(regexp = )  used to match password input patter like 2 caps one special cahr etc..
    //TODO:
    private String password;

    @NotEmpty
    private String about;

    private Set<RoleDto> roles=new HashSet<>();

}
