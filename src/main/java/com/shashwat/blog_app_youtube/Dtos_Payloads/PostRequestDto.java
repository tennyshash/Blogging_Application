package com.shashwat.blog_app_youtube.Dtos_Payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PostRequestDto {

    private Long postID;

    private String title;

    private String content;
    private String imageName;

}
