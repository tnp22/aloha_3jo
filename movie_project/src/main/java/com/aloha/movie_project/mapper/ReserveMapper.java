package com.aloha.movie_project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.movie_project.domain.Reserve;

@Mapper
public interface ReserveMapper {

    // 예매등록
    public int insertReserve(Reserve reserve);

    // 좌석검색
    public List<Reserve> selectSeat(String theaterListId);

    // 예매 리스트
    public List<Reserve> selectUsername(String userName);

    // 예매 id 로 조회
    public Reserve searchReserve(String id);
    
    // 예매 수 계산
    public int selectReservationCountByUsername(String username);

}
