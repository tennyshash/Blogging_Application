package com.shashwat.blog_app_youtube.Dtos_Payloads;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter

public class CommentDto {

    private Long commentID;
    private Date createdAt;
    private String content;
    private Long userId;
    private String userName;
}
