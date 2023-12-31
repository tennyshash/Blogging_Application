package com.shashwat.blog_app_youtube.Services.implementation;

import com.shashwat.blog_app_youtube.Dtos_Payloads.LikeDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination.PostPaginationResponse;
import com.shashwat.blog_app_youtube.Dtos_Payloads.PostRequestDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.PostResponseDto;
import com.shashwat.blog_app_youtube.Exception.ResourceNotFoundException;
import com.shashwat.blog_app_youtube.Models.Category;
import com.shashwat.blog_app_youtube.Models.Likes;
import com.shashwat.blog_app_youtube.Models.Post;
import com.shashwat.blog_app_youtube.Models.User;
import com.shashwat.blog_app_youtube.Repository.CategoryRepository;
import com.shashwat.blog_app_youtube.Repository.LikesRepository;
import com.shashwat.blog_app_youtube.Repository.PostRepository;
import com.shashwat.blog_app_youtube.Repository.UserRepository;
import com.shashwat.blog_app_youtube.Services.LikeService;
import com.shashwat.blog_app_youtube.Services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImple implements PostService {
    private ModelMapper modelMapper;
    private PostRepository postRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private LikeService likeService;


    @Autowired
    public PostServiceImple(ModelMapper modelMapper, PostRepository postRepository
            , UserRepository userRepository, CategoryRepository categoryRepository
            , LikeService likeService) {
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.likeService=likeService;
    }


    //CREATE
    @Override
    public PostResponseDto createPost(PostRequestDto postRequestDto, Long userID, Long categoryID){
        User user=userRepository.findById(userID)
                .orElseThrow( ()-> new ResourceNotFoundException("USer" , "UserID", userID));

        Category category= categoryRepository.findById(categoryID)
                .orElseThrow( ()-> new ResourceNotFoundException("Category" , "Category ID", categoryID));

        Post post=modelMapper.map(postRequestDto, Post.class);
        post.setImageName("default.png");
        post.setUser(user);
        post.setCategory(category);

        Post newPost=postRepository.save(post);

        return modelMapper.map(post, PostResponseDto.class);
    }
    //UPDATE POST
    @Override
    public PostResponseDto updatePost(PostRequestDto request, Long postID) {
        Post post = postRepository.findById(postID)
                .orElseThrow(() -> new ResourceNotFoundException("Post ", "Post ID", postID));

        // Adding If-else for extra verification in case user passes null/ empty field while updating .!
        Category category = null;
        if (request.getCategory() != null) {
            category = categoryRepository.findById(request.getCategory().getCategoryID())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "Category ID", request.getCategory().getCategoryID()));
        }else{
            category=post.getCategory();
        }

        post.setTitle(request.getTitle() == null ? post.getTitle() : request.getTitle());
        post.setContent(request.getContent() == null ? post.getContent() : request.getContent());
        post.setImageName(request.getImageName() == null ? post.getImageName() : request.getImageName());
        post.setCategory( category );

        Post updatedPost = postRepository.save(post);
        return modelMapper.map(updatedPost, PostResponseDto.class);
    }

    //DELETE POST
    @Override
    public void deletePost(Long postID){
        Post post=postRepository.findById(postID)
                .orElseThrow( ()-> new ResourceNotFoundException( "Post ", "Post ID" ,postID));
        postRepository.delete(post);
    }

    //GET POST BY ID
    @Override
    public PostResponseDto getPostByID(Long postID){
        Post post=postRepository.findById(postID)
                .orElseThrow( ()-> new ResourceNotFoundException( "Post ", "Post ID" ,postID));

        PostResponseDto response= modelMapper.map(post, PostResponseDto.class);

        response.setLikes( likeService.getLikesOnPost(postID));

         return response;
    }

    //GET ALL POST
    @Override
    public PostPaginationResponse getAllPost(Integer pageNumber, Integer pageSize , String sortBy, String sortDir){
                            // ----->  METHOD HERE
        Pageable pageable=this.creatingPageable(pageNumber, pageSize , sortBy, sortDir);

        Page<Post> pagePost =postRepository.findAll(pageable);
        List<Post> posts=pagePost.getContent();

        List<PostResponseDto> responseDtoList=posts.stream()
                .map( post -> modelMapper.map( post, PostResponseDto.class)).collect(Collectors.toList());
                            // ----->  METHOD HERE
        return this.settingPostResponseFromPageable( responseDtoList,  pagePost);
    }

    //GET POST BY CATEGORY
    @Override
    public PostPaginationResponse getPostByCategory(Long categoryID ,Integer pageNumber, Integer pageSize ,
                                                    String sortBy, String sortDir){
                            // ----->  METHOD HERE
        Pageable pageable=this.creatingPageable(pageNumber, pageSize , sortBy, sortDir);

        Category category=categoryRepository.findById(categoryID)
                .orElseThrow( ()-> new ResourceNotFoundException("Category" , "Category ID" , categoryID));

        Page<Post> pagePost=postRepository.findAllByCategory_Id(categoryID,pageable);
        List<Post> posts=pagePost.getContent();

        List<PostResponseDto> responseDtoList=posts.stream()
                .map( post -> modelMapper.map( post, PostResponseDto.class)).collect(Collectors.toList());
                            // ----->  METHOD HERE
        return this.settingPostResponseFromPageable( responseDtoList,  pagePost);
    }

    //GET POST BY USER
    @Override
    public PostPaginationResponse getPostByUser(Long userID ,Integer pageNumber, Integer pageSize ,
                                               String sortBy, String sortDir){
        // ----->  METHOD HERE
        Pageable pageable=this.creatingPageable(pageNumber, pageSize , sortBy, sortDir);

        User user=userRepository.findById(userID)
                .orElseThrow( ()-> new ResourceNotFoundException("User" , "User ID" , userID));

        Page<Post> postList=postRepository.findAllByUserId(userID,pageable);
        List<Post> posts=postList.getContent();

        List<PostResponseDto> postResponseList=postList.stream().map(
                post -> modelMapper.map(post,PostResponseDto.class)).collect(Collectors.toList());

        return this.settingPostResponseFromPageable( postResponseList,  postList);
    }

    //GET POST BY KEYWORD
    @Override
    public PostPaginationResponse searchPostByTitle(String keyword,Integer pageNumber, Integer pageSize ,
                                                   String sortBy, String sortDir){
        // ----->  METHOD HERE
        Pageable pageable=this.creatingPageable(pageNumber, pageSize , sortBy, sortDir);
        Page<Post> posts= postRepository.findByTitleContaining(keyword,pageable);
//        List<Post> posts= postRepository.searchByTitle("%" + keyword + "%");
        List<PostResponseDto> postResponseList=posts.stream()
                .map( (post -> modelMapper.map(post, PostResponseDto.class))).collect(Collectors.toList());

        return this.settingPostResponseFromPageable( postResponseList,  posts);
    }

                                // ----->  METHOD HERE FOR PAGINATION
    private PostPaginationResponse settingPostResponseFromPageable(List<PostResponseDto> responseDtoList, Page<Post>  pagePost){

        PostPaginationResponse postPaginationResponse= new PostPaginationResponse();
        postPaginationResponse.setContent(responseDtoList);
        postPaginationResponse.setPageNumber(pagePost.getNumber());
        postPaginationResponse.setPageSize(pagePost.getSize());
        postPaginationResponse.setTotalElements(pagePost.getTotalElements());

        postPaginationResponse.setTotalPages(pagePost.getTotalPages());
        postPaginationResponse.setLastPage(pagePost.isLast());

        return postPaginationResponse;
    }
    private Pageable creatingPageable(Integer pageNumber, Integer pageSize , String sortBy, String sortDir){

        // adding code for sorting by ascending or desc using ternary operator
        Sort sort= sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // DOING PAGINATION
        return  PageRequest.of(pageNumber,pageSize, sort);
        // here by default is ascending but this is also a way
    }
}
