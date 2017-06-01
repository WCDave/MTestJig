package threeD;

import gfxmain.KeyHandler;

import java.awt.event.KeyEvent;

public class KeyNav3DAdapter implements KeyHandler {
	
	protected CoordSys cs;
	protected final CoordSys initNavObject;

	public KeyNav3DAdapter(CoordSys cs){
		this.cs = cs;
		initNavObject = (CoordSys)cs.clone();
	}
	
	public int handleKey(int key){
		int retVal=0;
		switch (key) {
			case KeyEvent.VK_NUMPAD2 : {
				cs.xRotate(1);break;
			}
			case KeyEvent.VK_NUMPAD8 : {
				cs.xRotate(-1);break;
			}
			case KeyEvent.VK_NUMPAD4 : {
				cs.zRotate(1);break;
			}
			case KeyEvent.VK_NUMPAD6 : {
				cs.zRotate(-1);break;
			}
			case KeyEvent.VK_NUMPAD1 : {
				cs.yRotate(1);break;
			}
			case KeyEvent.VK_NUMPAD3 : {
				cs.yRotate(-1);break;
			}
			default : retVal=key;
		}
		return retVal;
	}
}
