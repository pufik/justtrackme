package com.justtrackme.service.user;

import com.justtrackme.model.User;

public interface UserService {

	User getUserByLoginAndPassword(String userName, String password);

}
