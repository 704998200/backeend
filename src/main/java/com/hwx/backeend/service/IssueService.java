package com.hwx.backeend.service;

import com.hwx.backeend.entity.Comment;
import com.hwx.backeend.entity.Issue;
import com.hwx.backeend.repository.CommentRepository;
import com.hwx.backeend.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class IssueService {
    @Autowired
    IssueRepository issueRepository;

    @Autowired
    CommentRepository commentRepository;

    public Integer save(Issue issue) {
        try {
            issueRepository.save(issue);
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    public Issue findById(Long id) {
        return issueRepository.findById(id).get();
    }

    public Integer deleteById(Long id) {
        try {
            issueRepository.deleteById(id);
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    public List<Comment> getIssueComments(Long issueId) {
        return commentRepository.findIssueComments(issueId);
    }
}
