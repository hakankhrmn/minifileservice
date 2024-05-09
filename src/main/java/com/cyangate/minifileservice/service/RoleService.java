package com.cyangate.minifileservice.service;

import com.cyangate.minifileservice.model.entity.Role;

public interface RoleService {
	Role getByRoleName(String roleName);
	void addRole(String roleName);
}
