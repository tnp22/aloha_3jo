package com.aloha.movie_project.service;

import java.util.List;

import com.aloha.movie_project.domain.Movie;
import com.github.pagehelper.PageInfo;

public interface MovieService {
    public List<Movie> movieList();
    public List<Movie> expectList();

    // 조회
    public Movie select(String id) throws Exception;
    // 생성
    public int insert(Movie movie) throws Exception;
    // 수정
    public int update(Movie movie) throws Exception;
    
    //페이징 목록
    public PageInfo<Movie> list(int page, int size) throws Exception;
    // 검색까지
    public PageInfo<Movie> list(int page, int size,String search) throws Exception;
    // 권한 조회
    public List<Movie> list() throws Exception;
}
