package com.justtrackme.security;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.AuthenticationException;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAuthenticationEntryPointTest {

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private AuthenticationException authException;

	@InjectMocks
	private DefaultAuthenticationEntryPoint unit = new DefaultAuthenticationEntryPoint();

	private String expectedMessage = "My expected message";

	@Test
	public void shouldSetUnauthorizedError() throws Exception {
		doNothing().when(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, expectedMessage);
		given(authException.getMessage()).willReturn(expectedMessage);

		unit.commence(request, response, authException);

		verifyZeroInteractions(request);
		verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, expectedMessage);
	}
}
