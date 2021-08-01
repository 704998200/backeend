package com.hwx.backeend.controller.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hwx.backeend.entity.Issue;
import com.hwx.backeend.entity.Project;
import com.hwx.backeend.entity.User;
import com.hwx.backeend.result.JsonResult;
import com.hwx.backeend.service.IssueService;
import com.hwx.backeend.service.MyUserDetailsService;
import com.hwx.backeend.service.UserService;
import com.hwx.backeend.service.api.ProjectService;
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
@RequestMapping("/api/v1/issue")
public class IssueController {
    @Autowired
    UserService userService;

    @Autowired
    IssueService issueService;
    @Autowired
    ProjectService projectService;

    @Autowired
     JwtTokenUtil jwtTokenUtil;

    @Autowired
    MyUserDetailsService userDetailsService;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/new")
    JsonResult newIssue(
            HttpServletRequest request,
            @RequestBody JSONObject jsonParam
    ) {
        Long projectId = jsonParam.getLong("projectId");
        Optional<Project> project = projectService.findById(projectId);
        // 分配给的用户
//        var assignToUserId = jsonParam.getLong("assignToUserId")
//        var assignToUser = userService.findById(assignToUserId)
        // 从 token 获取用户
        String token = jwtTokenUtil.getToken(request.getHeader("Authorization"));
        String username =jwtTokenUtil.getUsernameFromToken(token);
        User postedByUser = userService.findUserByUsername(username);
        String issueTitle = jsonParam.getString("issueTitle");
        String issueContent = jsonParam.getString("issueContent");
        JSONArray assignUsersList = jsonParam.getJSONArray("assignedUser");
//        var status = jsonParam.getIntValue("status")

        Issue issue = new Issue();
        issue.setProject(project.get());
        issue.setPostedBy(postedByUser);
        if (assignUsersList.isEmpty()) {
            // 没有人的时候分配给自己
            issue.getAssignUsers().add(postedByUser);
        } else {
            // 否则 根据列表添加
            for(int i = 0; i < assignUsersList.size(); i++) {
                String assignedUsername = (String) assignUsersList.get(i);
                User user = userService.findUserByUsername(assignedUsername);
                issue.getAssignUsers().add(user);
            }
        }

        issue.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        issue.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
//        issue.status = status
        issue.setIssueTitle(issueTitle);
        issue.setIssueContent(issueContent);
        Object result = issueService.save(issue);
        return new JsonResult(0, result);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/{issueId}/get")
    JsonResult getIssue(@PathVariable String issueId) {
        Object issue = issueService.findById(Long.valueOf(issueId));
        return new JsonResult(0, issue);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/{issueId}/delete")
    JsonResult deleteIssue(@PathVariable String issueId )  {
        Object result = issueService.deleteById(Long.valueOf(issueId));
        return new JsonResult(0, result);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/{issueId}/update")
    JsonResult updateIssue(JSONObject jsonParam, @PathVariable String issueId) {
        Long projectId = jsonParam.getLong("projectId");
        Optional<Project> project = projectService.findById(projectId);
        Long postedByUserId = jsonParam.getLong("postedByUserId");
        User postedByUser = userService.findById(postedByUserId);
        String issueTitle = jsonParam.getString("issueTitle");
        String issueContent = jsonParam.getString("issueContent");
        JSONArray assignUsersIdList = jsonParam.getJSONArray("assigned");
        int status = jsonParam.getIntValue("status");
        Issue issue = issueService.findById(projectId);
        issue.setProject(project.get())  ;
        issue.setPostedBy(postedByUser);
        for(int i = 0; i < assignUsersIdList.size(); i++) {
            JSONObject obj = assignUsersIdList.getJSONObject(i);
            Long userId = Long.valueOf((String) obj.get("userId"));
            User user = userService.findById(userId);
            issue.getAssignUsers().add(user);
        }
        issue.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
        issue.setStatus(status);
        issue.setIssueTitle(issueTitle);
        issue.setIssueContent(issueContent);
        Object result = issueService.save(issue);
        return new JsonResult(0, result);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/{issueId}/comments")
    JsonResult getIssueComments(@PathVariable Long issueId)  {
        Object result = issueService.getIssueComments(issueId);
        return new JsonResult(0, result);
    }
}
