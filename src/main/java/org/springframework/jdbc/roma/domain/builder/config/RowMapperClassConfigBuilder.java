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

import org.springframework.jdbc.roma.domain.builder.Builder;
import org.springframework.jdbc.roma.domain.model.config.RowMapperClassConfig;
import org.springframework.jdbc.roma.factory.RowMapperGeneratorFactory;

/**
 * @author Serkan ÖZAL
 */
public class RowMapperClassConfigBuilder implements Builder<RowMapperClassConfig> {

	private Class<?> clazz;
	@SuppressWarnings("rawtypes")
	private Class<? extends RowMapperGeneratorFactory> generatorFactoryClass;
	
	@Override
	public RowMapperClassConfig build() {
		RowMapperClassConfig config = new RowMapperClassConfig();
		config.setClazz(clazz);
		config.setGeneratorFactoryClass(generatorFactoryClass);
		return config;
	}
	
	public RowMapperClassConfigBuilder clazz(Class<?> clazz) {
		this.clazz = clazz;
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public RowMapperClassConfigBuilder generatorFactoryClass(Class<? extends RowMapperGeneratorFactory> generatorFactoryClass) {
		if (generatorFactoryClass != null && generatorFactoryClass.equals(RowMapperGeneratorFactory.class) == false) {
			this.generatorFactoryClass = generatorFactoryClass;
		}	
		return this;
	}
	
}
