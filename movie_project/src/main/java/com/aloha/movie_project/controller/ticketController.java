package com.aloha.movie_project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aloha.movie_project.domain.Movie;
import com.aloha.movie_project.service.MovieService;
import org.springframework.web.bind.annotation.RequestParam;

import com.aloha.movie_project.domain.Cinema;
import com.aloha.movie_project.domain.Movie;
import com.aloha.movie_project.domain.Theater;
import com.aloha.movie_project.domain.TheaterList;
import com.aloha.movie_project.domain.TicketList;
import com.aloha.movie_project.service.cinema.TheaterListService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/m")
public class ticketController {

    @Autowired
    private TheaterListService theaterListService;
    @Autowired
    private MovieService movieService;
    @GetMapping("/t")
    public String ticketMain(@RequestParam("id") String id, Model model) throws Exception {

        id = "6e937900-b05b-11ef-b8e4-4ccc6ad7549d"; // 무비 ID
        List<TheaterList> list = theaterListService.timeSearch(id);

        List<TicketList> ticketLists = new ArrayList<>();

        // 극장 구분 저장 리스트
        TreeSet<String> area = new TreeSet<>();

        for (TheaterList t : list) {
            TicketList ticket = new TicketList();
            Cinema cinema = t.getCinema();
            Movie movie = t.getMovie();
            Theater theater = t.getTheater();

            ticket.setArea(cinema.getArea()); // 지역
            ticket.setAreaSub(cinema.getAreaSub()); // 극장

            ticket.setTime(t.getTime()); // 상영날짜 + 시간
            ticket.setId(t.getId()); // 상영리스트 ID

            ticket.setTitle(movie.getTitle()); // 영화 제목

            ticket.setTheaterName(theater.getName()); // 상영관이름
            ticket.setMapUrl(theater.getMap()); // 상영관맵경로

            ticket.setMovieId(t.getMovieId()); // 무비 ID
            ticket.setTheaterId(t.getTheaterId()); // 시에터 ID
            ticket.setCinemaId(t.getCinemaId()); // 시네마 ID

            ticketLists.add(ticket); // List 업로드

        }

        log.info("리스트 : " + ticketLists);
        model.addAttribute("ticketList", ticketLists);
        return "/movie/ticket";

    }

}
