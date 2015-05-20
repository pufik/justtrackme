package com.xdev.template.rest.controller;

import java.util.Collection;
import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.xdev.template.rest.resource.Message;
import com.xdev.template.rest.resource.User;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController {

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Collection<User> getUsers() {
		return Collections.singletonList(new User());
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public User createUser(@RequestBody User user) {
		return user;
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public User updateUser(@RequestBody User user) {
		return user;
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public Message deleteUser(@PathVariable(value = "userId") String userId) {
		return Message.Builder.info("removed: " + userId).build();
	}

}
