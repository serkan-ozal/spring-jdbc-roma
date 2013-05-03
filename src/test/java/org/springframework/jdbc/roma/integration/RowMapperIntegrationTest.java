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

package org.springframework.jdbc.roma.integration;

import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.roma.integration.dao.UserDAO;
import org.springframework.jdbc.roma.integration.model.Gender;
import org.springframework.jdbc.roma.integration.model.Permission;
import org.springframework.jdbc.roma.integration.model.Role;
import org.springframework.jdbc.roma.integration.model.User;

/**
 * @author Serkan ÖZAL
 */
public class RowMapperIntegrationTest extends BaseRomaIntegrationTest {
	
	@Autowired
	private UserDAO userDAO;

	@Test
	public void usersAndTheirRolesAndTheirPermissionsRetrievedSuccessfully() {
		List<User> userList = userDAO.list();
		
		Assert.assertNotNull(userList);
		Assert.assertEquals(1, userList.size());
		
		User user = userList.get(0);
		
		Assert.assertEquals("user", user.getUsername());
		Assert.assertEquals("password", user.getPassword());
		Assert.assertEquals("Serkan", user.getFirstname());
		Assert.assertEquals("OZAL", user.getLastname());
		Assert.assertEquals(Gender.MALE, user.getGender());
		
		List<Role> roleList = user.getRoles();
		
		Assert.assertNotNull(roleList);
		Assert.assertEquals(1, roleList.size());
		
		Role role = roleList.get(0);
		
		Assert.assertEquals("Admin", role.getName());
		
		List<Permission> permissionList = role.getPermissions();
		Collections.sort(permissionList);
		
		Assert.assertNotNull(permissionList);
		Assert.assertEquals(4, permissionList.size());
		
		Permission p0 = permissionList.get(0);
		Permission p1 = permissionList.get(1);
		Permission p2 = permissionList.get(2);
		Permission p3 = permissionList.get(3);
		
		Assert.assertEquals("GET_PERM", p0.getName());
		Assert.assertEquals("LIST_PERM", p1.getName());
		Assert.assertEquals("UPDATE_PERM", p2.getName());
		Assert.assertEquals("DELETE_PERM", p3.getName());
	}
	
}
