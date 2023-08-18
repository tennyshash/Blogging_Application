package com.shashwat.blog_app_youtube.Services.implementation;

import com.shashwat.blog_app_youtube.Dtos_Payloads.CategoryDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination.CategoryPaginationResponse;
import com.shashwat.blog_app_youtube.Exception.ResourceNotFoundException;
import com.shashwat.blog_app_youtube.Models.Category;
import com.shashwat.blog_app_youtube.Repository.CategoryRepository;
import com.shashwat.blog_app_youtube.Services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImple implements CategoryService {
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImple(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto){

        Category category=modelMapper.map(categoryDto,Category.class);
        Category savedCategory=categoryRepository.save(category);

        return modelMapper.map(savedCategory,CategoryDto.class);
    }
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryID){

        Category category=categoryRepository.findById(categoryID)
                .orElseThrow( ()-> new ResourceNotFoundException("Category" ,"Category ID" , categoryID));

        category.setCategoryTitle(categoryDto.getCategoryTitle());
        category.setCategoryDescription(categoryDto.getCategoryDescription());

        Category updatedCategory=categoryRepository.save(category);

        return modelMapper.map(updatedCategory,CategoryDto.class);
    }
    @Override
    public void deleteCategory(Long categoryID){
        Category category=categoryRepository.findById(categoryID)
                .orElseThrow( ()-> new ResourceNotFoundException("Category" ,"Category ID" , categoryID));
        categoryRepository.delete(category);

    }
    @Override
    public CategoryDto getCategory(Long categoryID){
        Category category=categoryRepository.findById(categoryID)
                .orElseThrow(()-> new ResourceNotFoundException("Category" ,"Category ID" , categoryID));

        return modelMapper.map(category, CategoryDto.class);
    }
    @Override
    public CategoryPaginationResponse getAllCategory(Integer pageNumber, Integer pageSize, String sortBy , String sortDir){

        // adding code for sorting by ascending or desc using ternary operator
        Sort sort=sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // DOING PAGINATION
        Pageable pageRequest= PageRequest.of(pageNumber,pageSize,sort);

        Page<Category> categoryPage=categoryRepository.findAll(pageRequest);
        List<Category> categories=categoryPage.getContent();

        List<CategoryDto> categoryDtoList=categories.stream()
                .map( category -> modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());

        CategoryPaginationResponse response= new CategoryPaginationResponse();
            response.setContent(categoryDtoList);
            response.setPageNumber(categoryPage.getNumber());
            response.setPageSize(categoryPage.getSize());
            response.setTotalElements(categoryPage.getTotalElements());

            response.setTotalPages(categoryPage.getTotalPages());
            response.setLastPage(categoryPage.isLast());

        return response;
    }
}
