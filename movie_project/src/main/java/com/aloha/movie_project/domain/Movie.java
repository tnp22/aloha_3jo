package com.aloha.movie_project.domain;

import java.util.Date;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Movie {
    private int no;
    private String id;
    private String title;
    private String content;
    private String type;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;
    
    private Date regDate;
    private Date updDate;
    private Files files;

    private MultipartFile[] mainFiles;
    private MultipartFile[] stilcuts;

    public Movie(){
        this.id = UUID.randomUUID().toString();
    }
}
