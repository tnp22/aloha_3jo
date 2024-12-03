package com.aloha.movie_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.movie_project.domain.Notice;
import com.aloha.movie_project.mapper.NoticeMapper;

@Service
public class NoticeServiceImpl implements NoticeService{

    @Autowired
    NoticeMapper noticeMapper;

    @Override
    public List<Notice> list() {
       return noticeMapper.list();
    }
    
}
