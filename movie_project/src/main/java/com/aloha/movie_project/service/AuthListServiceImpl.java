package com.aloha.movie_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.movie_project.domain.AuthList;
import com.aloha.movie_project.mapper.AuthListMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("AuthListService")
public class AuthListServiceImpl implements AuthListService  {

    @Autowired
    private AuthListMapper authListMapper;


    @Override
    public int insert(AuthList authList) throws Exception {
        int result = authListMapper.insert(authList);
        return result;
    }

    @Override
    public List<AuthList> list() throws Exception {
        List<AuthList> list = authListMapper.list();
        return list;
    }

    @Override
    public int delete(int no) throws Exception {
        int result = authListMapper.delete(no);
        return result;
    }
    
}
