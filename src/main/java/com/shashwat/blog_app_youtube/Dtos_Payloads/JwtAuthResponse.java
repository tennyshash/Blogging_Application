package com.shashwat.blog_app_youtube.Dtos_Payloads;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
public class JwtAuthResponse {

    private String token;

    @CreationTimestamp
    private Date createdAt;
}
