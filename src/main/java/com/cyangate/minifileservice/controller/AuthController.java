package com.cyangate.minifileservice.controller;

import com.cyangate.minifileservice.config.security.JwtTokenManager;
import com.cyangate.minifileservice.model.dto.AuthenticationRequest;
import com.cyangate.minifileservice.model.dto.AuthenticationResponse;
import com.cyangate.minifileservice.model.dto.UserDto;
import com.cyangate.minifileservice.model.entity.User;
import com.cyangate.minifileservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

	private final JwtTokenManager jwtTokenManager;
	private final AuthenticationManager authenticationManager;
	private final UserService userService;
	private final ModelMapper modelMapper;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));


		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}

		UserDetails userdetails = userService.loadUserByUsername(authenticationRequest.getUsername());
		String token = jwtTokenManager.generateToken(userdetails);

		User user = userService.getUserByUsername(authenticationRequest.getUsername());
		UserDto userDto = modelMapper.map(user, UserDto.class);
		userDto.setUserRoleRoleName(user.getUserRole().getRoleName());
		return ResponseEntity.ok(new AuthenticationResponse(token, userDto));

	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDto userDto) throws Exception {
		return ResponseEntity.ok(userService.save(userDto));
	}
}