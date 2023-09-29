package com.shashwat.blog_app_youtube.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.shashwat.blog_app_youtube.Dtos_Payloads.View;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.*;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User extends BaseModel implements UserDetails {

    @Column(name = "user_name",nullable = false,length = 100)
    
    private String name;

    private String password;

    @Column(unique = true)
    private String email;

    private String about;


//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Likes> like = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Comment> comments= new ArrayList<>();
//
//
//    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , fetch = FetchType.LAZY )
//    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // Add this annotation to indicate the "like" property is managed (forward) side
    private List<Likes> like = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // Add this annotation to indicate the "comments" property is managed (forward) side
    private List<Comment> comments= new ArrayList<>();

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , fetch = FetchType.LAZY )
    @JsonManagedReference // Add this annotation to indicate the "posts" property is managed (forward) side
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role", referencedColumnName = "id")
    )
    private Set<Role> roles= new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "followers",
            joinColumns = @JoinColumn(name = "following_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    //@JsonBackReference
    private List<User> followers;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "followers")
    //@JsonManagedReference
    private List<User> following;


    //----------------->METHODS of UserDetails interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> authorities=this.roles.stream().map(
                (role)-> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());

        return authorities;
        //TODO: note->>all the authorities will be used by spring security to work acc.
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
