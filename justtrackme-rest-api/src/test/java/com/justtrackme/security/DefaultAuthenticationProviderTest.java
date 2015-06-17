package com.justtrackme.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.justtrackme.model.User;
import com.justtrackme.service.user.UserService;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAuthenticationProviderTest {

	@Mock
	private UserService userService;

	@Mock
	private Authentication authentication;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@InjectMocks
	private DefaultAuthenticationProvider unit = new DefaultAuthenticationProvider();

	@Test
	public void shouldSupportUsernamePasswordAuthenticationToken() throws Exception {
		Class<?> clazz = UsernamePasswordAuthenticationToken.class;
		assertTrue(unit.supports(clazz));
	}

	@Test
	public void shouldNotSupportOtherClassesThenUsernamePasswordAuthenticationToken() throws Exception {
		Class<?> clazz = Exception.class;
		assertFalse(unit.supports(clazz));
	}

	@Test
	public void shouldAuthenticateUser() throws Exception {
		String userName = "my user name";
		String password = "user password";
		User user = new User();
		user.setLogin(userName);
		user.setPassword(password);

		given(authentication.getName()).willReturn(userName);
		given(authentication.getCredentials()).willReturn(password);
		given(userService.getUserByLoginAndPassword(userName, password)).willReturn(user);

		Authentication actualAuth = unit.authenticate(authentication);
		
		verify(userService).getUserByLoginAndPassword(userName, password);
		assertEquals(UsernamePasswordAuthenticationToken.class, actualAuth.getClass());
		assertEquals(user, actualAuth.getDetails());
		
	}
	
	@Test
	public void shouldThrowExceptionForUnknownUser() throws Exception {
		String userName = "my user name";
		String password = "user password";
		
		given(authentication.getName()).willReturn(userName);
		given(authentication.getCredentials()).willReturn(password);
		doThrow(new RuntimeException()).when(userService).getUserByLoginAndPassword(userName, password);

		expectedException.expect(BadCredentialsException.class);
		expectedException.expectMessage("Bad creditials for user: " + userName);
		
		unit.authenticate(authentication);
	}
}
