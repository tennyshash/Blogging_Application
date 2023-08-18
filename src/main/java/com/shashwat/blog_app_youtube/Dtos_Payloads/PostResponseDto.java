package com.shashwat.blog_app_youtube.Dtos_Payloads;

import com.shashwat.blog_app_youtube.Models.Category;
import com.shashwat.blog_app_youtube.Models.Comment;
import com.shashwat.blog_app_youtube.Models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class PostResponseDto {

    private Long postID;
    private String title;
    private String content;
    private String imageName;
    private CategoryDto category;
    private UserDto user;
    private Set<CommentDto >comments = new HashSet<>();
}
