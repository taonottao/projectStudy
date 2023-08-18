package com.example.demo.controller;

import com.example.demo.common.AjaxResult;
import com.example.demo.common.AppVariable;
import com.example.demo.common.Param;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Userinfo;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/16 16:05
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping("/add")
    public AjaxResult add(Comment comment, HttpSession session) {
        Userinfo userinfo = (Userinfo) session.getAttribute(AppVariable.USER_SESSION_KEY);
        if (Param.checkParamNull(comment, comment.getContent(), userinfo.getUsername())) {
            return AjaxResult.fail(-1, "参数非法！");
        }
        comment.setUsername(userinfo.getUsername());
        return AjaxResult.success(commentService.add(comment));
    }

    @RequestMapping("/getlist")
    public AjaxResult selectAllByPid(HttpSession session, Integer pid) {

        List<Comment> comments = commentService.selectAllByPid(pid);
        return AjaxResult.success(comments);
    }

    @RequestMapping("/getall")
    public AjaxResult getAllComments() {
        List<Comment> list = commentService.getAllComments();
        return AjaxResult.success(list);
    }

    @RequestMapping("/delete")
    public AjaxResult deleteComment(Integer id) {
        Comment comment = commentService.selectOneById(id);
        if (Param.checkParamNull(comment)) {
            return AjaxResult.fail(-1, "改评论不存在！");
        }
        return AjaxResult.success(commentService.deleteComment(id));
    }
}
