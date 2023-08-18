package com.shashwat.blog_app_youtube.Dtos_Payloads;

import lombok.Data;

@Data
public class JwtAuthRequest {
    private String username;
    private String password;

}
