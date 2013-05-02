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

package org.springframework.jdbc.roma.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.springframework.jdbc.roma.generator.RowMapperFieldGenerator;
import org.springframework.jdbc.roma.util.RowMapperUtil;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;

import net.sf.cglib.proxy.LazyLoader;

public abstract class ProxyObjectLoader<T> implements LazyLoader {
	
	public abstract T load();
	
	@Override
	public T loadObject() throws Exception {
		return load();
	}
	
	@SuppressWarnings("rawtypes")
	public static ProxyObjectLoader createProxyObjectLoader(String loadingCode, String classPath, Object[] parameters) {
		try {
			ClassPool cp = ClassPool.getDefault();
			CtClass generatedCls = cp.makeClass("ProxyObjectLoader" +  RowMapperUtil.generateRandomClassPostFix());
			generatedCls.defrost();
			generatedCls.setSuperclass((cp.get(LazyLoader.class.getName())));
			
			for (int i = 0; i < parameters.length; i++) {
				String fieldName = parameters[i++].toString();
				Object value = parameters[i];
				if (value == null) {
					continue;
				}
				CtField ctf = new CtField(cp.get(value.getClass().getName()), fieldName, generatedCls);
				generatedCls.addField(ctf);
				String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
				CtMethod ctfSetter = 
							new CtMethod(
									CtClass.voidType, 
									setterName,
									new CtClass[] {cp.get(value.getClass().getName())},
									generatedCls);
				ctfSetter.setModifiers(Modifier.PUBLIC);
				String setterBody = 
					"{" + "\n" +
						"\t" + "this." + fieldName + " = " + "$1;" + "\n" +		
					"}";
				ctfSetter.setBody(setterBody);
				generatedCls.addMethod(ctfSetter);
			}
			
			String variablesCode = "";
			int separatorIndex = loadingCode.indexOf(RowMapperFieldGenerator.VARIABLES_AND_CODE_SEPARATOR);
			if (separatorIndex > 0) {
				variablesCode = loadingCode.substring(0, separatorIndex);
				loadingCode = loadingCode.substring(separatorIndex + 
													RowMapperFieldGenerator.VARIABLES_AND_CODE_SEPARATOR.length());
			}
			CtMethod loadMethod = 
					new CtMethod(
							cp.get(Object.class.getName()), 
							"load",
							new CtClass[] {},
							generatedCls);
			loadMethod.setModifiers(Modifier.PUBLIC);
			String body = 
				"{" + "\n" +
					"\t" + variablesCode +
					"\t" + "return " + loadingCode + ";" + "\n" +		
				"}";
			loadMethod.setBody(body);
			generatedCls.addMethod(loadMethod);
			
			ProxyObjectLoader pol = (ProxyObjectLoader) generatedCls.toClass().newInstance();
			
			for (int i = 0; i < parameters.length; i++) {
				String fieldName = parameters[i++].toString();
				Object value = parameters[i];
				if (value == null) {
					continue;
				}
				String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
				Method setterMethod = 
						pol.getClass().getDeclaredMethod(setterName, value.getClass());
				setterMethod.invoke(pol, value);
			}
			
			return pol;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}