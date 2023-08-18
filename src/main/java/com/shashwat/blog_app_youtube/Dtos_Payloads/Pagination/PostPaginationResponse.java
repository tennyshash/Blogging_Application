package com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination;

import com.shashwat.blog_app_youtube.Dtos_Payloads.PostResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor

public class PostPaginationResponse {

    private List<PostResponseDto> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;

}
