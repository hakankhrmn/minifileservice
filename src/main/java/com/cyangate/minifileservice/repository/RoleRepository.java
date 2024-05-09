package com.cyangate.minifileservice.repository;

import com.cyangate.minifileservice.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
	Role getByRoleName(String roleName);
}
