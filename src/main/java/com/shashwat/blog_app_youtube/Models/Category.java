package com.shashwat.blog_app_youtube.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category extends BaseModel{

    @Column(name = "Title",length = 50, nullable = false)
    private String categoryTitle;

    @Column(name = "Description")
    private String categoryDescription;

    @OneToMany(mappedBy = "category" , cascade = CascadeType.ALL , fetch = FetchType.LAZY )
    private List<Post> postList= new ArrayList<>();
}
