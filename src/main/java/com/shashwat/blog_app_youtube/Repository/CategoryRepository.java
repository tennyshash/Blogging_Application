package com.shashwat.blog_app_youtube.Repository;

import com.shashwat.blog_app_youtube.Models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Override
    Optional<Category> findById(Long aLong);

    @Override
    Page<Category> findAll(Pageable pageable);

    @Override
    void delete(Category entity);

    Category save(Category category);
}
