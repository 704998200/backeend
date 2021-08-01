package com.hwx.backeend.repository;


import com.hwx.backeend.entity.Comment;
import com.hwx.backeend.entity.Issue;
import com.hwx.backeend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
//    @Query("select i from Issue i where :user in elements(i.assignUsers)")
//    ArrayList<Issue> findByAssignUsers(User user);
    @Query("select i from Issue i where (:user in elements(i.assignUsers))")
    List<Issue> findByAssignUsers(User user);
    @Query("SELECT i FROM Issue i WHERE i.project = (SELECT p FROM Project p WHERE p.id = :projectId)")
    ArrayList<Issue> findProjectIssues(Long projectId);

    @Query("select count(e) from Issue e")
    Long getIssueCount();

    @Query("select count(i) from Issue i where i.status = 1")
    Long getOpenedIssueCount();

    @Query("select count(i) from Issue i where i.status = 0")
    Long getClosedIssueCount();

    ArrayList<Issue> findByPostedBy(User user);
}
