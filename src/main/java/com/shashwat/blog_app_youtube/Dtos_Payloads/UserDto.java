package com.shashwat.blog_app_youtube.Dtos_Payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
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

    //@JsonView(View.Base.class)
    private Long userId;

    //@JsonView(View.Base.class)
    @NotEmpty(message = "Field is Blank .! Try Again")
    @Size(min = 4, message = "Username Must be of minimum of 4 characters ..!!")
    private String name;

    //@JsonView(View.Base.class)
    @Email(message = "Your Email Address is Not Valid ..!!")
    @NotEmpty(message = "Enter proper email address ")
    private String email;

    @NotEmpty
    @Size(min = 3, max = 10 ,message = "Password must be in of minimum 3 characters and maximum of 10 characters ..!! ")
    //@Pattern(regexp = )  used to match password input patter like 2 caps one special cahr etc..
    //TODO:
    private String password;

    //@JsonView(View.Base.class)
    @NotEmpty(message = "Tell Me About Yourself .!")
    private String about;

    //@JsonView(View.Admin.class)
    private Set<RoleDto> roles=new HashSet<>();

    @JsonIgnore
    // To ignore password while displaying but due to some reason it was also ignoring set passwrod so we were not able to set..
    public String getPassword() {
        return password;
    }

    @JsonProperty // hence used this to let set password
    public void setPassword(String password) {
        this.password = password;
    }
}
