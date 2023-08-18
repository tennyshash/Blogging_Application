package com.shashwat.blog_app_youtube.Services;

import com.shashwat.blog_app_youtube.Dtos_Payloads.CategoryDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination.CategoryPaginationResponse;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryID);
    void deleteCategory(Long categoryID);
    CategoryDto getCategory(Long categoryID);
    CategoryPaginationResponse getAllCategory(Integer pageNumber, Integer pageSize, String sortBy , String sortDir);

}
