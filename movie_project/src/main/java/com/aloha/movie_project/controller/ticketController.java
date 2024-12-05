package com.aloha.movie_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import groovy.util.logging.Slf4j;

@Controller
@Slf4j
@RequestMapping("/m")
public class ticketController {

    @GetMapping("/t")
    public String ticketMain() {
        return "/movie/ticket";
    }

}
