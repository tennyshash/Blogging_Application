package com.shashwat.blog_app_youtube.Repository;

import com.shashwat.blog_app_youtube.Models.Comment;
import com.shashwat.blog_app_youtube.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Override
    Optional<Comment> findById(Long commentID);

    Comment save(Comment comment);

    @Override
    void delete(Comment entity);

    Page<Comment> findAllByUser(User user, Pageable pageable);
}
