package com.shashwat.blog_app_youtube.Services;

import com.shashwat.blog_app_youtube.Dtos_Payloads.ApiResponseDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.ForgetPasswordDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.UpdateUserDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.UserDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination.UserPaginationResponse;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface UserService {
    UserDto registerNewUSer(UserDto user) ;
    ApiResponseDto forgetPassword(ForgetPasswordDto userDto);

    UserDto createUser(UserDto userDto);
    UpdateUserDto updateUser(UpdateUserDto userDto, Long userId);

    UserDto getUserById(Long userId);

    ApiResponseDto followUser(Long followerID , Long followingID);
    ApiResponseDto unFollowUser(Long followerID , Long followingID);

                    /*          -->>>ADMIN USER Fields <<<<----     */

    UserPaginationResponse getAllUser(Integer pageNumber, Integer pageSize , String sortBy, String sortDir);
                  /*            -->>> Apache POI <<<<--*/
    ApiResponseDto registerUsersInBulk(String url) throws IOException;
    void writeInExcel(String url,String sheet,int nRow, int nCell, String Data) throws IOException;


    void deleteUser(Long userId);

    void updateRole(Long userID, Integer roleID);
}
