package com.shashwat.blog_app_youtube.Dtos_Payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ApiResponseDto {
    private String status;
    private String message;

}
