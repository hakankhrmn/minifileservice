package com.cyangate.minifileservice.service;

import com.cyangate.minifileservice.model.entity.Role;
import com.cyangate.minifileservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	@Override
	public Role getByRoleName(String roleName) {
		return roleRepository.getByRoleName(roleName);
	}

	@Override
	public void addRole(String roleName) {
		Role role = new Role();
		role.setRoleName(roleName);
		roleRepository.save(role);
	}
}
