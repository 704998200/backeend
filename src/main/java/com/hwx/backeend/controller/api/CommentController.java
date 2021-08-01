package com.hwx.backeend.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.hwx.backeend.entity.Comment;
import com.hwx.backeend.entity.Issue;
import com.hwx.backeend.entity.User;
import com.hwx.backeend.result.JsonResult;
import com.hwx.backeend.service.CommentService;
import com.hwx.backeend.service.IssueService;
import com.hwx.backeend.service.UserService;
import com.hwx.backeend.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Optional;

@RestController
public class CommentController {
    @Autowired
    UserService userService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    CommentService commentService;

    @Autowired
    IssueService issueService;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/new")
    JsonResult newComment(
            HttpServletRequest request,
            @RequestBody JSONObject jsonParam
    ) {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userService.findUserByUsername(username);
        String commentContent = jsonParam.getString("commentContent");
        Long issueId = jsonParam.getLong("issueId");
        Issue issue = issueService.findById(issueId);
        Comment comment = new Comment();
        comment.setPostedBy(user);
        comment.setIssue(issue);
        comment.setCommentContent(commentContent);
        comment.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        comment.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
        Object result = commentService.save(comment);
        return new JsonResult(0, result);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/{commentId}/get")
    JsonResult getComment(
            @RequestBody JSONObject jsonParam,
            @PathVariable String commentId
    ) {
        Optional commentContainer = commentService.getComment(commentId);
        if (commentContainer.isPresent()) {
            return new JsonResult(0, commentContainer.get());
        } else {
            return new JsonResult(114514, "");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/{commentId}/update")
    JsonResult updateComment(
            @PathVariable String commentId,
            @RequestBody JSONObject jsonParam
    ) {
        Long commentIdLong = Long.valueOf(commentId);
        String commentContent = jsonParam.getString("commentContent");
        Optional<Comment> commentContainer = commentService.findById(commentIdLong);
        if (commentContainer.isPresent()) {
            Comment comment = commentContainer.get();
            comment.setCommentContent(commentContent);
            comment.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            Object result = commentService.save(comment);
            return new JsonResult(0, result);
        } else{
            return new JsonResult(114514, "");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/{commentId}/delete")
    JsonResult deleteComment(
            @PathVariable String commentId
    )

     {
        Long commentIdLong = Long.valueOf(commentId);
        Object result = commentService.deleteById(commentIdLong);
        return new JsonResult(0, result);
    }
}
