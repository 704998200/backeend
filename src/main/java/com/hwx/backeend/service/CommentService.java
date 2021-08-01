package com.hwx.backeend.service;

import com.hwx.backeend.entity.Comment;
import com.hwx.backeend.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    public Object save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Optional<Comment> getComment(String commentId){
        return commentRepository.findById(Long.valueOf(commentId));
    }

    public Optional<Comment> findById(Long commentIdLong) {
        return commentRepository.findById(commentIdLong);
    }

    public Integer deleteById(Long id) {
        try {
            commentRepository.deleteById(id);
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }
}
