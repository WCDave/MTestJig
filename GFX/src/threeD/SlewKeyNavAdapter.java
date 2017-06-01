package threeD;

import java.awt.event.KeyEvent;

import enums.AxisType;

public class SlewKeyNavAdapter extends KeyNav3DAdapter  {	

	private boolean translationMode;
	private float translationRate;
	public SlewKeyNavAdapter(CoordSys cs, float transRate) {
		super(cs);
		translationRate=transRate;
	}

@Override
	public int handleKey(int key) {	
		int retVal=0;
		if(translationMode){
			switch (key) {
				case KeyEvent.VK_NUMPAD1 : {
					cs.translate(AxisType.Z, -translationRate);break;
				}
				case KeyEvent.VK_NUMPAD7 : {
					cs.translate(AxisType.Z, translationRate);break;
				}
				case KeyEvent.VK_NUMPAD2 : {
					cs.translate(AxisType.Y, -translationRate);break;
				}
				case KeyEvent.VK_NUMPAD8 : {
					cs.translate(AxisType.Y, translationRate);break;
				}
				case KeyEvent.VK_NUMPAD4 : {
					cs.translate(AxisType.X, translationRate);break;
				}
				case KeyEvent.VK_NUMPAD6 : {
					cs.translate(AxisType.X, -translationRate);break;
				}
				default : {retVal=key;}
			}
		}
		else {
			retVal=super.handleKey(key);
		}
		if(retVal!=0) {
			retVal=0;
			switch (key) {
				case KeyEvent.VK_SLASH : {
					translationMode = !translationMode;break;
				}
				case KeyEvent.VK_SEMICOLON : {
					cs.copy(initNavObject);break;
				}
				default : retVal=key;
			}
		}
		return retVal;
	}

	public boolean isTranslationMode() {
		return translationMode;
	}

}
