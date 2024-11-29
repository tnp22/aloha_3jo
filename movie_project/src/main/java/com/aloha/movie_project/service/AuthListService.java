package com.aloha.movie_project.service;

import java.util.List;

import com.aloha.movie_project.domain.AuthList;

public interface AuthListService {

   // 권한 조회
    public List<AuthList> list() throws Exception;

    // 권한 생성
    public int insert(AuthList authList) throws Exception;
}
