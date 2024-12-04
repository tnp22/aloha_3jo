package com.aloha.movie_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aloha.movie_project.domain.Movie;
import com.aloha.movie_project.service.MovieService;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@Controller
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/movieChart")
    public String movieChart(Model model
    ,@RequestParam(name = "page", required = false, defaultValue = "1") Integer page
    ,@RequestParam(name = "size", required = false, defaultValue = "8") Integer size
    ,@RequestParam(name = "tab", required = false, defaultValue = "movie") String tab) {

            PageInfo<Movie> moviePageInfo = movieService.movieList(page, size);
            PageInfo<Movie> expectPageInfo = movieService.expectList(page, size);
            model.addAttribute("moviePageInfo", moviePageInfo);
            model.addAttribute("expectPageInfo", expectPageInfo);
        return "/movie/movieChart";
    }
    
}
