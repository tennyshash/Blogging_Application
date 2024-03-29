package com.shashwat.blog_app_youtube.Controllers;

import com.shashwat.blog_app_youtube.Config.AppConstants;
import com.shashwat.blog_app_youtube.Dtos_Payloads.ApiResponseDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.ForgetPasswordDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.UpdateUserDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.UserDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination.UserPaginationResponse;
import com.shashwat.blog_app_youtube.Services.UserService;
import com.shashwat.blog_app_youtube.Services.implementation.UserServiceImple;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
    //CREATE
    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid  @RequestBody UserDto userDto){
        UserDto createdUser=userService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }   */

                                            /*  Password Reset */
    @PutMapping("/forgetPassword")
    public ResponseEntity<ApiResponseDto> forgetPassword(@RequestBody ForgetPasswordDto request){

        ApiResponseDto response=userService.forgetPassword(request);
        return new ResponseEntity<ApiResponseDto>( response, HttpStatus.ACCEPTED);
    }

                                            /*  UPDATE USER */
    @PutMapping("/{userID}")
    public  ResponseEntity<UpdateUserDto> updateUser(@Valid @RequestBody UpdateUserDto updateUser, @PathVariable Long userID){
        UpdateUserDto updatedUSer= userService.updateUser(updateUser,userID);
        return   ResponseEntity.ok(updatedUSer);
    }

                                            /*   GET A USER     */
    //@JsonView(View.Admin.class)
    @GetMapping("/{userID}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable Long userID){
        return ResponseEntity.ok(userService.getUserById(userID));
    }

                                            /*   FOLLOW     */
    @PostMapping("/{followerID}/following/{followingID}/follow")
    public ResponseEntity<ApiResponseDto> followUser(@PathVariable Long followerID , @PathVariable Long followingID ){
        return ResponseEntity.ok(userService.followUser(followerID,followingID));
    }

                                            /*  UNFOLLOW    */
    @PutMapping("/{followerID}/following/{followingID}/unfollow")
    public ResponseEntity<ApiResponseDto> unFollowUser(@PathVariable Long followerID , @PathVariable Long followingID ){
        return ResponseEntity.ok(userService.unFollowUser(followerID,followingID));
    }


                            /*          -->>>ADMIN USER Fields <<<<----     */

                                            /*  GET ALL USER    */
    //@JsonView(View.Admin.class) //  when we want to hide some sensitive content eg. password and display rest things
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/")
    public ResponseEntity<UserPaginationResponse> getAllUser(
            @RequestParam (value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam (value = "pageSize",  defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam (value = "sortBy",    defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam (value = "sortDir",   defaultValue = AppConstants.SORT_DIR, required = false) String sortDir ){

        UserPaginationResponse response=userService.getAllUser(pageNumber,pageSize,sortBy,sortBy);
        return ResponseEntity.ok().body(response);
    }

                                            /*  DELETE USER   */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{userID}")
    public ResponseEntity<ApiResponseDto> deleteUser(@PathVariable ("userID") Long uID){
        userService.deleteUser(uID);
        return new ResponseEntity( new ApiResponseDto("SUCCESS","User Have Been Deleted")
                ,HttpStatus.OK);
    }

                                            /*  UPDATE ROLE */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{userID}/role/{roleID}")
    public ResponseEntity<ApiResponseDto> updateRole(@PathVariable Long userID,
                                                     @PathVariable Integer roleID){
        userService.updateRole(userID,roleID);
        ApiResponseDto responseDto= new ApiResponseDto("SUCCESS","Role Has been Updated..!");

        return ResponseEntity.ok(responseDto);
    }
}
