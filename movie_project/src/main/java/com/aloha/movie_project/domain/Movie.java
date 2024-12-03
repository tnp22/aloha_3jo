package com.aloha.movie_project.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Movie {
    int no;
    String id;
    String title;
    String content;
    String type;
    Date releaseDate;
    Date regDate;
    Date updDate;
    Files files;
}
