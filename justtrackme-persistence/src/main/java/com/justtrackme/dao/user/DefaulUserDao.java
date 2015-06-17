package com.justtrackme.dao.user;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.justtrackme.model.Paged;
import com.justtrackme.model.SearchRequest;
import com.justtrackme.model.User;
import com.justtrackme.model.UserRole;

@Repository("userDao")
public class DefaulUserDao implements UserDao {

	private static final String USER_FIND_BY_LOGIN_AND_PASSWORD = "SELECT * FROM user WHERE `name` = ? and `password` = ?";

	@Resource(name = "jdbcTemplate")
	private JdbcOperations jdbcTemplate;

	private RowMapper<User> rowMapper = (rs, index) -> {
		User user = new User();
		user.setLogin(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		user.getRoles().add((rs.getBoolean("admin") ? UserRole.ROLE_ADMIN : UserRole.ROLE_USER));

		return user;
	};

	@Override
	public User getUserByLoginAndPassword(String userName, String password) {
		return jdbcTemplate.queryForObject(USER_FIND_BY_LOGIN_AND_PASSWORD, rowMapper, userName, password);
	}

	@Override
	public User getById(Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void create(User entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(User entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(User entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Paged<User> search(SearchRequest request) {
		throw new UnsupportedOperationException();
	}
}
