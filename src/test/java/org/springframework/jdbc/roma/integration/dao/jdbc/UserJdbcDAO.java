/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.jdbc.roma.integration.dao.jdbc;

import java.sql.Types;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.roma.integration.dao.UserDAO;
import org.springframework.jdbc.roma.integration.model.User;
import org.springframework.stereotype.Repository;

@Repository(value="userDAO")
public class UserJdbcDAO extends BaseJdbcDAO implements UserDAO {

	private RowMapper<User> userRowMapper;
	
	@PostConstruct
	public void init() {
		userRowMapper = rowMapperService.getRowMapper(User.class);
	}

	@Override
	public User get(Long id) {
		try {
			return 
				jdbcTemplate.queryForObject(
					"SELECT u.* FROM USER u WHERE u.id = ?", userRowMapper, id);
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@Override
	public void add(User user) throws Exception {
		jdbcTemplate.update(
			"INSERT INTO USER " + 
			"(" + 
				"username, password, firstname, lastname, enabled" +
			") " + 
			"VALUES (?, ?, ?, ?, ?)", 
			new Object[] {
				user.getUsername(), user.getPassword(),
				user.getFirstname(), user.getLastname(), user.isEnabled() ? 1 : 0
			},
			new int[] {
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.NUMERIC
			});
	}

	@Override
	public List<User> list() {
		return jdbcTemplate.query("SELECT u.* FROM USER u ORDER BY u.username", userRowMapper);
	}

	@Override
	public List<User> getUserListForRole(Long roleId) {
		return 
			jdbcTemplate.query(
				"SELECT u.* FROM USER u WHERE u.id IN " +
				"(" +
					"SELECT ur.user_id FROM USER_ROLE ur WHERE ur.role_id = " + roleId +
				") ORDER BY u.username", 
				userRowMapper);
	}

}
