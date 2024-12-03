package com.aloha.movie_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.movie_project.domain.Movie;
import com.aloha.movie_project.mapper.MovieMapper;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    MovieMapper movieMapper;

    @Override
    public List<Movie> movieList() {
        List<Movie> movieList = movieMapper.movieList();
        return movieList;
    }

    @Override
    public List<Movie> expectList() {
        List<Movie> movieList = movieMapper.expectList();
        return movieList;
    }
    
}
