package com.aloha.movie_project.service;

import java.util.List;

import com.aloha.movie_project.domain.Movie;
import com.github.pagehelper.PageInfo;

public interface MovieService {
    public List<Movie> movieList();
    public PageInfo<Movie> movieList(int page, int size);
    public List<Movie> expectList();
    public PageInfo<Movie> expectList(int page, int size);
}
