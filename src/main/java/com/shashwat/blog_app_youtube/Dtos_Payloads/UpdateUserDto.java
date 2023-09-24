package com.shashwat.blog_app_youtube.Dtos_Payloads;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class UpdateUserDto {

    private Long userId;

    @Size(min = 4, message = "Username Must be of minimum of 4 characters ..!!")
    private String name;

    @Email(message = "Your Email Address is Not Valid ..!!")
    private String email;

    @Size(min = 6, max = 12 ,message = "Password range minimum 6 characters and maximum of 12 characters ..!! ")
    private String password;

    private String about;

    //@JsonView(View.Admin.class)
    private Set<RoleDto> roles=new HashSet<>();

}