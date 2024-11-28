package com.aloha.movie_project.service;

import java.util.List;

import com.aloha.movie_project.domain.Board;
import com.aloha.movie_project.domain.Option;
import com.aloha.movie_project.domain.Page;

public interface BoardService {

    public List<Board> list() throws Exception;

    public List<Board> list( Page page,Option option) throws Exception;

    public Board select(String id) throws Exception;

    public int insert(Board board) throws Exception;

    public int update(Board board) throws Exception;

    public int delete(String id) throws Exception;
    
    public List<Board> search(String search
    , int searchCode, Page page,Option option) throws Exception;

    public int count(String search
    ,int searchCode ,Option option) throws Exception;

    public int count2() throws Exception;

    public boolean isOwner(String id,Long no) throws Exception;
}
