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

package org.springframework.jdbc.roma.config.provider.annotation;

import java.lang.reflect.Field;

import org.springframework.jdbc.roma.config.provider.ConfigProvider;
import org.springframework.jdbc.roma.domain.builder.config.RowMapperBlobFieldConfigBuilder;
import org.springframework.jdbc.roma.domain.builder.config.RowMapperClassConfigBuilder;
import org.springframework.jdbc.roma.domain.builder.config.RowMapperClobFieldConfigBuilder;
import org.springframework.jdbc.roma.domain.builder.config.RowMapperEnumFieldConfigBuilder;
import org.springframework.jdbc.roma.domain.builder.config.RowMapperFieldConfigBuilder;
import org.springframework.jdbc.roma.domain.builder.config.RowMapperObjectFieldConfigBuilder;
import org.springframework.jdbc.roma.domain.builder.config.RowMapperTimestampFieldConfigBuilder;
import org.springframework.jdbc.roma.domain.model.config.RowMapperBlobFieldConfig;
import org.springframework.jdbc.roma.domain.model.config.RowMapperClassConfig;
import org.springframework.jdbc.roma.domain.model.config.RowMapperClobFieldConfig;
import org.springframework.jdbc.roma.domain.model.config.RowMapperEnumFieldConfig;
import org.springframework.jdbc.roma.domain.model.config.RowMapperFieldConfig;
import org.springframework.jdbc.roma.domain.model.config.RowMapperObjectFieldConfig;
import org.springframework.jdbc.roma.domain.model.config.RowMapperTimestampFieldConfig;
import org.springframework.jdbc.roma.util.ReflectionUtil;

public class AnnotationBasedConfigProvider implements ConfigProvider {

	@Override
	public boolean isAvailable() {
		return true;
	}
	
	@Override
	public RowMapperFieldConfig getRowMapperFieldConfig(Class<?> clazz, String fieldName) {
		Field field = ReflectionUtil.getField(clazz, fieldName);
		if (field.isAnnotationPresent(RowMapperField.class)) {
			RowMapperField rmf = field.getAnnotation(RowMapperField.class);
			return 
				new RowMapperFieldConfigBuilder().
						field(field).
						columnName(rmf.columnName()).	
						fieldGeneratorClass(rmf.fieldGenerator()).
					build();	
		}
		else {
			return null;
		}	
	}

	@Override
	public RowMapperObjectFieldConfig getRowMapperObjectFieldConfig(Class<?> clazz, String fieldName) {
		Field field = ReflectionUtil.getField(clazz, fieldName);
		if (field.isAnnotationPresent(RowMapperObjectField.class)) {
			RowMapperObjectField rmof = field.getAnnotation(RowMapperObjectField.class);
			return 
				new RowMapperObjectFieldConfigBuilder().
						field(field).
						provideViaSpring(rmof.provideViaSpring()).
						provideViaImplementationCode(rmof.provideViaImplementationCode()).
						provideViaDataProvider(rmof.provideViaDataProvider()).
						additionalClasses(rmof.additionalClasses()).
						fieldType(rmof.fieldType()).
						lazy(rmof.lazy()).
					build();	
		}
		else {
			return null;
		}	
	}

	@Override
	public RowMapperEnumFieldConfig getRowMapperEnumFieldConfig(Class<?> clazz, String fieldName) {
		Field field = ReflectionUtil.getField(clazz, fieldName);
		if (field.isAnnotationPresent(RowMapperEnumField.class)) {
			RowMapperEnumField rmef = field.getAnnotation(RowMapperEnumField.class);
			return 
				new RowMapperEnumFieldConfigBuilder().
						field(field).
						constantsAndMaps(rmef.constantsAndMaps()).
					build();	
		}
		else {
			return null;
		}	
	}

	@Override
	public RowMapperClobFieldConfig getRowMapperClobFieldConfig(Class<?> clazz, String fieldName) {
		Field field = ReflectionUtil.getField(clazz, fieldName);
		if (field.isAnnotationPresent(RowMapperClobField.class)) {
			return 
				new RowMapperClobFieldConfigBuilder().
						field(field).
					build();	
		}
		else {
			return null;
		}	
	}

	@Override
	public RowMapperBlobFieldConfig getRowMapperBlobFieldConfig(Class<?> clazz, String fieldName) {
		Field field = ReflectionUtil.getField(clazz, fieldName);
		if (field.isAnnotationPresent(RowMapperBlobField.class)) {
			return 
				new RowMapperBlobFieldConfigBuilder().
						field(field).
					build();	
		}
		else {
			return null;
		}	
	}

	@Override
	public RowMapperTimestampFieldConfig getRowMapperTimestampFieldConfig(Class<?> clazz, String fieldName) {
		Field field = ReflectionUtil.getField(clazz, fieldName);
		if (field.isAnnotationPresent(RowMapperTimestampField.class)) {
			return 
				new RowMapperTimestampFieldConfigBuilder().
						field(field).
					build();	
		}
		else {
			return null;
		}	
	}

	@Override
	public RowMapperClassConfig getRowMapperClassConfig(Class<?> clazz) {
		if (clazz.isAnnotationPresent(RowMapperClass.class)) {
			RowMapperClass rmc = clazz.getAnnotation(RowMapperClass.class);
			return 
				new RowMapperClassConfigBuilder().
						clazz(clazz).
						generatorFactoryClass(rmc.generatorFactory()).
					build();	
		}
		else {
			return null;
		}	
	}

}
