package com.aloha.movie_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aloha.movie_project.domain.Movie;
import com.aloha.movie_project.service.MovieService;

import groovy.util.logging.Slf4j;

@Controller
@Slf4j
@RequestMapping("/m")
public class ticketController {
    @Autowired
    private MovieService movieService;
    @GetMapping("/t")
    public String ticketMain(Model model, @RequestParam("id") String id) throws Exception {
        Movie movie = movieService.movieInfo(id);
        model.addAttribute("movie", movie);
        return "/movie/ticket";
    }

}
