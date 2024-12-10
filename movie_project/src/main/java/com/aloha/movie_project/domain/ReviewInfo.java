package com.aloha.movie_project.domain;

import java.util.UUID;

import lombok.Data;

@Data
public class ReviewInfo {
    private String username;
    private String id;
    private String movieId;
    private String content;
    private int ratingValue;
    private String fileId;
    public ReviewInfo(){
        this.id = UUID.randomUUID().toString();
    }
}
