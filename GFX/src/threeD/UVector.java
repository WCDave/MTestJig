package threeD;

import gfxmain.GFXFramework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import VMath.VMath;

public class UVector implements Cloneable, Drawable3D{
	double x;
    double y;
	double z;
	double w;
	private Color color;
	private CoordSys ownedBySys;
	private boolean drawArrows=true;

	public UVector(double xx, double yy, double zz, double ww, Color c, CoordSys ownedBy, boolean drawArrows) {
		x=xx;
		y=yy;
		z=zz;
		w=ww;
		color=c;
		ownedBySys = ownedBy;
		this.drawArrows=drawArrows;
	}
	
	public UVector(double xx, double yy, double zz, double ww, CoordSys sys, boolean drawArrow) {
		this(xx,yy,zz,ww,Color.BLACK, sys, drawArrow);
		
	}
	
	public UVector(double[] aVec, CoordSys sys, boolean drawArrow){
		this(aVec[0],aVec[1],aVec[2],aVec[3], sys, drawArrow);
	}
	
	public double dotprd(double[] aVec){
		return dotprd(aVec[0],aVec[1],aVec[2]);
	}
	public double dotprd(double xx, double yy, double zz) {
		return xx*x+yy*y+zz*z; 
	}
	
	public double X(){
		return x;
	}
	public void setX(double aValue){
		x=aValue;
		return;
	}
	public double Y(){
		return y;
	}
	public void setY(double aValue){
		y=aValue;
		return;
	}
	public double Z(){
		return z;
	}
	public void setZ(double aValue){
		z=aValue;
		return;
	}
	public double[] getVectorForm(){
		double[] aVec={x,y,z,w};
		return aVec;
	}
	public void setAsVector(double[] aVector){
		x=aVector[0];
		y=aVector[1];
		z=aVector[2];
		w=aVector[3];
		return;
	}
	public boolean equals(Object o){
		UVector aUVec = (UVector)o;
		return (aUVec.x==x && aUVec.y==y && aUVec.z==z && aUVec.w==w);
	}
    public Object clone(){
        UVector o=null;
        try {
            o=(UVector)super.clone();
            //return o;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
    
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}


	public void draw(GFXFramework gfx, CoordSys cs) {
		double[] posVec = ownedBySys.getPositionVec();
		Graphics2D g2 = gfx.getG2();
		Dimension d = gfx.getSize();
		ScreenPoint anOriginVec=cs.getScrnCoords(posVec,d);    
        double[] anExtendVec = VMath.vecAdd(posVec, VMath.vecMultByScalar(getVectorForm(),5));
        if(anExtendVec!=null) {
        	ScreenPoint aResultVec = cs.getScrnCoords(anExtendVec,d);
	        if(aResultVec.isInView() && anOriginVec.isInView()) {
			    g2.setColor(color);
			    g2.drawLine(anOriginVec.x,anOriginVec.y,aResultVec.x,aResultVec.y);
	        }
        }
		
	}

}