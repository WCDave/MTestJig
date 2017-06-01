package threeD;

import gfxmain.GFXFramework;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import VMath.VMath;


public class Plane3D implements Drawable3D {
	
	private float gridSize=25;
	private CoordSys planeCoordSys;
	private Color color;
	private float planeSize;
	private List<double[]> gridListA, gridListB;
	
	public Plane3D(CoordSys cs, float planeRange, Color c, int gridSize)  {
		planeCoordSys = cs;
		color=c==null?new Color(80,80,80):c;
		this.gridSize=gridSize;
		this.planeSize = planeRange;
		setPlane();
	}	
	

	public void draw(GFXFramework gfx, CoordSys cs) {
		Graphics2D g2 = gfx.getG2();
		g2.setColor(color);
		
		drawGridLines(gfx, cs, g2, gridListA);		
		drawGridLines(gfx, cs, g2, gridListB);
	}	
	

	private void drawGridLines(GFXFramework gfx, CoordSys cs, Graphics2D g2, List<double[]> aList) {
		double[] from=null;
		Iterator<double[]> it = aList.iterator();
		while(it.hasNext()) {
			double[] to = it.next();
			if(to!=null){
				if(from!=null) {
					ScreenPoint p2 = cs.getScrnCoords(to, gfx.getSize());
					ScreenPoint p1 = cs.getScrnCoords(from, gfx.getSize());
					if(p1.isInView() && p2.isInView()) {
						g2.drawLine(p1.x, p1.y, p2.x, p2.y);
					}
				}
			}
			from=to;
		}
	}
	
	private void setPlane(){
		gridListA = new ArrayList<double[]>();
		gridListB = new ArrayList<double[]>();
		int range = (int)(planeSize/2);
		for(int i=-range;i<range;i+=gridSize){
			for(int j=-range;j<range;j+=gridSize){
				double[] point = VMath.vecAdd(planeCoordSys.getPositionVec(), planeCoordSys.transformVec(new double[]{j,i,0}));
				gridListA.add(point);
				point = VMath.vecAdd(planeCoordSys.getPositionVec(), planeCoordSys.transformVec(new double[]{i,j,0}));
				gridListB.add(point);
			}
			gridListA.add(null);
			gridListB.add(null);
		}
	}
}
