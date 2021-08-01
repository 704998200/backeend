package com.hwx.backeend.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * (Projects)实体类
 *
 * @author makejava
 * @since 2021-07-26 13:24:00
 */
@Entity
@Table(name = "projects")
public class Project implements Serializable {
    private static final long serialVersionUID = -61337710448067126L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "project_description", nullable = true)
    private String projectDescription;
    @Column(name = "project_name", nullable = true)
    private String projectName;
    @Column(name = "project_short_id", nullable = true)
    private String projectShortId;
    @Column(name = "project_status", nullable = true)
    private Integer projectStatus;

    @Column(name = "created_at", nullable = true)
    private Date createdTime;
    @Column(name = "finish_at", nullable = true)
    private Date finishTime;
    @Column(name = "start_at", nullable = true)
    private Date startTime;
    @Column(name = "updated_at", nullable = true)
    private Date updatedTime;

    public User getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }

    // 发出用户
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(
            name = "posted_by_user_id",
            referencedColumnName = "id"
    )
    //    @JSONField(serialize = false)
            User postedBy;

    // Issue 列表
    // mappedBy 是对方的字段名字
//    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true,mappedBy = "project")
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "project_issues",
            joinColumns = {@JoinColumn(
                    name = "project_id"

            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "issue_id"
            )}
    )
    @JSONField(serialize = false)
    List<Issue> issues;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectShortId() {
        return projectShortId;
    }

    public void setProjectShortId(String projectShortId) {
        this.projectShortId = projectShortId;
    }

    public Integer getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(Integer projectStatus) {
        this.projectStatus = projectStatus;
    }


    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdAt) {
        this.createdTime = createdAt;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishAt) {
        this.finishTime = finishAt;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startAt) {
        this.startTime = startAt;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedAt) {
        this.updatedTime = updatedAt;
    }

}
