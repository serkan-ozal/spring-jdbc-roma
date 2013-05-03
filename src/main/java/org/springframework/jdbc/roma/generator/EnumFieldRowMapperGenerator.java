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
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.roma.config.manager.ConfigManager;
import org.springframework.jdbc.roma.domain.model.config.RowMapperEnumFieldConfig;

public class EnumFieldRowMapperGenerator<T> extends AbstractRowMapperFieldGenerator<T> {

	private Object[] enumConstants;
	private RowMapperEnumFieldConfig rmefc;
	private String[] constantsAndMapsArray;
	private Map<String, String> constantsAndMaps = new HashMap<String, String>();
	
	public EnumFieldRowMapperGenerator(Field field, ConfigManager configManager) {
		super(field, configManager);
		init();
	}
	
	private void init() {
		enumConstants = fieldCls.getEnumConstants();
		rmefc = configManager.getRowMapperEnumFieldConfig(field);
		if (rmefc != null) {
			constantsAndMapsArray = rmefc.getConstantsAndMaps();
			if (constantsAndMapsArray != null) {
				for (String constantAndMap : constantsAndMapsArray) {
					String[] constantAndMapParts = constantAndMap.split(":");
					String constant = constantAndMapParts[0];
					String map = constantAndMapParts[1];
					constantsAndMaps.put(constant, map);
				}
			}
		}
	}
	
	@Override
	public String doFieldMapping(Field f) {
		String setterMethodName = getSetterMethodName(f);
		if (enumConstants == null) {
			return null;
		}
		String setValueExpr = RESULT_SET_ARGUMENT + ".getInt(\"" + columnName + "\")";
		if (f.getType().equals(Integer.class)) {
			setValueExpr = "Integer.valueOf" + "(" + setValueExpr + ")";
		}
		if (rmefc == null) {
			setValueExpr = f.getType().getName() + ".values()" + "[" + setValueExpr + "]";
			return 
				wrapWithNullCheck(	
					GENERATED_OBJECT_NAME + "." + setterMethodName + "(" + setValueExpr + ");",
					setterMethodName);
		}
		else {
			StringBuffer sb = new StringBuffer();
			String intValueFieldName = field.getName() + "IntVal";
			sb.
				append("Integer " + intValueFieldName).
				append(" = ").
				append("Integer.valueOf").append("(").append(setValueExpr).append(")").
				append(";").append("\n");

			for (int i = 0; i < constantsAndMapsArray.length; i++) {
				String constantAndMap = constantsAndMapsArray[i];
				String[] constantAndMapParts = constantAndMap.split(":");
				String constant = constantAndMapParts[0];
				String map = constantAndMapParts[1];
				sb.append("\t");
				if (i > 0) {
					sb.append("else ");
				}
				sb.
					append("if ").
						append("(").
							append(intValueFieldName + ".equals" + "(" + "Integer.valueOf" + "(" + constant + ")" + ")").
						append(")").append("\n").
					append("\t").append("{").append("\n").
						append("\t").append("\t").append(GENERATED_OBJECT_NAME + "." + setterMethodName).
							append("(").
								append(fieldCls.getName() + ".valueOf" + "(" + "\"" + map + "\"" + ")").
							append(")").append(";").append("\n").
					append("\t").append("}").append("\n");
			}
			
			return wrapWithExceptionHandling(sb.toString());
		}
	}

}
