package com.aloha.movie_project.service;

import java.util.List;

import com.aloha.movie_project.domain.ReviewInfo;
import com.github.pagehelper.PageInfo;

public interface ReviewService {
    public int insertReview(String id, String movieId, String userId, String content);    
    public int insertRating(String id, String reviewId, String userId, int ratingValue);
    public int updateReview( String id, String content);
    public int updateRating( String id, int ratingValue);
    public int deleteReview( String id);
    public int deleteRating( String id);
    public PageInfo<ReviewInfo> reviewList(String id, int page, int size) throws Exception;
}
