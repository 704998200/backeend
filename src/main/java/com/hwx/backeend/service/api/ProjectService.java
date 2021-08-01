package com.hwx.backeend.service.api;

import com.hwx.backeend.entity.Issue;
import com.hwx.backeend.entity.Project;
import com.hwx.backeend.repository.IssueRepository;
import com.hwx.backeend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public
class ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    IssueRepository issueRepository;

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public Optional<Project> findById(Long projectId) {
        return projectRepository.findById(projectId);
    }

    public int deleteById(Long projectId) {
        try {
            projectRepository.deleteById(projectId);
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public List<Issue> getProjectIssues(Long projectId) {
        return issueRepository.findProjectIssues(projectId);
    }
}
