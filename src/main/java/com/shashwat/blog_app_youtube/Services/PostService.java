package com.shashwat.blog_app_youtube.Services;

import com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination.PostPaginationResponse;
import com.shashwat.blog_app_youtube.Dtos_Payloads.PostRequestDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.PostResponseDto;

public interface PostService {

    PostResponseDto createPost(PostRequestDto postRequestDto, Long userID, Long categoryID);

    PostResponseDto updatePost(PostRequestDto request, Long postID);

    void deletePost(Long postID);

    PostResponseDto getPostByID(Long postID);

    PostPaginationResponse getAllPost(Integer pageNumber, Integer pageSize , String sortBy, String sortDir);

    PostPaginationResponse getPostByCategory(Long categoryID, Integer pageNumber, Integer pageSize , String sortBy, String sortDir);

    PostPaginationResponse getPostByUser(Long userID ,Integer pageNumber, Integer pageSize , String sortBy, String sortDir);

    PostPaginationResponse searchPostByTitle(String keyword, Integer pageNumber, Integer pageSize , String sortBy, String sortDir);
}
