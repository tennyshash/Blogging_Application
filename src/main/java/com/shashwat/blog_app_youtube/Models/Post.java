package com.shashwat.blog_app_youtube.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Post")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post  extends  BaseModel{

    @Column(name = "Post_title",nullable = false,length = 100)
    private String title;

    @Column(length = 100000)
    private String content;
    private String imageName;

    @ManyToOne
    //@JoinColumn(name = "category")
    private Category category;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "post" ,fetch = FetchType.LAZY ,cascade = CascadeType.ALL )
    @JsonManagedReference // Add this annotation to indicate the "like" property is managed (forward) side
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonManagedReference // Add this annotation to indicate the "like" property is managed (forward) side
    private List<Comment> comments= new ArrayList<>();
}
