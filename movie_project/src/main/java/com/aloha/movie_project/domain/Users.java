package com.aloha.movie_project.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Users {
    private String id;
    private String pw;
    private String name;
    private String email;
    private boolean enabled;
    private Date regDate;
    private Date updDate;

    private List<UserAuth> authList;

}
