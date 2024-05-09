package com.cyangate.minifileservice.config;

import com.cyangate.minifileservice.model.entity.Role;
import com.cyangate.minifileservice.model.entity.User;
import com.cyangate.minifileservice.repository.UserRepository;
import com.cyangate.minifileservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.cyangate.minifileservice.model.RoleName.ADMIN_USER;
import static com.cyangate.minifileservice.model.RoleName.REGULAR_USER;

@Component
public class DbInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final RoleService roleService;
	private final BCryptPasswordEncoder bcryptEncoder;

	@Autowired
	public DbInitializer(UserRepository userRepository, RoleService roleService, @Lazy BCryptPasswordEncoder bcryptEncoder) {
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.bcryptEncoder = bcryptEncoder;
	}

	@Override
	@Transactional
	public void run(String... strings) throws Exception {

		if(roleService.getByRoleName(ADMIN_USER.name())==null){

			roleService.addRole(ADMIN_USER.name());
			roleService.addRole(REGULAR_USER.name());
		}

		if (userRepository.getUserByUsername("admin")==null){
			Role adminUserRole = roleService.getByRoleName(ADMIN_USER.name());
			User adminUser = new User();
			adminUser.setUsername("admin");
			adminUser.setPassword(bcryptEncoder.encode("admin"));
			adminUser.setUserRole(adminUserRole);
			userRepository.save(adminUser);
			System.out.println("Admin is saved");

			Role regularUserRole = roleService.getByRoleName(REGULAR_USER.name());
			User regularUser = new User();
			regularUser.setUsername("user");
			regularUser.setPassword(bcryptEncoder.encode("user"));
			regularUser.setUserRole(regularUserRole);
			userRepository.save(regularUser);
			System.out.println("Regular user is saved");
		}
	}
}
