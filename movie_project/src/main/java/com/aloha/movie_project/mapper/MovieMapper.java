package com.aloha.movie_project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.movie_project.domain.Files;
import com.aloha.movie_project.domain.Movie;
import com.aloha.movie_project.domain.ReviewInfo;

@Mapper
public interface MovieMapper {

    public List<Movie> movieList();
    public List<Movie> expectList();

    // 조회
    public Movie select(String id) throws Exception;
    // 생성
    public int insert(Movie movie) throws Exception;
    // 수정
    public int update(Movie movie) throws Exception;
    // 삭제

    // 권한 조회
    public List<Movie> list() throws Exception;

    // 권한 검색 조회
    public List<Movie> search(String search) throws Exception;

    // 영화 상세정보 스틸컷
    public List<Files> stilList(String id) throws Exception;

    // 영화 상세정보 리뷰
    public List<ReviewInfo> reviewList(String id) throws Exception;

    // 영화 상세정보 조회
    public Movie movieInfo(String id) throws Exception;
}
