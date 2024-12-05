package com.aloha.movie_project.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Theater {
    private int no;
    private String id;
    private String name;
    private String cinemaId;
    private String map;


    //애드맵

    private int x;
    private int y;

    private int mapSize;

    private List<List<String>> mapData;
}
