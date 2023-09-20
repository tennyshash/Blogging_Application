package com.shashwat.blog_app_youtube.Dtos_Payloads;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtAuthRequest {
    private String username;
    private String password;

}
