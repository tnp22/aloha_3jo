package com.aloha.movie_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.movie_project.domain.Comments;
import com.aloha.movie_project.mapper.CommentMapper;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comments> list() throws Exception {
        List<Comments> commentList = commentMapper.list();
        return commentList;
    }

    @Override
    public Comments select(String id) throws Exception {
        Comments comment = commentMapper.select(id);
        return comment;
    }

    @Override
    public int insert(Comments comment) throws Exception {
        //답글 등록
        int rs = commentMapper.insert(comment);

        //댓글 등록
        // : 댓글 번호를 부모 댓글 번호로 수정(no = parent_no)
        int parentNo = comment.getParentNo();
        if(parentNo == 0){
            comment.setParentNo(comment.getNo());
            commentMapper.update(comment);
        }
        return rs;
    }

    @Override
    public int update(Comments comment) throws Exception {
        String id = comment.getId();
        String updatedWriter = comment.getWriter();
        String updatedContent = comment.getContent();
        comment = commentMapper.select(id);     //parentNo 유지
        comment.setWriter(updatedWriter);
        comment.setContent(updatedContent);
        int rs = commentMapper.update(comment);

        return rs;
    }

    @Override
    public int delete(String id) throws Exception {
        int rs = 0;
        Comments deletedComment = commentMapper.select(id);
        int parentNo = deletedComment.getParentNo();
        int no = deletedComment.getNo();

        if(parentNo == no){
            rs = commentMapper.deleteChild(parentNo);
        }
        else{
            rs = commentMapper.delete(id);
        }
        return rs;
    }

    @Override
    public List<Comments> listByParent(int boardNo) throws Exception {
        List<Comments> commentList = commentMapper.listByParent(boardNo);
        return commentList;
    }

    @Override
    public int deleteByParent(int boardNo) throws Exception {
        int rs = commentMapper.deleteByParent(boardNo);
        return rs;
    }

    @Override
    public List<Comments> replyList(int parentNo) throws Exception {
        List<Comments> replyList = commentMapper.replyList(parentNo);
        return replyList;
    }

    @Override
    public int deleteChild(int parentNo) throws Exception {
        int rs = commentMapper.deleteChild(parentNo);
        return rs;
    }
    
}
