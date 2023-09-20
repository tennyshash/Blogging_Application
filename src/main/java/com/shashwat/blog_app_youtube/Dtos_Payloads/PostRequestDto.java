package com.shashwat.blog_app_youtube.Dtos_Payloads;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PostRequestDto {

    private Long postID;

    @NotNull
    @Size(min = 4, message = "Min size of  title is 4")
    private String title;

    @NotNull
    private String content;

    private String imageName;
    
    private CategoryDto category;

}
