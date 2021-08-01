package com.hwx.backeend.repository;

import com.hwx.backeend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c where c.issue = (select i from Issue i where i.id = :issueId)")
    ArrayList<Comment> findIssueComments(Long issueId);
}
