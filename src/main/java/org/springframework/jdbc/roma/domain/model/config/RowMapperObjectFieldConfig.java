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

package org.springframework.jdbc.roma.domain.model.config;

import org.springframework.jdbc.roma.RowMapperObjectFieldDataProvider;

public class RowMapperObjectFieldConfig extends BaseRowMapperFieldConfig {

	private String provideViaSpring;
	private String provideViaImplementationCode;
	@SuppressWarnings("rawtypes")
	private Class<? extends RowMapperObjectFieldDataProvider> provideViaDataProvider;
	private Class<?>[] additionalClasses;
	private Class<?> fieldType;
	private boolean lazy = false;
	
	public String getProvideViaSpring() {
		return provideViaSpring;
	}
	
	public void setProvideViaSpring(String provideViaSpring) {
		this.provideViaSpring = provideViaSpring;
	}
	
	public String getProvideViaImplementationCode() {
		return provideViaImplementationCode;
	}
	
	public void setProvideViaImplementationCode(String provideViaImplementationCode) {
		this.provideViaImplementationCode = provideViaImplementationCode;
	}
	
	@SuppressWarnings("rawtypes")
	public Class<? extends RowMapperObjectFieldDataProvider> getProvideViaDataProvider() {
		return provideViaDataProvider;
	}
	
	@SuppressWarnings("rawtypes")
	public void setProvideViaDataProvider(Class<? extends RowMapperObjectFieldDataProvider> provideViaDataProvider) {
		this.provideViaDataProvider = provideViaDataProvider;
	}
	
	public Class<?>[] getAdditionalClasses() {
		return additionalClasses;
	}
	
	public void setAdditionalClasses(Class<?>[] additionalClasses) {
		this.additionalClasses = additionalClasses;
	}
	
	public Class<?> getFieldType() {
		return fieldType;
	}
	
	public void setFieldType(Class<?> fieldType) {
		this.fieldType = fieldType;
	}
	
	public boolean isLazy() {
		return lazy;
	}
	
	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}

}
