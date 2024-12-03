package com.aloha.movie_project.service;

import java.util.List;

import com.aloha.movie_project.domain.Movie;

public interface MovieService {
    public List<Movie> movieList();
    public List<Movie> expectList();
}
