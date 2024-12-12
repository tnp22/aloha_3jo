package com.aloha.movie_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.movie_project.domain.Reserve;
import com.aloha.movie_project.mapper.ReserveMapper;

@Service
public class ReserveServiceImpl implements ReserveService {

    @Autowired
    ReserveMapper reserveMapper;

    @Override
    public int insertReserve(Reserve reserve) {
        return reserveMapper.insertReserve(reserve);
    }

    @Override
    public List<Reserve> selectSeat(String theaterListId) {
        return reserveMapper.selectSeat(theaterListId);
    }

    @Override
    public List<Reserve> selectUsername(String userName) {
        return reserveMapper.selectUsername(userName);
    }

}
