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

package org.springframework.jdbc.roma.integration.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.roma.config.provider.annotation.RowMapperField;
import org.springframework.jdbc.roma.config.provider.annotation.RowMapperObjectField;
import org.springframework.jdbc.roma.config.provider.annotation.RowMapperSpringProvider;

public class User implements Comparable<User> {

	@RowMapperField(columnName="id")
	private Long id;
	@RowMapperField(columnName="username")
	private String username;
	@RowMapperField(columnName="password")
	private String password;
	@RowMapperField(columnName="firstname")
	private String firstname;
	@RowMapperField(columnName="lastname")
	private String lastname;
	@RowMapperField(columnName="enabled")
	private boolean enabled = true;
	@RowMapperField(columnName="gender")
	private Gender gender;
	@RowMapperObjectField(
			provideViaSpringProvider = 
				@RowMapperSpringProvider(
						provideCode="@{roleDAO}.getUserRoleList(${id})"),
			lazy = true)
	private List<Role> roles = new ArrayList<Role>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname() {
		return lastname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public Gender getGender() {
		return gender;
	}
	
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public List<Role> getRoles() {
		return roles;
	}
	
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role) {
		roles.add(role);
	}
	
	public void removeRole(Role role) {
		roles.remove(role);
	}

	@Override
	public int compareTo(User o) {
		return (int) (id - o.id);
	}
	
}
