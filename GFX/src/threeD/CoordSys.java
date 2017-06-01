package threeD;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;

import VMath.VMath;
import enums.AxisType;
import enums.DirectionTypes;
import gfxmain.GFXFramework;

public class CoordSys extends Observable implements Cloneable, Drawable3D {
	private UVector xAxis;
	private UVector yAxis;
	private UVector zAxis;
	private double x,y,z; //position of this coordinate system
	private double velX;
	private double velY;
	private double velZ;
	private double d = 800/1.1;
	
	private DirectionTypes viewDirection;	

public CoordSys(){
	this(0,0,0);
}
/**
 * 
 * @param xx X coordinate
 * @param yy Y coordinate
 * @param zz Z coordinate
 */
public CoordSys(double xx, double yy, double zz) {
	
	xAxis = new UVector(1,0,0,0, Color.RED, this, true);	
	yAxis = new UVector(0,1,0,0, Color.GREEN, this, true);	
	zAxis = new UVector(0,0,1,0, Color.CYAN, this, true);
	
	x=xx;y=yy;z=zz;
	viewDirection=DirectionTypes.FORWARD;
}

public UVector xAxis(){
	return xAxis;
}

public void setXAxis(double[] vec){
	xAxis.setAsVector(vec);
}

public void setYAxis(double[] vec){
	yAxis.setAsVector(vec);
}

public void setZAxis(double[] vec){
	zAxis.setAsVector(vec);
}

public UVector yAxis(){
	return yAxis;
}
public UVector zAxis(){
	return zAxis;
}

public void setX(float aValue){
	x=aValue;
	return;
}

public double X(){
	return x;
}

public void setY(double aValue){
	y=aValue;
	return;
}

public double Y(){
	return y;
}

public void setZ(double aValue){
	z=aValue;
	return;
}

public double Z(){
	return z;
}
/** Returns position vector for this coordinate system.
 * @return position vector of this coordinate system
 */
public double[] getPositionVec(){
    double[] aVec = new double[4];
	aVec[0] = x;
	aVec[1] = y;
	aVec[2] = z;
	return aVec;
}

/**
 * Sets position of this coordinate system
 * @param aVec position vector
 */
public void setPositionAsVec(double[] aVec){
	x=aVec[0];
	y=aVec[1];
	z=aVec[2];
	return;
}

/**
 * Translates the coordinate vectors of this coordinate system into a 3x3 matrix
 * @return coordinate system as a 3x3 matrix
 */
public double[][] getCoordSysAsMatrix(){
    double[][] aMatrix = {xAxis.getVectorForm(),yAxis.getVectorForm(),zAxis.getVectorForm()};
	return aMatrix;
}

/**
 * 
 * @param aVec  vector to transform into this coordinate system
 * @return transformed vector
 */
public double[] transformVec(double[] aVec){	
	return VMath.vecMultByMatrix(aVec, getCoordSysAsMatrix());
}

/**
 * Rotates coordinate system around X axis
 * @param angle  angle to rotate coordsystem around x axis
 */
public void xRotate(double angle) {
	QRotate(yAxis,xAxis,angle);
	QRotate(zAxis,xAxis,angle);
	return;
}

/**
 * Rotates coordinate system around Y axis
 * @param angle  angle to rotate coordsystem around y axis
 */
public void yRotate(double angle) {
	QRotate(xAxis,yAxis,-angle);
	QRotate(zAxis,yAxis,-angle);
	return;
}

/**
 * Rotates coordinate system around Z axis
 * @param angle  angle to rotate coordsystem around z axis
 */
public void zRotate(double angle) {
	QRotate(yAxis,zAxis,angle);
	QRotate(xAxis,zAxis,angle);
	return;
}

/**
 * 
 * @param vector 3D vector to translate into 2D screen position
 * @param d screen dimensions
 * @return Screenpoint giving 2D screen translation of 3D vector
 */
public ScreenPoint getScrnCoords(float[] vector, Dimension d){
	return this.getScrnCoords(vector[0],vector[1], vector[2], d);
}

/**
 * 
 * @param vector 3D vector to translate into 2D screen position
 * @param d screen dimensions
 * @return Screenpoint giving 2D screen translation of 3D vector
 */
public ScreenPoint getScrnCoords(double[] vector, Dimension d){
	return this.getScrnCoords(vector[0],vector[1], vector[2], d);
}

/**
 * 
 * @param xx x component of vector to translate to 2D screen point
 * @param yy y component of vector to translate to 2D screen point
 * @param zz z component of vector to translate to 2D screen point
 * @param dim screen dimensions
 * @return Screenpoint giving 2D screen translation of 3D vector
 */
public ScreenPoint getScrnCoords(double xx, double yy, double zz, Dimension dim){
    double [] vec1 = new double[4];
    double [] vec2 = new double[4];
	
    double scx = (double)dim.width/2;
    double scy = (double)dim.height/2;

	vec1[0]=-x+xx;
	vec1[1]=-y+yy;
	vec1[2]=-z+zz;
	
	switch(viewDirection){
		case FORWARD : vec2[0]=-xAxis.dotprd(vec1);
					   vec2[1]=yAxis.dotprd(vec1);
					   vec2[2]=zAxis.dotprd(vec1);
			           break;
		case AFT     : vec2[0]=xAxis.dotprd(vec1);
					   vec2[1]=yAxis.dotprd(vec1);
					   vec2[2]=-zAxis.dotprd(vec1);
					   break;
		case LEFT    : vec2[2]=-xAxis.dotprd(VMath.vecMultByScalar(vec1,-1));
					   vec2[1]=yAxis.dotprd(vec1);
					   vec2[0]=zAxis.dotprd(vec1);
					   break;
		case RIGHT   : vec2[2]=-xAxis.dotprd(vec1);
					   vec2[1]=yAxis.dotprd(vec1);
					   vec2[0]=-zAxis.dotprd(vec1);
					   break;
		case UP      : vec2[0]=-xAxis.dotprd(vec1);
				       vec2[2]=yAxis.dotprd(vec1);
				       vec2[1]=-zAxis.dotprd(vec1);
				       break;		
		case DOWN    : vec2[0]=-xAxis.dotprd(vec1);
		   			   vec2[2]=yAxis.dotprd(VMath.vecMultByScalar(vec1,-1));
		               vec2[1]=zAxis.dotprd(vec1);
	}
	
	int ix = 0;int iy = 0;
	if(vec2[2]>0) {
		float x=Math.round(d*vec2[0]/vec2[2]+scx);
		float y=Math.round(scy-d*vec2[1]/vec2[2]);
		ix=Math.abs(x)<Integer.MAX_VALUE?(int)x:(int)(Math.signum(x)*Integer.MAX_VALUE-1);
		iy=Math.abs(y)<Integer.MAX_VALUE?(int)y:(int)(Math.signum(y)*Integer.MAX_VALUE-1);
	}
	return new ScreenPoint(ix,iy,vec2[2]>0);
}

/**
 * 
 * @param axOfRot  axis to rotate coord system around
 * @param angle angle of rotation
 */
public void rotateAroundArbitraryAxis(double[] axOfRot, double angle){
	UVector arb = new UVector(axOfRot[0],axOfRot[1],axOfRot[2],0,null,false);
	QRotate(xAxis,arb,angle);
	QRotate(yAxis,arb,angle);
	QRotate(zAxis,arb,angle);
}


/**
 * Rotates a point around one of the axes of this coordinate system
 * @param axisType  which coordinate system axis to rotate around
 * @param aPoint the point to rotate
 * @param angle degrees to rotate the point
 * @return rotated point
 */
public double[] rotatePointAroundAxis(AxisType axisType, double[] aPoint, double angle){
	UVector uVec = new UVector(aPoint,null,false);
	switch(axisType){
		case X : QRotate(uVec, (UVector)xAxis.clone(), angle);break;
		case Y : QRotate(uVec, (UVector)yAxis.clone(), angle);break;
		case Z : QRotate(uVec, (UVector)zAxis.clone(), angle);
	}
	return uVec.getVectorForm();
}

//P' = P(N dot P)+cos(theta)(P-P(N dot P))+sin(theta)(N x P) 
/*given s,t number of tesselations in polar coordinates..  in other words, s=10,t=10 gives a 100 vertex sphere..  the number of triangles is 2(s-1)(t-1), if that helps
then the vertices are x=r sin phi cos theta, y = r sin phi sin theta, z = r cos phi, 
for an elastic collision, the impulse scalar j is j=(-(1+restitution)*V*N)/( ((I^-1(RxN))xR) + 1/mass )
V being the velocity, R being the point of collision in respect to center of mass*/

private void QRotate(UVector ax2r, UVector axOfRot, double angle) { //Quaternion rotations

    double [][] QR = new double[3][3];
    double [] AP = new double[4];
    double [] AV = new double[4];

    double s = Math.sin(-Math.toRadians(angle)/2);
    double w = Math.cos(-Math.toRadians(angle)/2);
    double x = s*axOfRot.X();
    double y = s*axOfRot.Y();
    double z = s*axOfRot.Z();	

	QR[0][0]=1-2*y*y-2*z*z;  QR[0][1]=2*x*y-2*w*z;   QR[0][2]=2*x*z+2*w*y;
	QR[1][0]=2*x*y+2*w*z;    QR[1][1]=1-2*x*x-2*z*z; QR[1][2]=2*y*z-2*w*x;
	QR[2][0]=2*x*z-2*w*y;    QR[2][1]=2*y*z+2*w*x;   QR[2][2]=1-2*x*x-2*y*y;

	AV=ax2r.getVectorForm();
	AP[0]=0;AP[1]=0;AP[2]=0;
	for (int i=0;i<3;i++)
		for (int j=0;j<3;j++) 
			AP[i]+=AV[j]*QR[i][j];
	ax2r.setAsVector(AP);
	setChanged();
	notifyObservers();
	return;
}

@Override
public Object clone(){
	CoordSys o=null;
	try {
        o=(CoordSys)super.clone();
        o.xAxis = (UVector)o.xAxis.clone();
        o.yAxis = (UVector)o.yAxis.clone();
        o.zAxis = (UVector)o.zAxis.clone();
		return o;
	} catch (CloneNotSupportedException e) {
		e.printStackTrace();
	}
	return o;
}

@Override
public boolean equals(Object o){
	CoordSys aSys = (CoordSys)o;
	return (aSys.xAxis.equals(xAxis) && aSys.yAxis.equals(yAxis) && aSys.zAxis.equals(zAxis)
			&& aSys.x==x && aSys.y==y && aSys.z==z);
}

/**
 * Sets view direction for this coordinate system
 * @param viewDirection
 */
public void setViewDirection(DirectionTypes viewDirection) {
	this.viewDirection = viewDirection;
}

/**
 * Retrieves current view direction
 * @return current viewing direction
 */
public DirectionTypes getViewDirection() {
	return viewDirection;
}

public void setD(double d) {
	this.d = d;
}

public double getD() {
	return d;
}

public void setVelX(double velX) {
	this.velX = velX;
}

public double getVelX() {
	return velX;
}

public void setVelY(double velY) {
	this.velY = velY;
}

public double getVelY() {
	return velY;
}

public void setVelZ(double velZ) {
	this.velZ = velZ;
}

public double getVelZ() {
	return velZ;
}

/**
 * Sets velocity for this coordinate system
 * @param aVec
 */
public void setVelocityAsVec(double[] aVec){
	velX=aVec[0];velY=aVec[1];velZ=aVec[2];
}
/**
 * returns velocity vector
 * @return vector containing velocity of this coordinate system
 */
public double[] getVelocityAsVec(){
	return new double[]{velX, velY, velZ,0};
}

/**
 * Translates this coordinate system along axis
 * @param axis axis of translation
 * @param distance distance to translate this coordinate system along the axis
 */
public void translate(AxisType axis, float distance){
	UVector transAxis=null;
	switch(axis){
		case Z : transAxis = zAxis;break;
		case Y : transAxis = yAxis;break;
		case X : transAxis = xAxis;
	}
	setPositionAsVec(VMath.vecAdd(getPositionVec(), 
			         VMath.vecMultByScalar(transAxis.getVectorForm(), distance)));
	setChanged();
	notifyObservers();
}

/**
 * Copies sysToCopy to this coordinate system
 * @param sysToCopy  Coordinate system to copy to this coordinate system.
 */
public void copy(CoordSys sysToCopy){
	velX=sysToCopy.velX;
	velY=sysToCopy.velY;
	velZ=sysToCopy.velZ;
	d = sysToCopy.d;
	viewDirection=sysToCopy.viewDirection;
	x=sysToCopy.x;
	y=sysToCopy.y;
	z=sysToCopy.z;
	xAxis=(UVector)sysToCopy.xAxis.clone();
	yAxis=(UVector)sysToCopy.yAxis.clone();
	zAxis=(UVector)sysToCopy.zAxis.clone();
}

public void draw(GFXFramework gfx, CoordSys cs) {
    xAxis.draw(gfx,cs);
    yAxis.draw(gfx,cs);
    zAxis.draw(gfx,cs);
	
}

/*public float[] getAngularVelociyVec(){
    return VMath.crossprd(this.getPositionVec(),this.getVelocityAsVec());
}*/
}