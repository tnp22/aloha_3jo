package com.aloha.movie_project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.movie_project.domain.Notice;

@Mapper
public interface NoticeMapper {
    public List<Notice> list();
}
