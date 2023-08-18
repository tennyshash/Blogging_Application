package com.shashwat.blog_app_youtube.Controllers;

import com.shashwat.blog_app_youtube.Config.AppConstants;
import com.shashwat.blog_app_youtube.Dtos_Payloads.ApiResponseDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.UserDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination.UserPaginationResponse;
import com.shashwat.blog_app_youtube.Services.implementation.UserServiceImple;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserServiceImple userService;

    @Autowired
    public UserController(UserServiceImple userService) {
        this.userService = userService;
    }

    //CREATE
    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid  @RequestBody UserDto userDto){
        UserDto createdUser=userService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    //UPDATE
    @PutMapping("/{userID}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable Long userID){
        UserDto updatedUSer= userService.updateUser(userDto,userID);
        return   ResponseEntity.ok(updatedUSer);
    }

    //DELETE only Admin can delete User (Use Case)

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{userID}")
    public ResponseEntity<ApiResponseDto> deleteUser(@PathVariable ("userID") Long uID){
        userService.deleteUser(uID);
        return new ResponseEntity( new ApiResponseDto("SUCCESS","User Have Been Deleted")
                ,HttpStatus.OK);
    }
    //GET ALL USER
    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUser(
            @RequestParam (value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam (value = "pageSize",  defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam (value = "sortBy",    defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam (value = "sortDir",   defaultValue = AppConstants.SORT_DIR, required = false) String sortDir ){

        UserPaginationResponse response=userService.getAllUser(pageNumber,pageSize,sortBy,sortBy);
        return ResponseEntity.ok().body(response.getContent());
    }

    //GET A USER
    @GetMapping("/{userID}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable Long userID){
        return ResponseEntity.ok(userService.getUserById(userID));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{userID}/role/{roleID}")
    public ResponseEntity<ApiResponseDto> updateRole(@PathVariable Long userID,
                                                     @PathVariable Integer roleID){
        userService.updateRole(userID,roleID);
        ApiResponseDto responseDto= new ApiResponseDto("SUCCESS","Role Has been Updated..!");

        return ResponseEntity.ok(responseDto);
    }
}
