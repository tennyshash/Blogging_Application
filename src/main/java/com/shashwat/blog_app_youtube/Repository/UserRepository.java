package com.shashwat.blog_app_youtube.Repository;

import com.shashwat.blog_app_youtube.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    User save(User user);

    @Override
    Optional<User> findById(Long aLong);

    @Override
    Page<User> findAll(Pageable pageable);

    @Override
    void delete(User entity);
}
