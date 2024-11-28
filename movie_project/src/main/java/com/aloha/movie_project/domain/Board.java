package com.aloha.movie_project.domain;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Board {

    private int no;
    private String id;
    private String title;
    private Long userNo;
    private String writer;
    private String content;
    private Date createdAt;
    private Date updatedAt;

    private List<MultipartFile> fileList;       // 파일 목록
    private List<String> deleteFiles;           // 삭제할 파일 ID 목록

    //private String search;

    public Board() {
        this.id = UUID.randomUUID().toString();
    }
    
}
