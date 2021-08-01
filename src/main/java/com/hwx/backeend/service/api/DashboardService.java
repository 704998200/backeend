package com.hwx.backeend.service.api;


import com.hwx.backeend.entity.Issue;
import com.hwx.backeend.entity.User;
import com.hwx.backeend.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public
class DashboardService {

    @Autowired
    IssueRepository issueRepository;

    public List<Issue> getAssignToMeIssues(User user) {
        return issueRepository.findByAssignUsers(user);
    }

    void getLastUpdated(String username) {

    }

    public Long getTotalIssueCount() {
        return issueRepository.getIssueCount();
    }

    public Long getOpenedIssueCount() {
        return issueRepository.getOpenedIssueCount();
    }

    public Long getClosedIssueCount(){
        return issueRepository.getClosedIssueCount();
    }

    public List<Issue> getOpenByMeIssues(User user) {
        return issueRepository.findByPostedBy(user);
    }
}
