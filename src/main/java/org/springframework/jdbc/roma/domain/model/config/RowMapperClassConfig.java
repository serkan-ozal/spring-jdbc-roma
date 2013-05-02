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

import org.springframework.jdbc.roma.factory.RowMapperGeneratorFactory;

public class RowMapperClassConfig extends BaseRowMapperClassConfig {

	@SuppressWarnings("rawtypes")
	private Class<? extends RowMapperGeneratorFactory> generatorFactoryClass;
	
	@SuppressWarnings("rawtypes")
	public Class<? extends RowMapperGeneratorFactory> getGeneratorFactoryClass() {
		return generatorFactoryClass;
	}
	
	@SuppressWarnings("rawtypes")
	public void setGeneratorFactoryClass(Class<? extends RowMapperGeneratorFactory> generatorFactoryClass) {
		this.generatorFactoryClass = generatorFactoryClass;
	}
	
}
