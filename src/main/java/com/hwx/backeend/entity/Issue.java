package com.hwx.backeend.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * (Issues)实体类
 *
 * @author makejava
 * @since 2021-07-26 13:24:00
 */
@Entity
@Table(name = "issues")
public class Issue implements Serializable {
    private static final long serialVersionUID = -67551526789122500L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "issue_content", nullable = true)
    private String issueContent;
    @Column(name = "issue_title", nullable = true)
    private String issueTitle;


    private Long projectId;
    @Column(name = "status", nullable = true)
    private Integer status;
    @Column(name = "created_at", nullable = true)
    private Date createdTime;
    @Column(name = "updated_at", nullable = true)
    private Date updatedTime;
    // 发出用户
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(
            name = "posted_by_user_id",
            referencedColumnName = "id"
    )
    User postedBy;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "project_issues",
            joinColumns = {@JoinColumn(
                    name = "issue_id"

            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "project_id"
            )}
    )
    @JSONField(serialize = false)
    Project project;

    public User getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<User> getAssignUsers() {
        return assignUsers;
    }

    public void setAssignUsers(List<User> assignUsers) {
        this.assignUsers = assignUsers;
    }

    // 分配给的用户
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "issue_assign_user",
            joinColumns = {@JoinColumn(
                    name = "issue_id",
                    foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)

            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "assign_user_id",
                    foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)
            )}
    )
    @JSONField(serialize = false)
    List<User> assignUsers=new ArrayList<User>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssueContent() {
        return issueContent;
    }

    public void setIssueContent(String issueContent) {
        this.issueContent = issueContent;
    }

    public String getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
