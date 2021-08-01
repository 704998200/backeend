package com.hwx.backeend.controller.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hwx.backeend.entity.Issue;
import com.hwx.backeend.entity.User;
import com.hwx.backeend.result.JsonResult;
import com.hwx.backeend.service.UserService;
import com.hwx.backeend.service.api.DashboardService;
import com.hwx.backeend.service.api.ProjectService;
import com.hwx.backeend.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    @Autowired
    UserService userService;

    @Autowired
    DashboardService dashboardService;

    @Autowired
    ProjectService projectService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    /**
     * 获取分配给用户的 issue 列表
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/assigned")
    JsonResult getAssignedToMe(HttpServletRequest request, HttpServletResponse resp ){
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userService.findUserByUsername(username);
        List<Issue> issues = dashboardService.getAssignToMeIssues(user);
        JSONArray json = new JSONArray();
        for(Issue issue:issues) {
            JSONObject issueItem = new JSONObject();
            issueItem.put("title",issue.getIssueTitle());
            issueItem.put("id",issue.getId());
            json.add(issueItem);
        }
        return new JsonResult(0, json);
    }

    /**
     * 获取用户打开的的 issue 列表
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/opened")
    JsonResult getOpenByMe(HttpServletRequest request,HttpServletResponse resp) {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userService.findUserByUsername(username);
        List<Issue> issues = dashboardService.getOpenByMeIssues(user);
        JSONArray json = new JSONArray();
        for(Issue issue:issues) {
            JSONObject issueItem = new JSONObject();
            issueItem.put("title",issue.getIssueTitle());
            issueItem.put("id",issue.getId());
            json.add(issueItem);
        }
        return new JsonResult(0, json);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/counts")
    JsonResult getIssueCount() {
        Long totalIssueCount = dashboardService.getTotalIssueCount();
        Long openedIssueCount = dashboardService.getOpenedIssueCount();
        Long closedIssueCount = dashboardService.getClosedIssueCount();
        JSONObject json = new JSONObject();
        json.put("totalIssue",totalIssueCount);
        json.put("closedIssue",closedIssueCount);
        json.put("openedIssue",openedIssueCount);
        return new JsonResult(0, json);
    }
}
