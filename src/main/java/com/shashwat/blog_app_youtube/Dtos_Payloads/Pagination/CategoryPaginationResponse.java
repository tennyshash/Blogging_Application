package com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination;

import com.shashwat.blog_app_youtube.Dtos_Payloads.CategoryDto;
import lombok.Data;

import java.util.List;

@Data
public class CategoryPaginationResponse {

    List<CategoryDto> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;
}
