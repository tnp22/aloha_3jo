package com.aloha.movie_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aloha.movie_project.domain.Comments;
import com.aloha.movie_project.service.CommentService;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Controller
@RequestMapping("/comment")
public class CommentController {
    
    @Autowired
    private CommentService commentService;


    /**
     * 댓글 등록
     * @param comment
     * @return
     * @throws Exception
     */
    @PostMapping("")
    @ResponseBody
    public String commentPost(@RequestBody Comments comment) throws Exception{
        int rs = commentService.insert(comment);
        if(rs > 0 ){
            return "SUCCESS";
        }
        return "FAIL";
    }
    
    /**
     * 댓글 목록조회
     * @param model
     * @param boardNo
     * @return
     * @throws Exception
     */
    @GetMapping("")    
    public String commentList(Model model,@RequestParam("boardNo") int boardNo)throws Exception{
        List<Comments> commentList = commentService.listByParent(boardNo);
        model.addAttribute("commentList",commentList);
        return "/comment/list";
    }

    /**
     * 댓글 삭제 
     * */
    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteComment(@PathVariable("id") String id) throws Exception {
        int rs = commentService.delete(id);

        if(rs>0){
            return "SUCCESS";
        }
        return "FAIL";
    }
    

    /**
     * 댓글 수정
     * */
    @PutMapping("")
    @ResponseBody
    public String updateComment(@RequestBody Comments comment) throws Exception {
        int rs = commentService.update(comment);

        if(rs>0){
            return "SUCCESS";
        }
        return "FAIL";
    }




}
