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

package org.springframework.jdbc.roma.domain.builder.config;

import java.lang.reflect.Field;

import org.springframework.jdbc.roma.domain.builder.Builder;
import org.springframework.jdbc.roma.domain.model.config.RowMapperImplementationProviderConfig;

public class RowMapperImplementationProviderConfigBuilder implements Builder<RowMapperImplementationProviderConfig> {

	private Field field;
	private String provideCode;
	private Class<?>[] usedClasses;
	
	@Override
	public RowMapperImplementationProviderConfig build() {
		RowMapperImplementationProviderConfig config = new RowMapperImplementationProviderConfig();
		config.setField(field);
		config.setProvideCode(provideCode);
		config.setUsedClasses(usedClasses);
		return config;
	}
	
	public RowMapperImplementationProviderConfigBuilder field(Field field) {
		this.field = field;
		return this;
	}
	
	public RowMapperImplementationProviderConfigBuilder provideCode(String provideCode) {
		this.provideCode = provideCode;
		return this;
	}
	
	public RowMapperImplementationProviderConfigBuilder usedClasses(Class<?>[] usedClasses) {
		this.usedClasses = usedClasses;
		return this;
	}
	
}
