package com.aloha.movie_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.aloha.movie_project.domain.Board;
import com.aloha.movie_project.domain.Comments;
import com.aloha.movie_project.domain.CustomUser;
import com.aloha.movie_project.domain.Files;
import com.aloha.movie_project.domain.Option;
import com.aloha.movie_project.domain.Page;
import com.aloha.movie_project.service.BoardService;
import com.aloha.movie_project.service.CommentService;
import com.aloha.movie_project.service.FileService;

import lombok.extern.slf4j.Slf4j;



/**
 * 목록         /board/list     [GET]
 * 조회         /board/select   [GET]
 * 등록         /board/insert   [GET]
 * 등록 처리    /board/insert   [POST]
 * 수정         /board/update   [GET]
 * 수정 처리    /board/update   [POST]
 * 삭제 처리    /board/delete   [POST]
*/
@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private FileService fileService;

    @Autowired
    private CommentService commentService;
    
    /**
     * 목록
     * @return
     * @throws Exception 
    */
    // @GetMapping("/list")
    // public String list(Model model) throws Exception {
    //     List<Board> boardList = boardService.list();
    //     model.addAttribute("boardList", boardList);
    //     return "/board/list";
    // }

    /**
     * 검색
     * @return
     * @throws Exception 
    */
    @GetMapping("/list")
    public String search(Model model,
        @RequestParam(name="search", required = false) String search,
        @RequestParam(name="searchCode", required = false, defaultValue = "-1") Integer searchCode
        ,@RequestParam(name="rows", defaultValue = "10") int rows
        ,Page page
        , Option option) throws Exception {
        List<Board> boardList = null;
        if(search==null){
            page.setRows(rows);
            boardList = boardService.list(page,option);
            model.addAttribute("page", page);
        }
        else{
           // page.setPage(boardService.count(search, searchCode, option));
            boardList = boardService.search(search,searchCode,page,option);
            model.addAttribute("page", page);
            model.addAttribute("search", search);
            model.addAttribute("searchCode", searchCode);
        }
        model.addAttribute("rows", page.getRows());
        model.addAttribute("option", option);
        model.addAttribute("boardList", boardList);
        
        String pageUrl = UriComponentsBuilder.fromPath("/board/list")
                            //.queryParam("page",page)
                            .queryParam("search",search)
                            .queryParam("searchCode",searchCode)
                            .queryParam("rows",page.getRows())
                            .queryParam("option",option.getOrderCode())
                            .toUriString();
        model.addAttribute("pageUrl", pageUrl);
        
        return "/board/list";
    }
    
    /**
     * 조회
     * @param id
     * @return
     * @throws Exception 
    */
    @GetMapping("/select")
    public String select(Model model
                      , @RequestParam("id") String id
                      , Files file) throws Exception {
        
        // 게시글 조회
        Board board = boardService.select(id);
        model.addAttribute("board", board);


        // 파일 목록 조회
        file.setParentNo(board.getNo());
        file.setParentTable("board");

        log.info("file : " + file);
        List<Files> fileList = fileService.listByParent(file);
        model.addAttribute("fileList", fileList);

        List<Comments> commentList = commentService.listByParent(board.getNo());
        model.addAttribute("commentList", commentList);

        return "/board/select";
    }


    /**
     * 등록
     * @return
     */
    @GetMapping("/insert")
    @Secured("ROLE_USER")
    public String insert(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        model.addAttribute("username", username);
        // model.addAttribute("userNo", customUser.getUser().getNo());
        return "/board/insert";
    }
    
    /**
     * 등록 처리
     * @param board
     * @return
     * @throws Exception 
    */
    @PostMapping("/insert")
    @Secured("ROLE_USER")
    public String insertPost(Board board) throws Exception {
        log.info("board : " + board);
        int result = boardService.insert(board);
        if( result > 0 ) {
            return "redirect:/board/list";
        }
        return "redirect:/board/insert?error";
    }
    
    /**
     * 수정
     * @param id
     * @return
     * @throws Exception 
    */
    @GetMapping("/update")
    @PreAuthorize("(hasRole('ADMIN')) or (#p1 != null and @BoardService.isOwner(#p1,authentication.principal.user.no))")
    public String update(Model model
                      , @RequestParam("id") String id
                      , Files file) throws Exception {
        // 게시글 조회
        Board board = boardService.select(id);
        model.addAttribute("board", board);

        // 파일 목록 조회
        file.setParentNo(board.getNo());
        file.setParentTable("board");

        log.info("file : " + file);
        List<Files> fileList = fileService.listByParent(file);
        model.addAttribute("fileList", fileList);

        return "/board/update";
    }
    
    /**
     * 수정 처리
     * @param board
     * @return
     * @throws Exception 
    */
    // 👩‍💼 작성자 본인 👩‍🔧 관리자
	
    @PostMapping("/update")
    @PreAuthorize("(hasRole('ADMIN')) or (#p1 != null and @BoardService.isOwner(#p1,authentication.principal.user.no))")
    public String updatePost(Board board) throws Exception {
        int result = boardService.update(board);
        if( result > 0 ) {
            return "redirect:/board/list";
        }
        return "redirect:/board/update?error&id="+board.getId();
    }
    
    // 삭제 처리
    @PostMapping("/delete")
    @PreAuthorize("(hasRole('ADMIN')) or (#p1 != null and @BoardService.isOwner(#p1,authentication.principal.user.no))")
    public String delete(@RequestParam("id") String id) throws Exception {
        int result = boardService.delete(id);
        if( result > 0 ) 
            return "redirect:/board/list";
        return "redirect:/board/update?error&id="+id;
    }
    
}
