package com.shashwat.blog_app_youtube.Repository;

import com.shashwat.blog_app_youtube.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository <Role,Integer>{

    @Override
    Optional<Role> findById(Integer integer);

    @Override
    <S extends Role> List<S> saveAll(Iterable<S> entities);
}
