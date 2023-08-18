package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/16 16:00
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    public int add(Comment comment) {
        return commentMapper.add(comment);
    }

    public List<Comment> selectAllByPid(Integer pid) {
        return commentMapper.selectAllByPid(pid);
    }

    public List<Comment> getAllComments() {
        return commentMapper.getAllComments();
    }

    public int deleteComment(Integer id) {
        return commentMapper.deleteComment(id);
    }

    public Comment selectOneById(Integer id) {
        return commentMapper.selectOneById(id);
    }

}
