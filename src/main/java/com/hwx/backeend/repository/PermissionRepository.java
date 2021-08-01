package com.hwx.backeend.repository;

import com.hwx.backeend.entity.Issue;
import com.hwx.backeend.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}
