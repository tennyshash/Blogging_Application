package com.shashwat.blog_app_youtube.Models;

import com.fasterxml.jackson.annotation.JsonView;
import com.shashwat.blog_app_youtube.Dtos_Payloads.View;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Setter
@Getter
public class Role {

    @Id
    @Column(nullable = false,name = "ID")
    private Integer id;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updateAt;


    private String roleName;

}
