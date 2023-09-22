package com.shashwat.blog_app_youtube.Services;

import com.shashwat.blog_app_youtube.Dtos_Payloads.UpdateUserDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.UserDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination.UserPaginationResponse;

public interface UserService {
    UserDto registerNewUSer(UserDto user);

    UserDto createUser(UserDto userDto);
    UpdateUserDto updateUser(UpdateUserDto userDto, Long userId);
    UserDto getUserById(Long userId);
    UserPaginationResponse getAllUser(Integer pageNumber, Integer pageSize , String sortBy, String sortDir);
    void deleteUser(Long userId);

    void updateRole(Long userID, Integer roleID);
}
