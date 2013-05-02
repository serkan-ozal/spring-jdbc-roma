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
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.cglib.proxy.LazyLoader;

import org.springframework.jdbc.roma.RowMapperObjectFieldDataProvider;
import org.springframework.jdbc.roma.config.manager.ConfigManager;
import org.springframework.jdbc.roma.domain.model.config.RowMapperObjectFieldConfig;
import org.springframework.jdbc.roma.proxy.ProxyHelper;
import org.springframework.jdbc.roma.proxy.ProxyListLoader;
import org.springframework.jdbc.roma.util.ReflectionUtil;
import org.springframework.jdbc.roma.util.SpringUtil;

public class ObjectFieldRowMapperGenerator<T> extends AbstractRowMapperFieldGenerator<T> {

	private static final Pattern BEAN_PATTERN = Pattern.compile("@\\{([^\\{]+)\\}");
	private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\$\\{([^\\{]+)\\}");
	private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("\\#\\{([^\\{]+)\\}");
	
	@SuppressWarnings("rawtypes")
	private RowMapperObjectFieldDataProvider dataProvider;
	
	public ObjectFieldRowMapperGenerator(Field field, ConfigManager configManager) {
		super(field, configManager);
	}
	
	@SuppressWarnings("rawtypes")
	public ObjectFieldRowMapperGenerator(Field field, RowMapperObjectFieldDataProvider dataProvider, 
			ConfigManager configManager) {
		super(field, configManager);
		this.dataProvider = dataProvider;
	}
	
	@SuppressWarnings("rawtypes")
	public RowMapperObjectFieldDataProvider getDataProvider() {
		return dataProvider;
	}
	
	@SuppressWarnings("rawtypes")
	public void setDataProvider(RowMapperObjectFieldDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public String doFieldMapping(Field f) {
		RowMapperObjectFieldConfig rmofc = configManager.getRowMapperObjectFieldConfig(f);
		if (rmofc == null) {
			return "";
		}
		
		String setterMethodName = getSetterMethodName(f);
		
		Class<? extends RowMapperObjectFieldDataProvider> rmdpCls = rmofc.getProvideViaDataProvider();
		if (rmdpCls != null && rmdpCls.equals(RowMapperObjectFieldDataProvider.class) == false) {
			return 
				wrapWithExceptionHandling(
					getValueFromDataProvider(f, rmofc, rmdpCls, setterMethodName));
		}
		else {
			String springCode = rmofc.getProvideViaSpring();
			if (springCode != null && springCode.length() > 0) {
				return 
					wrapWithExceptionHandling(
						getValueFromSpringCode(f, rmofc, springCode, setterMethodName));
			}
			else {
				String provideImplCode = rmofc.getProvideViaImplementationCode();
				if (provideImplCode != null && provideImplCode.length() > 0) {
					return 
						wrapWithExceptionHandling(
							getValueFromImplementationCode(f, rmofc, provideImplCode, setterMethodName));
				}
				return "";
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected String getValueFromDataProvider(Field f, RowMapperObjectFieldConfig rmofc, 
			Class<? extends RowMapperObjectFieldDataProvider> rmdpCls, String setterMethodName) {
		String rmdpClsName = rmdpCls.getName();
		String rmdpFieldName = "rmdp_" + columnName;
		String rmdpCreationCode = 
				rmdpClsName + " " + rmdpFieldName + " = " + "new " + rmdpClsName + "();"; 
		
		return 
			rmdpCreationCode + "\n" + 
			wrapWithLazyLoadingIfNeeded(f, setterMethodName, rmofc.isLazy(),  rmofc, 
				GENERATED_OBJECT_NAME + "." + 
					setterMethodName + 
					"(" + 
						"(" + f.getType().getName() + ")" + rmdpFieldName + 
							".provideData(" + GENERATED_OBJECT_NAME + ")" +
					");", null);
	}
	
	protected String getValueFromSpringCode(Field f, RowMapperObjectFieldConfig rmofc, 
			String springCode, String setterMethodName) {
		StringBuffer variables = new StringBuffer();
		springCode = processBeanNames(springCode, variables);
		springCode = processParameters(springCode, variables);
		springCode = processAttributes(springCode, variables);

		return 
			wrapWithLazyLoadingIfNeeded(f, setterMethodName, rmofc.isLazy(),  rmofc, 
				GENERATED_OBJECT_NAME + "." + 
					setterMethodName + 
					"(" + 
						"(" + f.getType().getName() + ")" + springCode +
					");", variables);
	}
	
	protected String getValueFromImplementationCode(Field f, RowMapperObjectFieldConfig rmofc, 
			String provideImplCode, String setterMethodName) {
		StringBuffer variables = new StringBuffer();
		provideImplCode = processParameters(provideImplCode, variables);
		
		Class<?>[] additionalClasses = rmofc.getAdditionalClasses();
		if (additionalClasses != null) {
			for (Class<?> cls : additionalClasses) {
				rowMapper.addAdditionalClass(cls);
			}	
		}
		
		return 
			wrapWithLazyLoadingIfNeeded(f, setterMethodName, rmofc.isLazy(),  rmofc, 
				GENERATED_OBJECT_NAME + "." + 
					setterMethodName + 
					"(" + 
						"(" + f.getType().getName() + ")" + provideImplCode +
					");", variables);
	}
	
	protected String wrapWithLazyLoadingIfNeeded(Field f, String setterMethodName, boolean isLazy, 
			RowMapperObjectFieldConfig rmofc, String generatedCode, StringBuffer variables) {
		if (isLazy) {
			int firstIndex = generatedCode.indexOf("(");
			int lastIndex = generatedCode.lastIndexOf(")");
			String setValue = generatedCode.substring(firstIndex + 1, lastIndex);
			
			Class<?> fieldCls = f.getType();
			String fieldClsName = fieldCls.getSimpleName();
			
			StringBuffer additionalClasses = new StringBuffer();
			for (Class<?> cls : rowMapper.getAdditionalClasses()) {
				additionalClasses.append(",").append(cls.getName() + ".class");
			}
			
			if (List.class.isAssignableFrom(fieldCls)) {
				fieldCls = rmofc.getFieldType();
				fieldClsName = fieldCls.getName();
				rowMapper.addAdditionalClass(fieldCls);
				setValue = setValue.replace("\"", "\\\"");
				if (variables != null) {
					setValue = variables.toString() + VARIABLES_AND_CODE_SEPARATOR + setValue;
				}
				return 
					GENERATED_OBJECT_NAME + "." + setterMethodName + "(" + "\n" +
					"\t" + "(" + f.getType().getName() + ")" + ProxyHelper.class.getName() + ".proxyList(" + fieldClsName + ".class, " + "\n" +
					"\t" + "\t" + ProxyListLoader.class.getName() + ".createProxyListLoader(" + 
								"\"" + setValue + "\"" + ", " + 
								"\"" + additionalClasses.toString() + "\"" + "," +
								"new Object[] {" + 
									"\"mappedObject\", mappedObject" + 
								"}" + 
							")" +
						")" + 
					");";
			}
			else {
				rowMapper.addAdditionalClass(fieldCls);
				setValue = setValue.replace("\"", "\\\"");
				return 
					GENERATED_OBJECT_NAME + "." + setterMethodName + "(" + "\n" +
					"\t" + "(" + f.getType().getName() + ")" + ProxyHelper.class.getName() + ".proxyObject(" + fieldClsName + ".class, " + "\n" +
					"\t" + "\t" + LazyLoader.class.getName() + ".createProxyLoader(" + 
							"\"" + variables.toString() + VARIABLES_AND_CODE_SEPARATOR + setValue + "\"" + ", " + 
							"\"" + additionalClasses.toString() + "\"" + ", " + 
								"new Object[] {" + 
									"\"mappedObject\", mappedObject" + 
								"}" + 
							")" +
						")" + 
					");";
			}
		}
		else {
			return (variables != null ? variables.toString() + "\n" : "") + generatedCode;
		}
	}
	
	protected String processBeanNames(String code, StringBuffer variables) {
		Matcher m = BEAN_PATTERN.matcher(code);
		while (m.find()) {
			String group = m.group(0);
			String springBeanName = m.group(1);
			Class<?> beanType= SpringUtil.getType(springBeanName);
			if (beanType == null) {
				continue;
			}
			rowMapper.addAdditionalClass(beanType);
			code = 
				code.replace(group, 
						"(" +
							"(" + beanType.getName() + ")" + 
							SpringUtil.class.getName() + ".getBean(\"" + springBeanName + "\")" + 
						")");
		}
		return code;
	}
	
	protected String processParameters(String code, StringBuffer variables) {
		Matcher m = PARAMETER_PATTERN.matcher(code);
		while (m.find()) {
			String param = m.group(0);
			String paramName = m.group(1);
			String randomId = UUID.randomUUID().toString().replace("-", "_");
			String variableName = paramName + "$" + randomId;
			
			Field paramField = ReflectionUtil.getField(rowMapper.getClazz(), paramName);
			if (paramField == null) {
				logger.error("Unable to find field " + paramName + " in class " + rowMapper.getClazz().getName());
				return "";
			}
			
			Class<?> paramType = paramField.getType();
			String getterMethod = "get" + 
					Character.toUpperCase(paramName.charAt(0)) + paramName.substring(1) + "()";
			String providerForParamValue = GENERATED_OBJECT_NAME + "." + getterMethod;
			if (paramType != null) {
				variables.
					append(paramType.getName()).append(" ").append(variableName).
					append(" = ").
					append(providerForParamValue).
					append(";");
				code = code.replace(param, variableName);
			}
			else {
				code = code.replace(param, providerForParamValue);
			}
		}
		return code;
	}
	
	protected String processAttributes(String code, StringBuffer variables) {
		Matcher m = ATTRIBUTE_PATTERN.matcher(code);
		while (m.find()) {
			String param = m.group(0);
			String paramName = m.group(1);
			String getterMethod = "get" + 
					Character.toUpperCase(paramName.charAt(0)) + paramName.substring(1) + "()";
			String formattedParam = getterMethod;
			code = code.replace(param, formattedParam);
		}
		return code;
	}

}
