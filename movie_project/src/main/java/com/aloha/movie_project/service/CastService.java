package com.aloha.movie_project.service;

import java.util.List;

import com.aloha.movie_project.domain.Cast;

public interface CastService {
    public List<Cast> castList(String id) throws Exception;
}
