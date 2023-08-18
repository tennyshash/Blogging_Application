package com.shashwat.blog_app_youtube.Repository;

import com.shashwat.blog_app_youtube.Models.Category;
import com.shashwat.blog_app_youtube.Models.Post;
import com.shashwat.blog_app_youtube.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {


    //Pagination is being used


    @Override
    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByUserId(Long userID,Pageable pageable);

    Page<Post> findAllByCategory_Id(Long categoryID,Pageable pageable);


    //does a like query on title
    Page<Post> findByTitleContaining(String title ,Pageable pageable);
                    //OR
//    @Query("select p from Post p where p.title like : key") // for dynamically taking values we used key
//    List<Post> searchByTitle(@Param("key") String title);
//    // in service when passing keyword -> "%+keyword+%"


    @Override
    void delete(Post entity);

    @Override
    Optional<Post> findById(Long aLong);

    Post save(Post post);


}
