/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.service;

import java.util.List;
import java.util.Map;

/**
 * Interface for shared registry for services used by Impala to share beans between modules.
 * 
 * @author Phil Zoio
 */
public interface ServiceRegistry {

	void addService(String beanName, String moduleName, Object service, ClassLoader classLoader);	

	void addService(String beanName, String moduleName, Object service, Map<String,?> attributes, ClassLoader classLoader);

	void remove(Object service);
	
	/**
	 * Evicts the services contributing from a particular module
	 */
	void evictModuleServices(String moduleName);

	ServiceRegistryReference getService(String beanName, Class<?>[] interfaces);
	
	List<ServiceRegistryReference> getServices(ServiceReferenceFilter filter);

}