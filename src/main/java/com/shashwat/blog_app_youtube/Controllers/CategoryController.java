package com.shashwat.blog_app_youtube.Controllers;

import com.shashwat.blog_app_youtube.Config.AppConstants;
import com.shashwat.blog_app_youtube.Dtos_Payloads.CategoryDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.ApiResponseDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination.CategoryPaginationResponse;
import com.shashwat.blog_app_youtube.Models.Category;
import com.shashwat.blog_app_youtube.Repository.CategoryRepository;
import com.shashwat.blog_app_youtube.Services.CategoryService;
import com.shashwat.blog_app_youtube.Services.implementation.CategoryServiceImple;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //create
    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@Valid  @RequestBody CategoryDto categoryDto ){
        CategoryDto createCategory= categoryService.createCategory(categoryDto);
        return new ResponseEntity<CategoryDto>(createCategory, HttpStatus.CREATED);
    }
    //update
    @PutMapping("/{categoryID}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto ,@PathVariable Long categoryID){
        CategoryDto updatedCategory= categoryService.updateCategory(categoryDto,categoryID);
        return new ResponseEntity<CategoryDto>(updatedCategory, HttpStatus.OK);
    }
    //delete
    @DeleteMapping("/{categoryID}")
    public ResponseEntity<ApiResponseDto> deleteCategory(@PathVariable Long categoryID){
        categoryService.deleteCategory(categoryID);
        return new ResponseEntity<ApiResponseDto>( new ApiResponseDto("SUCCESS","Category is deleted"),HttpStatus.OK);
    }
    //get one
    @GetMapping("/{categoryID}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long categoryID){
        CategoryDto category= categoryService.getCategory(categoryID);
        return new ResponseEntity<CategoryDto>( category, HttpStatus.OK);
    }
    //get all
    @GetMapping("/")
    public ResponseEntity<CategoryPaginationResponse> getAllCategory(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize" ,defaultValue = AppConstants.PAGE_SIZE ,required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false) String sortDir ){

        CategoryPaginationResponse categoryList= categoryService.getAllCategory(pageNumber,pageSize,sortBy, sortDir);
        return ResponseEntity.ok(categoryList);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDto>> getAllCategory( ){

        List<Category> categoryList=categoryRepository.findAll();
        List<CategoryDto> responses= categoryList.stream().map( category -> modelMapper.map(category,CategoryDto.class)).collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }
}
