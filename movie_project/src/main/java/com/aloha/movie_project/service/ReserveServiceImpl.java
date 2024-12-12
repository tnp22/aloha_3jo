package com.aloha.movie_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.movie_project.domain.Movie;
import com.aloha.movie_project.domain.Reserve;
import com.aloha.movie_project.mapper.ReserveMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ReserveServiceImpl implements ReserveService {

    @Autowired
    ReserveMapper reserveMapper;

    @Override
    public int insertReserve(Reserve reserve) {
        int reuslt = reserveMapper.insertReserve(reserve);
        return reuslt;
    }

    @Override
    public List<Reserve> selectSeat(String theaterListId) {
        return reserveMapper.selectSeat(theaterListId);
    }

    @Override
    public List<Reserve> selectUsername(String userName) {
        return reserveMapper.selectUsername(userName);
    }

    @Override
    public PageInfo<Reserve> selectUsername(int page, int size, String userName) throws Exception {
        // ⭐ PageHelper.startPage(현재 페이지, 페이지당 게시글 수);
        PageHelper.startPage(page, size);
        List<Reserve> movieList = reserveMapper.selectUsername(userName);

        // ⭐ PageInfo( 리스트, 노출 페이지 개수 )
        PageInfo<Reserve> pageInfo = new PageInfo<Reserve>(movieList, 10);
        return pageInfo;
    }

    @Override
    public Reserve searchReserve(String id) {
        return reserveMapper.searchReserve(id);
    }

}
