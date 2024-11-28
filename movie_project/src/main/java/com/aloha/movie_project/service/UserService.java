package com.aloha.movie_project.service;

import com.aloha.movie_project.domain.UserAuth;
import com.aloha.movie_project.domain.Users;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    
    public boolean login(Users user, HttpServletRequest request) throws Exception;
    // 조회
    public Users select(String username) throws Exception;

    // 회원 가입
    public int join(Users user) throws Exception;

    // 회원 수정
    public int update(Users user) throws Exception;

    // 회원 권한 등록
    public int insertAuth(UserAuth userAuth) throws Exception;

}
