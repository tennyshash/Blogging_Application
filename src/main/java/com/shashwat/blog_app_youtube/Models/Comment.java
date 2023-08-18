package com.shashwat.blog_app_youtube.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Comment extends BaseModel{

    private String content;

    @ManyToOne
    private User user;

    @ManyToOne
    private Post post;

}
