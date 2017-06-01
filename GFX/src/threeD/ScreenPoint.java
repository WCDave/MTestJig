package threeD;

import java.awt.Point;

public class ScreenPoint extends Point {
	
	private boolean isInView; 

/**
 * Constructs a point 
 * @param x X position on screen
 * @param y Y position on screen
 * @param inView indicates whether point is in view
 */
	public ScreenPoint(int x, int y, boolean inView){
		super(x,y);
		isInView=inView;
	}

/**
 * Returns true if this point is in view
 * @return true or false
 */
	public boolean isInView() {
		return isInView;
	}

}
