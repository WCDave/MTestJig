package gfxmain;

import java.awt.event.KeyEvent;


public enum KeyPadArrows {
	
		LEFT(KeyEvent.VK_NUMPAD4,KeyEvent.VK_LEFT), RIGHT(KeyEvent.VK_NUMPAD6,KeyEvent.VK_RIGHT),
		UP(KeyEvent.VK_NUMPAD8,KeyEvent.VK_UP), DOWN(KeyEvent.VK_NUMPAD2,KeyEvent.VK_DOWN), 
		NONE(0,0),TRIMDN(KeyEvent.VK_NUMPAD7,KeyEvent.VK_HOME), TRIMUP(KeyEvent.VK_NUMPAD1,KeyEvent.VK_END);
		
		private final int numLockValue, nonNumLockValue;
		
		KeyPadArrows(int numLock, int nonNumLock){
			numLockValue=numLock;
			nonNumLockValue=nonNumLock;
		}
		public static KeyPadArrows getDirection(int i){
			KeyPadArrows retVal=null;
			switch(i){
				case KeyEvent.VK_LEFT :
				case KeyEvent.VK_NUMPAD4 : retVal=LEFT;break;
				
				case KeyEvent.VK_RIGHT :
				case KeyEvent.VK_NUMPAD6 : retVal=RIGHT;break;
				
				case KeyEvent.VK_UP : 
				case KeyEvent.VK_NUMPAD8 : retVal=UP;break;
					
				case KeyEvent.VK_DOWN : 
				case KeyEvent.VK_NUMPAD2 : retVal=DOWN;break;
				
				default : retVal=NONE;
			}
			return retVal;
			
		}
		public int getNumLockValue() {
			return numLockValue;
		}
		public int getNonNumLockValue() {
			return nonNumLockValue;
		}
	}