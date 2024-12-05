package com.aloha.movie_project.domain;

import java.util.UUID;

import lombok.Data;

@Data
public class Cast {
    private int no;
    private String id;
    private String movieId;
    private String rule;
    private String name;
    private Files files;
    
    public Cast() {
        this.id = UUID.randomUUID().toString();
    }
}
