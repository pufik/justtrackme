package com.justtrackme.dao.user;

import com.justtrackme.dao.Dao;
import com.justtrackme.model.User;

public interface UserDao extends Dao<User, Long> {

	User getUserByLoginAndPassword(String userName, String password);
}
