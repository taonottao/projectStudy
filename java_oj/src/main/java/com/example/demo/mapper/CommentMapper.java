package com.example.demo.mapper;

import com.example.demo.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/16 15:50
 */
@Mapper
public interface CommentMapper {
    int add(Comment comment);
    List<Comment> selectAllByPid(Integer pid);
    List<Comment> getAllComments();
    int deleteComment(Integer id);

    Comment selectOneById(Integer id);
}
