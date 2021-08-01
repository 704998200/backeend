package com.hwx.backeend.repository;

import com.hwx.backeend.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository {
    Role findByName(String roleName);
}
