package com.cyangate.minifileservice.service;

import com.cyangate.minifileservice.model.entity.User;
import com.cyangate.minifileservice.model.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	UserDto save(UserDto userDto);
	User getUserByUsername(String username);

}
