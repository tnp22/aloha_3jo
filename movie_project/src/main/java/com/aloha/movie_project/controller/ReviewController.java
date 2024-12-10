package com.aloha.movie_project.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aloha.movie_project.domain.ReviewInfo;
import com.aloha.movie_project.service.ReviewService;
import com.aloha.movie_project.service.UserService;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;




@Controller
@Slf4j
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    UserService userService;
    @Autowired
    ReviewService reviewService;

    @GetMapping("/list")
    public String list(Model model,@RequestParam("id") String id
    ,@RequestParam(name = "page", required = false, defaultValue = "1") Integer page
    ,@RequestParam(name = "size", required = false, defaultValue = "8") Integer size) throws Exception {
        log.info("페이지는 " + page);
        PageInfo<ReviewInfo> reviewList = reviewService.reviewList(id, page, size);
        log.info("리뷰 리스트 생성");
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("movieId", id);
        return "/movie/reviewList";
    }
    

    @ResponseBody
    @PostMapping("/insert")
    public String insertReview(@RequestBody ReviewInfo reviewInfo) throws Exception {
        String userId = userService.select(reviewInfo.getUsername()).getId();
        int result = reviewService.insertReview(reviewInfo.getId(), reviewInfo.getMovieId(), userId, reviewInfo.getContent());
        reviewService.insertRating(UUID.randomUUID().toString(), reviewInfo.getId(), userId, reviewInfo.getRatingValue());
        if(result>0)
            return "SUCCESS";
        return "FAIL";
    }

    @ResponseBody
    @PutMapping("/update")
    public String updateReview(ReviewInfo reviewInfo) {
        log.info("리뷰 업데이트 시도");
        int result = reviewService.updateReview(reviewInfo.getId(), reviewInfo.getContent());
        reviewService.updateRating(reviewInfo.getId(), reviewInfo.getRatingValue());
        if(result>0)
            return "SUCCESS";
        return "FAIL";
    }

    @ResponseBody
    @DeleteMapping("/delete/{id}/{movieId}")
    public String getMethodName(@PathVariable("id") String id, @PathVariable("movieId") String movieId) {
        log.info("리뷰 삭제 시도중");
        log.info("id : " + id);
        int result = reviewService.deleteReview(id);
        reviewService.deleteRating(id);
        if(result>0)
            return "SUCCESS";
        return "Fail";
    }
}
