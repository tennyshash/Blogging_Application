package com.shashwat.blog_app_youtube.Dtos_Payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
public class RoleDto {

    //@JsonView(View.Admin.class)
    @JsonIgnore
    private Integer id;


    private String roleName;
}
