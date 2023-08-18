package com.shashwat.blog_app_youtube.Controllers;

import com.shashwat.blog_app_youtube.Config.AppConstants;
import com.shashwat.blog_app_youtube.Dtos_Payloads.*;
import com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination.PostPaginationResponse;
import com.shashwat.blog_app_youtube.Services.implementation.FileServiceImple;
import com.shashwat.blog_app_youtube.Services.implementation.PostServiceImple;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/")
public class PostController {
    private PostServiceImple postService;
    private FileServiceImple fileService;

    @Value("${project.image}")
    private String path;


    @Autowired
    public PostController(PostServiceImple postService , FileServiceImple fileService) {
        this.postService = postService;
        this.fileService=fileService;
    }

    //CREATE POST
    @PostMapping("/user/{userID}/category/{categoryID}/posts")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto request,
                                                      @PathVariable Long userID,
                                                      @PathVariable Long categoryID){
        PostResponseDto createdPost =postService.createPost(request,userID,categoryID);

        return new ResponseEntity<PostResponseDto>(createdPost, HttpStatus.CREATED);
    }
    //UPDATE POST
    @PutMapping("/posts/{postID}")
    public ResponseEntity<PostResponseDto> updatePostByID(@RequestBody PostRequestDto request,
                                                          @PathVariable Long postID){

        PostResponseDto response=postService.updatePost(request,postID);

        return new ResponseEntity<PostResponseDto>(response,HttpStatus.OK);
    }

    //Get post by Title
    @GetMapping("/posts/search/{keyword}")
    public ResponseEntity <PostPaginationResponse> searchPostByTitle (
            @PathVariable String keyword,
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize" ,defaultValue = AppConstants.PAGE_SIZE ,required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false) String sortDir){

        PostPaginationResponse response=postService.searchPostByTitle(keyword,pageNumber, pageSize ,sortBy , sortDir);
        return new ResponseEntity<>( response ,HttpStatus.OK);

    }

    //Get Post by User
    @GetMapping("/user/{userID}/posts")
    public ResponseEntity<PostPaginationResponse> getPostByUser(
            @PathVariable Long userID,
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize" ,defaultValue = AppConstants.PAGE_SIZE ,required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false) String sortDir){

        PostPaginationResponse postResponse=postService.getPostByUser(userID,pageNumber, pageSize ,sortBy , sortDir);
        return new ResponseEntity<>(postResponse,HttpStatus.OK);
    }
    //Get Post by Category
    @GetMapping("/category/{categoryID}/posts")
    public ResponseEntity<PostPaginationResponse> getPostByCategory(
            @PathVariable Long categoryID,
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize" ,defaultValue = AppConstants.PAGE_SIZE ,required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false) String sortDir){

        PostPaginationResponse response=postService.getPostByCategory(categoryID,pageNumber, pageSize ,sortBy , sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    //Get All Post
    @GetMapping("/posts")
    public ResponseEntity<PostPaginationResponse> getAllPost(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize" ,defaultValue = AppConstants.PAGE_SIZE ,required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false) String sortDir){

        PostPaginationResponse postResponse=postService.getAllPost(pageNumber, pageSize ,sortBy , sortDir);

        return new ResponseEntity<>(postResponse,HttpStatus.OK);

    }

    //Get Post By ID
    @GetMapping("/posts/{postID}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long postID){

        PostResponseDto responsePost=postService.getPostByID(postID);
        return new ResponseEntity<PostResponseDto>(responsePost,HttpStatus.OK);
    }

    //Delete post
    @DeleteMapping("/posts/{postID}")
    public ResponseEntity<ApiResponseDto> deletePostByID(@PathVariable Long postID){
        postService.deletePost(postID);

        return new ResponseEntity<ApiResponseDto>( new ApiResponseDto("SUCCESS" ,"Post is Deleted"), HttpStatus.OK);
    }

    //POST IMAGE UPLOAD
    @PostMapping("/posts/image/upload/{postID}")
    public ResponseEntity<ImageResponseDto> uploadPostImage(
            @RequestParam MultipartFile image,
             @PathVariable Long postID
            ) throws IOException {

            PostResponseDto response=postService.getPostByID(postID);
            ImageResponseDto imageResponse= new ImageResponseDto();

            //TODO: Additional checks
            if(!image.getContentType().equals(MediaType.IMAGE_JPEG_VALUE)){

                imageResponse.setStatus("FAILURE");
                imageResponse.setMessage("Only JPEG IS ALLOWED ");
                imageResponse.setImageURL("NULL");

                return new ResponseEntity<>(imageResponse,HttpStatus.INTERNAL_SERVER_ERROR);
            }
//            if(image.isEmpty()){
//                updatedResponse.setContent("File is Empty .. ! /n Something went wrong ");
//                return new ResponseEntity<>(updatedResponse,HttpStatus.INTERNAL_SERVER_ERROR);
//            }

            String fileName=fileService.uploadImage(path,image);

            PostRequestDto updatedRequest= new PostRequestDto();
            updatedRequest.setPostID(postID);
            updatedRequest.setImageName(fileName);
            updatedRequest.setTitle(response.getTitle());
            updatedRequest.setContent(response.getContent());

            postService.updatePost( updatedRequest , postID);

            imageResponse.setStatus("SUCCESS");
            imageResponse.setMessage("File Uploaded Successfully");
            imageResponse.setImageURL(
                    ServletUriComponentsBuilder.fromCurrentContextPath().path("/post/image/").path(fileName).toUriString()
            );

            return new ResponseEntity<>(imageResponse,HttpStatus.OK);
    }

    //METHOD to SERVE FILE
        //localhost:9999/post/images/abc.png
    @GetMapping( value = "/post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse servletImageResponse // to send image data in response we need
    ) throws IOException {

        InputStream resource =fileService.getResource(path,imageName);
        servletImageResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,servletImageResponse.getOutputStream());
    }
}
