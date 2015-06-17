package com.justtrackme.security;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.justtrackme.model.User;
import com.justtrackme.service.user.UserService;

public class DefaultAuthenticationProvider implements AuthenticationProvider {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultAuthenticationProvider.class);

	@Resource(name = "userService")
	private UserService userService;


	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String login = authentication.getName();
		String password = authentication.getCredentials().toString();

		LOG.debug("Authenticate user [{}]", login);

		return getAuthentication(login, password);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UsernamePasswordAuthenticationToken.class.equals(clazz);
	}

	private Authentication getAuthentication(String login, String password) {
		User principal = getUser(login, password);
		Collection<GrantedAuthority> authorities = getAuthorities(principal);

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login, password, authorities);
		token.setDetails(principal);

		return token;
	}

	private User getUser(String login, String password) {
		User user = null;
		try {
			user = userService.getUserByLoginAndPassword(login, password);
		} catch (Exception e) {
			LOG.error("Can't find user for login: " + login, e);
			throw new BadCredentialsException("Bad creditials for user: " + login);
		}

		return user;
	}

	private Collection<GrantedAuthority> getAuthorities(User principal) {
		LOG.debug("Resolve user authorities: {}", principal);
		return principal.getRoles().stream().map((role) -> new SimpleGrantedAuthority(role.getSecurityCode())).collect(Collectors.toSet());
	}
}
