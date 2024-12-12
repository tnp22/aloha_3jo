package com.aloha.movie_project.service;

import java.util.List;

import com.aloha.movie_project.domain.Reserve;

public interface ReserveService {
    // 예매등록
    public int insertReserve(Reserve reserve);

    // 좌석검색
    public List<Reserve> selectSeat(String theaterListId);

    // 예매 리스트
    public List<Reserve> selectUsername(String userName);
}
