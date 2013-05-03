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

package org.springframework.jdbc.roma.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Serkan ÖZAL
 */
public class ReflectionUtil {

	private ReflectionUtil() {
		
	}
	
	public static Field getField(Class<?> cls, String fieldName) {
		while (cls != null && cls.equals(Object.class) == false) {
			Field field = null;
			try {
				field = cls.getDeclaredField(fieldName);
			} 
			catch (SecurityException e) {
				e.printStackTrace();
			} 
			catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
			if (field != null) {
				field.setAccessible(true);
				return field;
			}
			cls = cls.getSuperclass();
		}
		return null;
	}
	
	public static List<Field> getAllFields(Class<?> cls) {
		List<Field> fields = new ArrayList<Field>();
		createFields(cls, fields, null);
		return fields;
	}

	public static List<Field> getAllFields(Class<?> cls, Class<? extends Annotation> annotationFilter) {
		List<Field> fields = new ArrayList<Field>();
		createFields(cls, fields, annotationFilter);
		return fields;
	}
	
	private static void createFields(Class<?> cls, List<Field> fields, 
			Class<? extends Annotation> annotationFilter) {
		if (cls == null || cls.equals(Object.class)) {
			return;
		}
		
		Class<?> superCls = cls.getSuperclass();
		createFields(superCls, fields, annotationFilter);
		
		for (Field f : cls.getDeclaredFields()) {
			f.setAccessible(true);
			if (annotationFilter == null) {
				fields.add(f);
			}	
			else {
				if (f.getAnnotation(annotationFilter) != null) {
					fields.add(f);
				}	
			}
		}	
	}
	
	public static boolean isPrimitiveType(Class<?> cls) {
		if (cls.equals(boolean.class) || cls.equals(Boolean.class)) {
			return true;
		}	
		else if (cls.equals(byte.class) || cls.equals(Byte.class)) {
			return true;
		}	
		else if (cls.equals(char.class) || cls.equals(Character.class)) {
			return true;
		}	
		else if (cls.equals(short.class) || cls.equals(Short.class)) {
			return true;
		}	
		else if (cls.equals(int.class) || cls.equals(Integer.class)) {
			return true;
		}	
		else if (cls.equals(float.class) || cls.equals(Float.class)) {
			return true;
		}	
		else if (cls.equals(long.class) || cls.equals(Long.class)) {
			return true;
		}	
		else if (cls.equals(double.class) || cls.equals(Double.class)) {
			return true;
		}
		else if (cls.equals(String.class)) {
			return true;
		}
		else {
			return false;
		}	
	}
	
	public static boolean isNonPrimitiveType(Class<?> cls) {
		return !isPrimitiveType(cls);
	}
	
	public static boolean isComplexType(Class<?> cls) {
		if (isPrimitiveType(cls)) {
			return false;
		}
		else if (cls.isEnum()) {
			return false;
		}
		else if (cls.equals(String.class)) {
			return false;
		}
		else if (isCollectionType(cls)) {
			return false;
		}
		else if (List.class.isAssignableFrom(cls)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public static boolean isCollectionType(Class<?> cls) {
		if (cls.isArray()) {
			return true;
		}
		else if (List.class.isAssignableFrom(cls)) {
			return true;
		}
		else if (Set.class.isAssignableFrom(cls)) {
			return true;
		}
		else if (Map.class.isAssignableFrom(cls)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static Class<?> getNonPrimitiveType(Class<?> cls) {
		if (cls.equals(boolean.class)) {
			return Boolean.class;
		}	
		else if (cls.equals(byte.class)) {
			return Byte.class;
		}
		else if (cls.equals(char.class)) {
			return Character.class;
		}
		else if (cls.equals(short.class)) {
			return Short.class;
		}	
		else if (cls.equals(int.class)) {
			return Integer.class;
		}	
		else if (cls.equals(float.class)) {
			return Float.class;
		}	
		else if (cls.equals(long.class)) {
			return Long.class;
		}	
		else if (cls.equals(double.class)) {
			return Double.class;
		}	
		else {
			return cls;
		}	
	}
	
	public static boolean isDecimalType(Class<?> cls) {
		if (cls.equals(byte.class) || cls.equals(Byte.class)) {
			return true;
		}	
		else if (cls.equals(short.class) || cls.equals(Short.class)) {
			return true;
		}	
		else if (cls.equals(int.class) || cls.equals(Integer.class)) {
			return true;
		}	
		else if (cls.equals(long.class) || cls.equals(Long.class)) {
			return true;
		}	
		else {
			return false;
		}
	}	
	
}
