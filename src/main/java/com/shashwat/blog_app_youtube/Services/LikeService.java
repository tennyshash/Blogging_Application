package com.shashwat.blog_app_youtube.Services;

import com.shashwat.blog_app_youtube.Dtos_Payloads.LikeDto;

import java.util.List;

public interface LikeService {

    LikeDto userLikesPost(Long userID, Long postID);

    LikeDto userDislikesPost(Long userID,Long postID);

    List<LikeDto> getLikesOnPost(Long postID);

}
