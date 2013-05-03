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

package org.springframework.jdbc.roma.factory;

import java.lang.reflect.Field;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.roma.config.manager.ConfigManager;
import org.springframework.jdbc.roma.domain.model.config.RowMapperBlobFieldConfig;
import org.springframework.jdbc.roma.domain.model.config.RowMapperFieldConfig;
import org.springframework.jdbc.roma.domain.model.config.RowMapperObjectFieldConfig;
import org.springframework.jdbc.roma.generator.BlobFieldRowMapperGenerator;
import org.springframework.jdbc.roma.generator.BooleanFieldRowMapperGenerator;
import org.springframework.jdbc.roma.generator.ByteFieldRowMapperGenerator;
import org.springframework.jdbc.roma.generator.CharacterFieldRowMapperGenerator;
import org.springframework.jdbc.roma.generator.DateFieldRowMapperGenerator;
import org.springframework.jdbc.roma.generator.DoubleFieldRowMapperGenerator;
import org.springframework.jdbc.roma.generator.EnumFieldRowMapperGenerator;
import org.springframework.jdbc.roma.generator.FloatFieldRowMapperGenerator;
import org.springframework.jdbc.roma.generator.IntegerFieldRowMapperGenerator;
import org.springframework.jdbc.roma.generator.LongFieldRowMapperGenerator;
import org.springframework.jdbc.roma.generator.ObjectFieldRowMapperGenerator;
import org.springframework.jdbc.roma.generator.RowMapperFieldGenerator;
import org.springframework.jdbc.roma.generator.ShortFieldRowMapperGenerator;
import org.springframework.jdbc.roma.generator.StringFieldRowMapperGenerator;

/**
 * @author Serkan ÖZAL
 */
public class DefaultRowMapperGeneratorFactory<T> implements RowMapperGeneratorFactory<T> {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private ConfigManager configManager;
	
	public DefaultRowMapperGeneratorFactory(ConfigManager configManager) {
		this.configManager = configManager;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public RowMapperFieldGenerator<T> createRowMapperFieldGenerator(Field f) {
		RowMapperFieldConfig rmfc = configManager.getRowMapperFieldConfig(f);
		RowMapperObjectFieldConfig rmofc = configManager.getRowMapperObjectFieldConfig(f);
	
		if (rmfc == null && rmofc == null) {
			return null;
		}
		
		if (rmfc != null) {
			Class<? extends RowMapperFieldGenerator> fieldGenCls = rmfc.getFieldGeneratorClass();
			if (fieldGenCls != null && fieldGenCls.equals(RowMapperFieldGenerator.class) == false) {
				try {
					RowMapperFieldGenerator fieldGen = fieldGenCls.newInstance();
					return fieldGen;
				} 
				catch (Exception e) {
					logger.error("Unable to create instance of class " + fieldGenCls.getName(), e);
				} 
			}
		}
		
		RowMapperFieldGenerator<T> rmfg = null;
		
		Class<?> cls = f.getType();
		RowMapperBlobFieldConfig rmbfc = configManager.getRowMapperBlobFieldConfig(f);
		
		if (rmbfc != null) {
			rmfg = new BlobFieldRowMapperGenerator<T>(f, configManager);
		}
		else if (rmofc != null) {
			rmfg = new ObjectFieldRowMapperGenerator<T>(f, configManager);
		}
		else if (cls.equals(boolean.class) || cls.equals(Boolean.class)) {
			rmfg = new BooleanFieldRowMapperGenerator<T>(f, configManager);
		}	
		else if (cls.equals(byte.class) || cls.equals(Byte.class)) {
			rmfg = new ByteFieldRowMapperGenerator<T>(f, configManager);
		}	
		else if (cls.equals(char.class) || cls.equals(Character.class)) {
			rmfg = new CharacterFieldRowMapperGenerator<T>(f, configManager);
		}	
		else if (cls.equals(short.class) || cls.equals(Short.class)) {
			rmfg = new ShortFieldRowMapperGenerator<T>(f, configManager);
		}	
		else if (cls.equals(int.class) || cls.equals(Integer.class)) {
			rmfg = new IntegerFieldRowMapperGenerator<T>(f, configManager);
		}	
		else if (cls.equals(float.class) || cls.equals(Float.class)) {
			rmfg = new FloatFieldRowMapperGenerator<T>(f, configManager);
		}	
		else if (cls.equals(long.class) || cls.equals(Long.class)) {
			rmfg = new LongFieldRowMapperGenerator<T>(f, configManager);
		}	
		else if (cls.equals(double.class) || cls.equals(Double.class)) {
			rmfg = new DoubleFieldRowMapperGenerator<T>(f, configManager);
		}
		else if (cls.equals(String.class) || cls.equals(String.class)) {
			rmfg = new StringFieldRowMapperGenerator<T>(f, configManager);
		}
		else if (cls.equals(Date.class)) {
			rmfg = new DateFieldRowMapperGenerator<T>(f, configManager);
		}
		else if (cls.isEnum()) {
			rmfg = new EnumFieldRowMapperGenerator<T>(f, configManager);
		}
		else {
			rmfg = new ObjectFieldRowMapperGenerator<T>(f, configManager);
		}
		
		return rmfg;
	}
	
}
