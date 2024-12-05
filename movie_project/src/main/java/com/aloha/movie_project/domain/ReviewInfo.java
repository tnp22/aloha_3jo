package com.aloha.movie_project.domain;

import lombok.Data;

@Data
public class ReviewInfo {
    private String username;
    private String id;
    private String content;
    private int ratingValue;
}
