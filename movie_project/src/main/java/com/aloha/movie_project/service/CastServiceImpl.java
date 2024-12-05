package com.aloha.movie_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.movie_project.domain.Cast;
import com.aloha.movie_project.mapper.CastMapper;

@Service
public class CastServiceImpl implements CastService {

    @Autowired
    private CastMapper castMapper;

    @Override
    public List<Cast> castList(String id) throws Exception {
        List<Cast> castList = castMapper.castList(id);
        return castList;
    }
    
}
