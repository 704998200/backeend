package com.hwx.backeend.repository;

import com.hwx.backeend.entity.Permission;
import com.hwx.backeend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
