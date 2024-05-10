package com.cyangate.minifileservice.service;

import com.cyangate.minifileservice.model.entity.Role;
import com.cyangate.minifileservice.model.entity.User;
import com.cyangate.minifileservice.model.dto.UserDto;
import com.cyangate.minifileservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder bcryptEncoder;
	private final RoleService roleService;
	private final ModelMapper modelMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		List<SimpleGrantedAuthority> roles = new ArrayList<>();
		User user = getUserByUsername(username);

		roles.add(new SimpleGrantedAuthority(user.getUserRole().getRoleName()));
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);
	}

	@Override
	public UserDto save(UserDto userDto) {

		Role userRole = roleService.getByRoleName(userDto.getUserRoleRoleName());

		User newUser = new User();
		newUser.setUsername(userDto.getUsername());
		newUser.setPassword(bcryptEncoder.encode(userDto.getPassword()));
		newUser.setUserRole(userRole);

		UserDto userDtoResponse = modelMapper.map(userRepository.save(newUser), UserDto.class);
		userDtoResponse.setUserRoleRoleName(userRole.getRoleName());

		return userDtoResponse;
	}

	@Override
	public User getUserByUsername(String username) {
		User user = userRepository.getUserByUsername(username);
		if(user==null){
			throw new UsernameNotFoundException("Could not find user");
		}
		return user;
	}

}
