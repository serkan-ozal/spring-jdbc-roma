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

package org.springframework.jdbc.roma.generator;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.roma.GeneratedRowMapper;
import org.springframework.jdbc.roma.config.manager.ConfigManager;
import org.springframework.jdbc.roma.domain.model.config.RowMapperFieldConfig;
import org.springframework.jdbc.roma.util.ReflectionUtil;

public abstract class AbstractRowMapperFieldGenerator<T> implements RowMapperFieldGenerator<T> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected ConfigManager configManager;
	protected Field field;
	protected Class<?> fieldCls;
	protected String columnName;
	protected GeneratedRowMapper<T> rowMapper;
	
	public AbstractRowMapperFieldGenerator(Field field, ConfigManager configManager) {
		this.field = field;
		this.configManager = configManager;
		this.fieldCls = field.getType();
		RowMapperFieldConfig rmfc = configManager.getRowMapperFieldConfig(field);
		if (rmfc != null) {
			this.columnName = rmfc.getColumnName();
		}	
		field.setAccessible(true);
	}
	
	@Override
	public void assignedToRowMapper(GeneratedRowMapper<T> rowMapper) {
		this.rowMapper = rowMapper;
	}

	protected String getSetterMethodName(Field f) {
		String fieldName = f.getName();
		return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	}
	
	abstract protected String doFieldMapping(Field f);
	
	@Override
	public final String generateFieldMapping(Field f) {
		return doFieldMapping(f);
	}

	protected String wrapWithNullCheck(String generated, String setterMethodName) {
		if (ReflectionUtil.isPrimitiveType(field.getType())) {
			return generated;
		}
		else {
			return 
				generated + "\n" +
				"\t" + "if " + "(" + RESULT_SET_ARGUMENT + ".wasNull()" + ")" + "\n" +
				"\t" + "{" + "\n" +
				"\t\t" + 	GENERATED_OBJECT_NAME + "." + setterMethodName + "(" + "null" + ");" + "\n" +
				"\t" + "}";
		}	
	}
	
	protected String wrapWithExceptionHandling(String generated) {
		return 
			""   + "try" + "\n" +
			"\t" + "{" + "\n" +
			"\t" + "\t" + generated + "\n" +
			"\t" + "}" + "\n" +
			"\t" + "catch (Throwable t)" + "\n" + 
			"\t" + "{" + "\n" +
			"\t" + "\t" + "t.printStackTrace();\n" +
			"\t" + "}";
	}
	
}
