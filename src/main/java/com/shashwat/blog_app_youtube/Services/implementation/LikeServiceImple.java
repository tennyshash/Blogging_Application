package com.shashwat.blog_app_youtube.Services.implementation;

import com.shashwat.blog_app_youtube.Dtos_Payloads.LikeDto;
import com.shashwat.blog_app_youtube.Exception.ApiException;
import com.shashwat.blog_app_youtube.Exception.ResourceNotFoundException;
import com.shashwat.blog_app_youtube.Models.Likes;
import com.shashwat.blog_app_youtube.Models.Post;
import com.shashwat.blog_app_youtube.Models.User;
import com.shashwat.blog_app_youtube.Repository.LikesRepository;
import com.shashwat.blog_app_youtube.Repository.PostRepository;
import com.shashwat.blog_app_youtube.Repository.UserRepository;
import com.shashwat.blog_app_youtube.Services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeServiceImple implements LikeService {

    private UserRepository userRepository;
    private PostRepository postRepository;

    private LikesRepository likesRepository;

    @Autowired
    public LikeServiceImple ( UserRepository userRepository, PostRepository postRepository,
                              LikesRepository likesRepository){
        this.userRepository=userRepository;
        this.postRepository=postRepository;
        this.likesRepository=likesRepository;

    }
    @Override
    public LikeDto userLikesPost(Long userID, Long postID) {

        Post post=postRepository.findById(postID)
                .orElseThrow( ()-> new ResourceNotFoundException( "Post ", "Post ID" ,postID));

        User user= userRepository.findById(userID).orElseThrow( ()-> new ResourceNotFoundException("User", "ID", userID));

        // To Avoid Multiple likes by user
        Optional<Likes> likes=likesRepository.getByPostIdAndAndUserId(postID,userID);
        if(likes.isPresent())   return null;

        Likes newLike= new Likes();
            newLike.setUser(user);
            newLike.setPost(post);

        Likes liked=likesRepository.save(newLike);

        LikeDto response= new LikeDto();
            response.setLikeID(liked.getId());
            response.setUserName(liked.getUser().getName());
            response.setUserID(liked.getUser().getId());

            return response;
    }

    @Override
    public LikeDto userDislikesPost(Long userID,Long postID){

        Post post=postRepository.findById(postID)
                .orElseThrow( ()-> new ResourceNotFoundException( "Post ", "Post ID" ,postID));
        User user= userRepository.findById(userID).orElseThrow( ()-> new ResourceNotFoundException("User", "ID", userID));

        Optional<Likes> likes=likesRepository.getByPostIdAndAndUserId(postID,userID);
        if(likes.isEmpty()){
            throw new ApiException("NO Such Like Present with User ID : "+ userID + " & Post ID :" + postID +" .");
        }
        likesRepository.delete(likes.get());

        return  null;
    }

    @Override
    public List<LikeDto> getLikesOnPost(Long postID) {

        Post post=postRepository.findById(postID)
                .orElseThrow( ()-> new ResourceNotFoundException( "Post ", "Post ID" ,postID));

        List<Likes> totalLikes= likesRepository.getAllByPostId(postID);

        List<LikeDto> likeDtoList=totalLikes.stream().map( like->{

            LikeDto likeDto= new LikeDto();
            likeDto.setUserName(like.getUser().getName());
            likeDto.setLikeID(like.getId());
            likeDto.setUserID(like.getUser().getId());
            return likeDto;

        }).collect(Collectors.toList());

        return likeDtoList;

    }
}
