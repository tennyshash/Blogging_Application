package com.shashwat.blog_app_youtube.Dtos_Payloads;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
public class RoleDto {

    @JsonView(View.Admin.class)
    private Integer id;

    @JsonView(View.Base.class)
    private String roleName;
}
