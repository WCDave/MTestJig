package tests;

import java.awt.event.KeyEvent;
import threeD.CoordSys;
import threeD.SlewKeyNavAdapter;
import junit.framework.TestCase;


public class SlewTest extends TestCase {

	SlewKeyNavAdapter s;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		s = new SlewKeyNavAdapter(new CoordSys(),20);
	}

	public void testHandleKey() {
		assertTrue(s.handleKey(999)==999);
		assertTrue(s.handleKey(KeyEvent.VK_NUMPAD1)==0);
	}

	public void testIsTranslationMode() {
		assertFalse(s.isTranslationMode());
		s.handleKey(KeyEvent.VK_SLASH);
		assertTrue(s.isTranslationMode());
	}

}
