package org.jboss.tools.hibernate.runtime.v_3_6.internal;

import java.lang.reflect.Method;

import org.hibernate.tool.hbm2x.GenericExporter;
import org.jboss.tools.hibernate.runtime.common.AbstractGenericExporterFacade;
import org.jboss.tools.hibernate.runtime.common.IFacadeFactory;
import org.jboss.tools.hibernate.runtime.spi.IGenericExporter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;


public class GenericExporterFacadeTest {

	private static final IFacadeFactory FACADE_FACTORY = new FacadeFactoryImpl();
	
	private IGenericExporter genericExporterFacade = null; 
	private GenericExporter genericExporter = null;
	
	private String methodName = null;
	private Object[] arguments = null;
	
	@Before
	public void setUp() throws Exception {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(GenericExporter.class);
		enhancer.setCallback(new MethodInterceptor() {
			@Override
			public Object intercept(
					Object obj, 
					Method method, 
					Object[] args, 
					MethodProxy proxy) throws Throwable {
				if (methodName == null) {
					methodName = method.getName();
				}
				if (arguments == null) {
					arguments = args;
				}
				return proxy.invokeSuper(obj, args);
			}					
		});
		genericExporter = (GenericExporter)enhancer.create();
		genericExporterFacade = new AbstractGenericExporterFacade(FACADE_FACTORY, genericExporter) {};
		reset();
	}
	
	@Test
	public void testSetFilePattern() {
		genericExporter.setFilePattern("barfoo");
		Assert.assertEquals("barfoo", genericExporter.getFilePattern());
		reset();
		genericExporterFacade.setFilePattern("foobar");
		Assert.assertEquals("setFilePattern", methodName);
		Assert.assertArrayEquals(new Object[] { "foobar" }, arguments);
		Assert.assertEquals("foobar", genericExporter.getFilePattern());
	}
	
	@Test
	public void testSetTemplate() {
		genericExporter.setTemplateName("barfoo");
		Assert.assertEquals("barfoo", genericExporter.getTemplateName());
		reset();
		genericExporterFacade.setTemplateName("foobar");
		Assert.assertEquals("setTemplateName", methodName);
		Assert.assertArrayEquals(new Object[] { "foobar" }, arguments);
		Assert.assertEquals("foobar", genericExporter.getTemplateName());
	}
	
	@Test
	public void testSetForEach() {
		genericExporterFacade.setForEach("foobar");
		Assert.assertEquals("setForEach", methodName);
		Assert.assertArrayEquals(new Object[] { "foobar" }, arguments);
	}
	
	@Test
	public void testGetFilePattern() {
		genericExporter.setFilePattern("foobar");
		reset();
		Assert.assertEquals("foobar", genericExporterFacade.getFilePattern());
		Assert.assertEquals("getFilePattern", methodName);
		Assert.assertArrayEquals(new Object[] {}, arguments);
	}
	
	@Test
	public void testGetTemplateName() {
		genericExporter.setTemplateName("foobar");
		reset();
		Assert.assertEquals("foobar", genericExporterFacade.getTemplateName());
		Assert.assertEquals("getTemplateName", methodName);
		Assert.assertArrayEquals(new Object[] {}, arguments);
	}
	
	private void reset() {
		methodName = null;
		arguments = null;
	}
	
}
