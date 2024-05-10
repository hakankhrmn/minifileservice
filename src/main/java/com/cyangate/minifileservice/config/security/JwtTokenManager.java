package com.cyangate.minifileservice.config.security;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.cyangate.minifileservice.model.RoleNameEnum.ADMIN_USER;
import static com.cyangate.minifileservice.model.RoleNameEnum.REGULAR_USER;

@Service
public class JwtTokenManager {

	private static final int validity = 30 * 60 * 1000;
	private String secret="asdfghjkliqwertyuopzxcvbnmasdfghjkliqwertyuopzxcvbnm";


	public String generateToken(UserDetails userDetails) {

		Map<String, Object> claims = new HashMap<>();

		Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

		if (roles.contains(new SimpleGrantedAuthority(ADMIN_USER.name()))) {
			claims.put("isAdmin", true);
		}
		if (roles.contains(new SimpleGrantedAuthority(REGULAR_USER.name()))) {
			claims.put("isRegular", true);
		}

		return doGenerateToken(claims, userDetails.getUsername());

	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + validity))
				.signWith(SignatureAlgorithm.HS256, secret).compact();

	}

	public boolean tokenValidate(String token) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
		} catch (ExpiredJwtException ex) {
			throw ex;
		}
	}

	public String getUsernameToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

		List<SimpleGrantedAuthority> roles = new ArrayList<>();

		Boolean isAdmin = claims.get("isAdmin", Boolean.class);
		Boolean isUser = claims.get("isRegular", Boolean.class);

		if (isAdmin != null && isAdmin) {
			roles.add(new SimpleGrantedAuthority(ADMIN_USER.name()));
		}

		if (isUser != null && isUser) {
			roles.add(new SimpleGrantedAuthority(REGULAR_USER.name()));
		}
		return roles;

	}

}
