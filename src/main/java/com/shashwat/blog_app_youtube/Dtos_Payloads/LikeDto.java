package com.shashwat.blog_app_youtube.Dtos_Payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LikeDto {

    @JsonIgnore
    private Long likeID;
    private Long userID;
    private String userName;

}
