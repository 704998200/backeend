package com.hwx.backeend.entity;


import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;
import java.io.Serializable;

/**
 * (Comments)实体类
 *
 * @author makejava
 * @since 2021-07-26 13:23:23
 */
@Entity
@Table(name = "comments")
public class Comment implements Serializable {
    private static final long serialVersionUID = -63677814915459422L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 内容
    @Column(name = "comment_content", nullable = true)
    private String commentContent;

    public User getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(
            name = "posted_by_user_id",
            referencedColumnName = "id"
    )
    User postedBy ;

    @ManyToOne(fetch = FetchType.LAZY, cascade =CascadeType.MERGE)
    @JoinTable(
            name = "issue_comment",
            joinColumns = {@JoinColumn(
                    name = "comment_id",
                    referencedColumnName = "id"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "issue_id",
                    referencedColumnName = "id"
            )}
    )
    @JSONField(serialize = false)
    Issue issue ;


    @Column(name = "created_at", nullable = true)
    private Date createdTime;
    @Column(name = "updated_at", nullable = true)
    private Date updatedTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdAt) {
        this.createdTime = createdAt;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedAt) {
        this.updatedTime = updatedAt;
    }

}
