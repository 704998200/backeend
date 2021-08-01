package com.hwx.backeend.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * (Permissions)实体类
 *
 * @author makejava
 * @since 2021-07-26 13:24:00
 */
@Entity
@Table(name = "permissions")
public class Permission implements Serializable {
    private static final long serialVersionUID = 505947728058882665L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = true, unique = true)
    private String name;
    @Column(name = "permission_description", nullable = true, unique = true)
    private String permissionDescription;
    @Column(name = "url", nullable = true)
    private String url;
    // 反向关系 权限对角色
    @ManyToMany(targetEntity = Role.class)
    @JoinTable(
            name = "role_permission",
            joinColumns = {@JoinColumn(name = "permission_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    @JSONField(serialize = false)
    List<Role> roles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermissionDescription() {
        return permissionDescription;
    }

    public void setPermissionDescription(String permissionDescription) {
        this.permissionDescription = permissionDescription;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
