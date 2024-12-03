package com.aloha.movie_project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.movie_project.domain.Movie;

@Mapper
public interface MovieMapper {

    public List<Movie> movieList();
    public List<Movie> expectList();
}
