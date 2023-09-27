package com.shashwat.blog_app_youtube.Controllers;

import com.shashwat.blog_app_youtube.Dtos_Payloads.ApiResponseDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.LikeDto;
import com.shashwat.blog_app_youtube.Services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class LikeController {

    private LikeService likeService;

    @Autowired
    public LikeController (LikeService likeService){
        this.likeService=likeService;
    }


    //  LIKE POST
    @PostMapping("/user/{userID}/post/{postID}/like")
    public ResponseEntity<LikeDto> userLikesPost (@PathVariable Long userID, @PathVariable Long postID){
        LikeDto response =likeService.userLikesPost(userID,postID);
        return ResponseEntity.ok( response);
    }

    // DisLike POST
    @DeleteMapping("/user/{userID}/post/{postID}/dislike")
    public ResponseEntity<ApiResponseDto> userDislikesPost (@PathVariable Long userID, @PathVariable Long postID){
        likeService.userDislikesPost(userID,postID);

        return ResponseEntity.ok( new ApiResponseDto("SUCCESS","Disliked Successfully"));
    }

    //  Get Likes on Post
    @GetMapping("/like/post/{postID}")
    public ResponseEntity<List<LikeDto>> getLikesOnPost (@PathVariable Long postID){
        List<LikeDto> likes =likeService.getLikesOnPost(postID);

        return ResponseEntity.ok( likes);
    }

    //is User Liked Post

}
