package org.impalaframework.spring.web;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.impalaframework.plugin.plugin.ApplicationContextSet;
import org.impalaframework.plugin.plugin.SimpleParentSpec;
import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.impalaframework.spring.web.WebPluginLoader;
import org.impalaframework.spring.web.WebServletSpec;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.context.support.ServletContextResource;

public class WebPluginLoaderTest extends TestCase {

	private ServletContext servletContext;
	private WebPluginLoader loader;

	public void setUp() {
		servletContext = EasyMock.createMock(ServletContext.class);
		PropertyClassLocationResolver resolver = new PropertyClassLocationResolver();
		loader = new WebPluginLoader(resolver, servletContext);
	}
	
	public final void testNewApplicationContext() {
		final ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
		final GenericApplicationContext parent = new GenericApplicationContext();
		final GenericWebApplicationContext applicationContext = loader.newApplicationContext(parent, classLoader);

		assertNotNull(applicationContext);
		assertNotNull(applicationContext.getBeanFactory());
		assertSame(classLoader, applicationContext.getClassLoader());
		assertSame(servletContext, applicationContext.getServletContext());
	}
	
	public final void testGetClassLocations() {
		final String[] locations = new String[] {"context1", "context2"};
		WebServletSpec spec = new WebServletSpec(new SimpleParentSpec(new String[]{"loc"}), "impala-web", locations);
		final Resource[] classLocations = loader.getClassLocations(new ApplicationContextSet(), spec);
		for (Resource resource : classLocations) {
			assertTrue(resource instanceof FileSystemResource);
			assertTrue(resource.exists());
		}
	}
	
	public void testGetSpringLocations() {
		final String[] locations = new String[] {"context1", "context2"};
		WebServletSpec spec = new WebServletSpec(new SimpleParentSpec(new String[]{"loc"}), "name", locations);
		final Resource[] resources = loader.getSpringConfigResources(new ApplicationContextSet(), spec, ClassUtils.getDefaultClassLoader());
		assertEquals(2, resources.length);
		for (int i = 0; i < resources.length; i++) {
			assertTrue(resources[i] instanceof ServletContextResource);
			assertEquals(locations[i], resources[i].getFilename());
		}
	}

}
