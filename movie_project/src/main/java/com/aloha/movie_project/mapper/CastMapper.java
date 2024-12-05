package com.aloha.movie_project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.movie_project.domain.Cast;

@Mapper
public interface CastMapper {
    public List<Cast> castList(String id) throws Exception;
}
