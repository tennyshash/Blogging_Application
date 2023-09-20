package com.shashwat.blog_app_youtube.Services.implementation;

import com.shashwat.blog_app_youtube.Dtos_Payloads.CommentDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination.CategoryPaginationResponse;
import com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination.CommentPaginationResponse;
import com.shashwat.blog_app_youtube.Exception.ResourceNotFoundException;
import com.shashwat.blog_app_youtube.Models.Comment;
import com.shashwat.blog_app_youtube.Models.Post;
import com.shashwat.blog_app_youtube.Models.User;
import com.shashwat.blog_app_youtube.Repository.CommentRepository;
import com.shashwat.blog_app_youtube.Repository.PostRepository;
import com.shashwat.blog_app_youtube.Repository.UserRepository;
import com.shashwat.blog_app_youtube.Services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImple implements CommentService {

    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CommentServiceImple(PostRepository postRepository, CommentRepository commentRepository,
                               ModelMapper modelMapper , UserRepository userRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.modelMapper=modelMapper;
        this.userRepository=userRepository;
    }

    @Override
    public CommentDto createComment(CommentDto commentDto,Long userID, Long postID ) {

        Post post= postRepository.findById(postID).orElseThrow( ()-> new ResourceNotFoundException( "Post" , "post id" , postID));
        Comment comment=modelMapper.map(commentDto, Comment.class);

        User user=userRepository.findById(userID).orElseThrow( ()-> new ResourceNotFoundException("User" , "User ID",userID));

        comment.setPost(post);
        comment.setUser(user);

        Comment savedComment=commentRepository.save(comment);

        return modelMapper.map(savedComment,CommentDto.class);
    }

    @Override
    public void deleteComment(Long commentID) {

        Comment comment=commentRepository.findById(commentID).orElseThrow(
                ()-> new ResourceNotFoundException("Comment" , " comment ID", commentID)
        );
        commentRepository.delete(comment);
    }


    @Override
    public CommentPaginationResponse getCommentByUser(Long userID, Integer pageNumber, Integer pageSize,
                                                      String sortBy, String sortDir) {
        // adding code for sorting by ascending or desc using ternary operator
        Sort sort=sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // DOING PAGINATION
        Pageable pageRequest= PageRequest.of(pageNumber,pageSize,sort);

        User user=userRepository.findById(userID).orElseThrow(
                ()-> new ResourceNotFoundException("User" , "User ID " , userID));

        Page<Comment> commentPage=commentRepository.findAllByUser(user,pageRequest);
        List<Comment> comments=commentPage.getContent();

        List<CommentDto> commentDtosList= comments.stream()
                .map( comment -> modelMapper.map(comment,CommentDto.class)).collect(Collectors.toList());

        CommentPaginationResponse response= new CommentPaginationResponse();
            response.setContent(commentDtosList);
            response.setPageNumber(commentPage.getNumber());
            response.setPageSize(commentPage.getSize());
            response.setTotalElements(commentPage.getTotalElements());

            response.setTotalPages(commentPage.getTotalPages());
            response.setLastPage(commentPage.isLast());

        return response;
    }
}
