package com.shashwat.blog_app_youtube.Dtos_Payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ImageResponseDto {

    private String Status;
    private String message;
    private String imageURL;

}
