package com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination;

import com.shashwat.blog_app_youtube.Dtos_Payloads.UserDto;
import lombok.Data;

import java.util.List;

@Data
public class UserPaginationResponse {

    private List<UserDto> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;
}
