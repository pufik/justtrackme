package com.justtrackme.service.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.justtrackme.dao.user.UserDao;
import com.justtrackme.model.User;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserServiceTest {

	@Mock
	private UserDao userDao;

	@InjectMocks
	private DefaultUserService unit = new DefaultUserService();

	@Test
	public void shouldReturnUserByLoginAndPassword() throws Exception {
		String userName = "testLogin";
		String password = "test password";
		User expectedUser = new User();
		expectedUser.setLogin(userName);
		expectedUser.setPassword(password);

		given(userDao.getUserByLoginAndPassword(userName, password)).willReturn(expectedUser);

		User actualUser = unit.getUserByLoginAndPassword(userName, password);

		verify(userDao).getUserByLoginAndPassword(userName, password);
		assertEquals(expectedUser, actualUser);
	}
}
