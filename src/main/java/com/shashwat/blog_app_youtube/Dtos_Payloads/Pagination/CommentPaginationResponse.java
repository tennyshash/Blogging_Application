package com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination;

import com.shashwat.blog_app_youtube.Dtos_Payloads.CategoryDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.CommentDto;
import lombok.Data;

import java.util.List;

@Data
public class CommentPaginationResponse {

    List<CommentDto> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;
}
