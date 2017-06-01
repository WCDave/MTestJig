package tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(SlewTest.class);
		//$JUnit-END$
		return suite;
	}

}
