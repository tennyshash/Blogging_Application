package com.shashwat.blog_app_youtube.Dtos_Payloads;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {

    private Long categoryID;

    @Size(min = 4, message = "Min size of category title is 4")
    private String categoryTitle;

    @Size(min = 10,  message = "Min size of category title is 10 ")
    private String categoryDescription;
}
