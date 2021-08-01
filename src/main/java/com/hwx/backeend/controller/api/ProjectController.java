package com.hwx.backeend.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.hwx.backeend.entity.Issue;
import com.hwx.backeend.entity.Project;
import com.hwx.backeend.entity.User;
import com.hwx.backeend.result.JsonResult;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {
    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    /**
     * 发起新的 project
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/new")
    JsonResult newProject(
            HttpServletRequest request ,
            @RequestBody JSONObject jsonParam
    )  {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userService.findUserByUsername(username);
        String projectName = jsonParam.getString("name");
        String projectShortId = jsonParam.getString("shortId");
        String projectDescription = jsonParam.getString("description");
        Long projectStartDate = jsonParam.getLong("startDate");
        Long projectFinishDate = jsonParam.getLong("finishDate");
        Project project = new Project();
//        if (jsonParam.containsKey("id")) {
//            // 只有给出了 id 才会写入 id
//            project.id = jsonParam.getLong("id")
//        }
        project.setPostedBy(user);
        project.setProjectName(projectName);
        project.setProjectDescription(projectDescription) ;
        project.setProjectShortId(projectShortId);
        project.setStartTime(new Timestamp(projectStartDate));
        project.setFinishTime(new Timestamp(projectFinishDate));
        project.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        Object result = projectService.save(project);
        // 填充返回
        JSONObject json = new JSONObject();
        json.put("project",project);
        return new JsonResult(0, json);
    }

    /**
     * 更新现有的 Project
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/{projectId}/update")
    JsonResult updateProject(
            HttpServletRequest request,
            @RequestBody JSONObject jsonParam ,
            @PathVariable("projectId") Long projectId
    ) {
        String projectName = jsonParam.getString("name");
        String projectShortId = jsonParam.getString("shortId");
        String projectDescription = jsonParam.getString("description");
        Long projectStartDate = jsonParam.getLong("startDate");
        Long projectFinishDate = jsonParam.getLong("finishDate");
        Optional<Project> projectOptional = projectService.findById(projectId);
        Project project = projectOptional.get();

        // 信息更新
        project.setId(projectId);
        project.setProjectName(projectName) ;
        project.setProjectDescription(projectDescription) ;
        project.setProjectShortId(projectShortId) ;
        project.setUpdatedTime(new Timestamp(System.currentTimeMillis())) ;
        project.setStartTime(new Timestamp(projectStartDate)) ;
        project.setFinishTime(new Timestamp(projectFinishDate)) ;
        Object result = projectService.save(project);
        // 填充返回
        JSONObject json = new JSONObject();
        json.put("project",project);
        return new JsonResult(0, json);
    }

    /**
     * 获取 Project
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/{projectId}/get")
    JsonResult getProject(
            @PathVariable("projectId") Long projectId
    ) {

        Optional<Project> result = projectService.findById(projectId);
        // 填充返回
        JSONObject json = new JSONObject();
        json.put("project",result.get());
        return new JsonResult(0, result.get());
    }

    /**
     * 获取 所有Project
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/getAll")
    JsonResult getAllProject(
            HttpServletRequest request
            ) {
        List<Project> result = projectService.findAll();

        return new JsonResult(0, result);
    }

    /**
     *
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/{projectId}/delete")
    JsonResult deleteProject(

            @PathVariable("projectId") Long projectId
    ) {
//        var projectId = jsonParam.getLong("id")
        int result = projectService.deleteById(projectId);

        return new JsonResult(0, result);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/{projectId}/issues")
    JsonResult getProjectIssue(@PathVariable  Long projectId)  {
        List<Issue> issues = projectService.getProjectIssues(projectId);
        return new JsonResult(0, issues);
    }
}
