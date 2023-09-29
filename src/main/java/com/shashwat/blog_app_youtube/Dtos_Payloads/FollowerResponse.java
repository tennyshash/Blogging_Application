package com.shashwat.blog_app_youtube.Dtos_Payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerResponse {

    //@JsonIgnore
    private Long userID;
    private String userName;

}
