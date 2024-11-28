package com.aloha.movie_project.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aloha.movie_project.domain.Board;
import com.aloha.movie_project.domain.Files;
import com.aloha.movie_project.domain.Option;
import com.aloha.movie_project.domain.Page;
import com.aloha.movie_project.mapper.BoardMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("BoardService")
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private FileService fileService;
    
    @Override
    public List<Board> list() throws Exception {
        List<Board> list = boardMapper.list(new Page(),new Option());
        return list;
    }

    @Override
    public List<Board> list(Page page,Option option) throws Exception {
        int total = count2();
        page.setTotal(total);
        List<Board> list = boardMapper.list(page,option);
        return list;
    }

    @Override
    public Board select(String id) throws Exception {
        Board board = boardMapper.select(id);
        return board;
    }

    @Override
    public int insert(Board board) throws Exception {
        int result = boardMapper.insert(board);

        List<MultipartFile> fileList = board.getFileList();

        if( fileList != null ) 
            for (MultipartFile file : fileList) {
                if(file != null && file.isEmpty())
                    continue;
                    
                Files uploadFile = new Files();
                uploadFile.setFile(file);
                uploadFile.setParentTable("board");
                uploadFile.setParentNo(board.getNo());
                uploadFile.setType("main");
                fileService.upload(uploadFile);
            }

        return result;
    }

    @Override
    public int update(Board board) throws Exception {
        // 게시글 정보 수정
        int result = boardMapper.update(board);

        // 삭제할 파일 처리
        List<String> deleteFiles = board.getDeleteFiles();
        if( deleteFiles != null && !deleteFiles.isEmpty() )
            for (String fileId : deleteFiles) {
                log.info("fileId : " + fileId);
                fileService.delete(fileId); // 파일 삭제 요청
            }

        return result;
    }

    @Override
    public int delete(String id) throws Exception {
        Board board = boardMapper.select(id);

        // 게시글 삭제
        int result = boardMapper.delete(id);

        // 첨부파일 종속 삭제
        Files deleteFile = new Files();
        deleteFile.setParentTable("board");
        deleteFile.setParentNo(board.getNo());
        int fileResult = fileService.deleteByParent(deleteFile);
        log.info("fileResult : " + fileResult);
        return result;
    }

    @Override
    public List<Board> search(String search,int searchCode,Page page,Option option) throws Exception {
        int total = count(search, searchCode, option);
        page.setTotal(total);
        List<Board> searchList = boardMapper.search(search,searchCode,page,option);
        return searchList;
    }

    @Override
    public int count(String search, int searchCode, Option option) throws Exception {
        return boardMapper.count(search, searchCode, option);
    }

    @Override
    public int count2() throws Exception {
        return boardMapper.count2();
    }

    @Override
    public boolean isOwner(String id, Long no) throws Exception {
        Board board = boardMapper.select(id);
        return board.getUserNo() == no ;
    }
}
