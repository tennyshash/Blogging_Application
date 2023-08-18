package com.shashwat.blog_app_youtube.Services;

import com.shashwat.blog_app_youtube.Dtos_Payloads.CommentDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination.CommentPaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto, Long postID);

    void deleteComment(Long commentID);

    CommentPaginationResponse getCommentByUser(Long userID, Integer pageNumber, Integer pageSize , String sortBy, String sortDir);
}
