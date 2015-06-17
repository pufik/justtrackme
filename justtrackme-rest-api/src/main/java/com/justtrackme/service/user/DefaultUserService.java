package com.justtrackme.service.user;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.justtrackme.dao.user.UserDao;
import com.justtrackme.model.User;

@Service("userService")
public class DefaultUserService implements UserService {

	@Resource(name = "userDao")
	private UserDao userDao;

	@Override
	public User getUserByLoginAndPassword(String userName, String password) {
		return userDao.getUserByLoginAndPassword(userName, password);
	}
}
