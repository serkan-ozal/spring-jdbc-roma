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

package org.springframework.jdbc.roma.domain.model;

/**
 * @author Serkan ÖZAL
 */
public class ObjectWrapper<T> {

	private T obj;
	
	public ObjectWrapper(T obj) {
		this.obj = obj;
	}
	
	public T get() {
		return obj;
	}
	
	public void set(T obj) {
		this.obj = obj;
	}
	
	@Override
	public String toString() {
		if (obj != null) {
			return obj.toString();
		}
		else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ObjectWrapper == false) {
			return false;
		}
		ObjectWrapper<T> ow = (ObjectWrapper<T>)obj;
		if (this.obj == null && ow.obj == null) {
			return true;
		}
		else if (this.obj != null && ow.obj == null) {
			return false;
		}
		else if (this.obj == null && ow.obj != null) {
			return false;
		}
		else {
			return this.obj.equals(ow.obj);
		}	
	}
	
}
