package org.frans.thesis.test;

import static org.junit.Assert.*;

import org.frans.thesis.start.CFApplication;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mt4j.util.math.Vector3D;

public class CFComponentTest {
	
	CFComponentNotAbstract component;
	CFSceneNotAbstract scene;
	CFApplication application;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		application = new CFApplicationNotABstract();
		scene = new CFSceneNotAbstract(application, "testscene");
		component = new CFComponentNotAbstract(scene);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConstructor() {
		assertEquals(scene, component.getCFScene());
		assertEquals(component.getComponentMenu(), null);
	}
	
	@Test
	public void testResize() {
		float width = component.getWidth();
		float height = component.getHeight();
		assertEquals(width, 100, 0);
		assertEquals(height, 100, 0);
		
		component.scaleImage(3);
		assertEquals(width, 300, 0);
		assertEquals(height, 300, 0);
	}
	
	@Test
	public void testReposition(){
		Vector3D position = new Vector3D(123, 456);
		component.reposition(position);
		assertEquals(position, component.getPosition());
	}

}
