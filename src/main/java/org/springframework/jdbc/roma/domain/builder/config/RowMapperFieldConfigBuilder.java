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
import org.springframework.jdbc.roma.domain.model.config.RowMapperFieldConfig;
import org.springframework.jdbc.roma.generator.RowMapperFieldGenerator;

/**
 * @author Serkan ÖZAL
 */
public class RowMapperFieldConfigBuilder implements Builder<RowMapperFieldConfig> {

	private Field field;
	private String columnName;
	@SuppressWarnings("rawtypes")
	private Class<? extends RowMapperFieldGenerator> fieldGeneratorClass;
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public RowMapperFieldConfig build() {
		RowMapperFieldConfig config = new RowMapperFieldConfig();
		config.setField(field);
		config.setColumnName(columnName);
		config.setFieldGeneratorClass((Class<? extends RowMapperFieldGenerator>) fieldGeneratorClass);
		return config;
	}
	
	public RowMapperFieldConfigBuilder field(Field field) {
		this.field = field;
		return this;
	}
	
	public RowMapperFieldConfigBuilder columnName(String columnName) {
		this.columnName = columnName;
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public RowMapperFieldConfigBuilder fieldGeneratorClass(Class<? extends RowMapperFieldGenerator> fieldGeneratorClass) {
		if (fieldGeneratorClass != null && fieldGeneratorClass.equals(RowMapperFieldGenerator.class) == false) {
			this.fieldGeneratorClass = fieldGeneratorClass;
		}	
		return this;
	}

}
