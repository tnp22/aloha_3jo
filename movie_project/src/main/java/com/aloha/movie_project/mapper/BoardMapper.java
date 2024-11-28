package com.aloha.movie_project.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aloha.movie_project.domain.Board;
import com.aloha.movie_project.domain.Option;
import com.aloha.movie_project.domain.Page;

@Mapper
public interface BoardMapper {
    
    //public List<Board> list(@Param("rows") int rows, @Param("option") Option option) throws Exception;
    
     // 전체 목록
    public List<Board> list() throws Exception;

    public List<Board> list(@Param("page") Page page, @Param("option") Option option) throws Exception;

    public Board select(@Param("id") String id) throws Exception;

    public int insert(Board board) throws Exception;

    public int update(Board board) throws Exception;

    public int delete(String id) throws Exception;

    public List<Board> search(@Param("search") String search
    ,@Param("searchCode") int searchCode,@Param("page") Page page
    ,@Param("option") Option option) throws Exception;

    public int count(@Param("search") String search
    ,@Param("searchCode") int searchCode
    ,@Param("option") Option option) throws Exception;

    public int count2() throws Exception;
}
