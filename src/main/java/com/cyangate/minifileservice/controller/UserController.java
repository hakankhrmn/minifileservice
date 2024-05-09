package com.cyangate.minifileservice.controller;

import com.cyangate.minifileservice.model.entity.User;
import com.cyangate.minifileservice.model.dto.UserDto;
import com.cyangate.minifileservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

	private final UserService userService;
	private final ModelMapper modelMapper;


	@GetMapping("/")
	@PreAuthorize("hasAnyAuthority('ADMIN_USER','REGULAR_USER')")
	public ResponseEntity<UserDto> getAuthenticatedUser(){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}
		User user = userService.getUserByUsername(username);
		UserDto userDto = null;
		if(user != null){
			userDto= modelMapper.map(user, UserDto.class);
			userDto.setUserRoleRoleName(user.getUserRole().getRoleName());
		}

		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}
}
