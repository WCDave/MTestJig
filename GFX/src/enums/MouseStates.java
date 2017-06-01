package enums;

import java.awt.Point;

public enum MouseStates {

	CLICKED,DRAGGED,ENTERED,EXITED,MOVED,PRESSED,RELEASED,NONE;
	
	private Point location;
	private boolean stateChanged;
	
	public void setMousePositionStatus(int x, int y, boolean status){
		location=new Point(x,y);
		stateChanged = true;
	}
	public Point getMousePosition(){
		return location;
	}
	public boolean isStateChange(){
		boolean result=stateChanged;
		stateChanged=false;
		return result;
	}
}
