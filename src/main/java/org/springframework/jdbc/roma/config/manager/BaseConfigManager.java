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

package org.springframework.jdbc.roma.config.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.roma.config.provider.ConfigProvider;

public abstract class BaseConfigManager implements ConfigManager {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected List<ConfigProvider> configProviderList = new ArrayList<ConfigProvider>();
	
	public BaseConfigManager() {
		init();
	}
	
	abstract protected void init();
	
	protected void addConfigProviderIfAvailable(ConfigProvider configProvider) {
		if (configProvider.isAvailable()) {
			configProviderList.add(configProvider);
		}
	}
	
	@Override
	public void addConfigProvider(ConfigProvider configProvider) {
		configProviderList.add(configProvider);
	}
	
	@Override
	public void removeConfigProvider(ConfigProvider configProvider) {
		configProviderList.remove(configProvider);
	}
	
	@Override
	public List<ConfigProvider> getAllConfigProviders() {
		return configProviderList;
	}
	
}
