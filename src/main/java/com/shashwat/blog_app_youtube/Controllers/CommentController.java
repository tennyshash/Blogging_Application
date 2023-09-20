package com.shashwat.blog_app_youtube.Controllers;

import com.shashwat.blog_app_youtube.Config.AppConstants;
import com.shashwat.blog_app_youtube.Dtos_Payloads.ApiResponseDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.CommentDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination.CategoryPaginationResponse;
import com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination.CommentPaginationResponse;
import com.shashwat.blog_app_youtube.Services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // Create Comment
    @PostMapping("/user/{userID}/post/{postID}/comments/")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto comment,
                                                    @PathVariable  Long userID,
                                                    @PathVariable Long postID){

        CommentDto newComment=commentService.createComment(comment,userID,postID);

        return new ResponseEntity<CommentDto>(newComment, HttpStatus.CREATED);

    }

    // Delete Comment
    @DeleteMapping("/comments/{commentID}")
    public ResponseEntity<ApiResponseDto> deleteComment(@PathVariable Long commentID){

        commentService.deleteComment(commentID);

        return new ResponseEntity<ApiResponseDto>(
                new ApiResponseDto( "SUCCESS", "Deleted Successfully"),HttpStatus.OK);

    }

    // Get Comment By User
    @GetMapping("/user/{userID}/comments")
    public ResponseEntity<CommentPaginationResponse> getAllCommentByUser (
            @PathVariable Long userID,
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize" ,defaultValue = AppConstants.PAGE_SIZE ,required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false) String sortDir){

        CommentPaginationResponse response=commentService.getCommentByUser(userID,pageNumber,pageSize,sortBy,sortDir);

        return ResponseEntity.ok(response);

    }
}
