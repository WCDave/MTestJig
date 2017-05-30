package orbits;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.concurrent.locks.ReentrantLock;

import main.AbstractView;
import VMath.VMath;
import enums.AxisType;
import enums.DirectionTypes;
import org.apache.commons.math3.util.FastMath;

public class CoordSys implements Cloneable, Drawable3D, Serializable {
  private UVector xAxis;
  private UVector yAxis;
  private UVector zAxis;
  private double x, y, z; //position of this coordinate system
  private double[] velocityVec = new double[3];
  private final static double D2R = FastMath.PI / 180;
  //private static final int MAX_INT_MINUS_1 = Integer.MAX_VALUE-1;
  private static final double D2R_DIVIDED_BY_2 = -D2R / 2;
  private double d = 800 / FastMath.pow(1.1, 5);
  private double[][] inverseMatrix;

  private DirectionTypes viewDirection;

  public CoordSys() {       //$327/ 1800 289 7704 option 8 x 3971568
    this(0, 0, 0);
  }

  public CoordSys(double xx, double yy, double zz) {

    xAxis = new UVector(1, 0, 0, Color.RED);
    yAxis = new UVector(0, 1, 0, Color.GREEN);
    zAxis = new UVector(0, 0, 1, Color.CYAN);
    inverseMatrix = new double[][]{{1,0,0}, {0,1,0}, {0,0,1}};

    x = xx;
    y = yy;
    z = zz;
    viewDirection = DirectionTypes.FORWARD;
  }

  public UVector xAxis() {
    return xAxis;
  }

  public void setXAxis(double[] vec) {
    xAxis.setAsVector(vec);
  }

  public void setYAxis(double[] vec) {
    yAxis.setAsVector(vec);
  }

  public void setZAxis(double[] vec) {
    zAxis.setAsVector(vec);
  }

  public UVector yAxis() {
    return yAxis;
  }

  public UVector zAxis() {
    return zAxis;
  }

  public void setX(double aValue) {
    x = aValue;
    return;
  }

  public double X() {
    return x;
  }

  public void setY(double aValue) {
    y = aValue;
    return;
  }

  public double Y() {
    return y;
  }

  public void setZ(double aValue) {
    z = aValue;
    return;
  }

  public double Z() {
    return z;
  }

  public double[] getPositionVec() {
    return new double[]{x, y, z};
  }

  public void setPositionAsVec(double[] aVec) {
    x = aVec[0];
    y = aVec[1];
    z = aVec[2];
    return;
  }

  public double[][] getInverseMatrix() {
    return inverseMatrix;
  }

  public void setInverseMatrix(double[][] inverseMatrix) {
    this.inverseMatrix = inverseMatrix;
  }


  public double[][] getCoordSysAsMatrix() {
    double[][] aMatrix = {xAxis.getVectorForm(), yAxis.getVectorForm(), zAxis.getVectorForm()};
    return aMatrix;
  }

  public double[] transformVec(double[] aVec) {
    double[] resultVec = {0, 0, 0};
    double[][] aMatrix = getCoordSysAsMatrix();
    for (int i = 0; i < 3; i++)
      for (int j = 0; j < 3; j++)
        resultVec[i] = resultVec[i] + aMatrix[j][i] * aVec[j];
    return resultVec;
  }

  public void xRotate(double angle) {
    QRotate(yAxis, xAxis, angle);
    QRotate(zAxis, xAxis, angle);
    return;
  }

  public void yRotate(double angle) {
    QRotate(xAxis, yAxis, angle);
    QRotate(zAxis, yAxis, angle);
    return;
  }

  public void zRotate(double angle) {
    QRotate(yAxis, zAxis, angle);
    QRotate(xAxis, zAxis, angle);
    return;
  }

  public double[] getVectorForViewDirection() {
    switch (viewDirection) {
      case FORWARD:
        return zAxis.getVectorForm();
      case AFT:
        return VMath.vecMultByScalar(zAxis.getVectorForm(), -1);
      case RIGHT:
        return xAxis.getVectorForm();
      case LEFT:
        return VMath.vecMultByScalar(xAxis.getVectorForm(), -1);
      case UP:
        return yAxis.getVectorForm();
      case DOWN:
        return VMath.vecMultByScalar(yAxis.getVectorForm(), -1);
      default:
        return null;
    }

  }

  public ScreenPoint getScrnCoords(double[] vector, int screenX, int screenY) {
    return this.getScrnCoords(vector[0], vector[1], vector[2], screenX, screenY);
  }

  public ScreenPoint getScrnCoords(double xx, double yy, double zz, int screenX, int screenY) {
    double[] vec1 = {-x + xx, -y + yy, -z + zz};
    double[] vec2 = new double[3];
    //int [] outval = null;
//    double scx = (double)screenX/2;
//    double scy = (double)screenY/2;
    //outval[0]=0;outval[1]=0;

//	vec1[0]=-x+xx;
//	vec1[1]=-y+yy;
//	vec1[2]=-z+zz;

    switch (viewDirection) {
      case FORWARD:
        vec2[0] = -xAxis.dotprd(vec1);
        vec2[1] = yAxis.dotprd(vec1);
        vec2[2] = zAxis.dotprd(vec1);
        break;
      case AFT:
        vec2[0] = xAxis.dotprd(vec1);
        vec2[1] = yAxis.dotprd(vec1);
        vec2[2] = -zAxis.dotprd(vec1);
        break;
      case LEFT:
        vec2[2] = -xAxis.dotprd(VMath.vecMultByScalar(vec1, -1));
        vec2[1] = yAxis.dotprd(vec1);
        vec2[0] = zAxis.dotprd(vec1);
        break;
      case RIGHT:
        vec2[2] = -xAxis.dotprd(vec1);
        vec2[1] = yAxis.dotprd(vec1);
        vec2[0] = -zAxis.dotprd(vec1);
        break;
      case UP:
        vec2[0] = -xAxis.dotprd(vec1);
        vec2[2] = yAxis.dotprd(vec1);
        vec2[1] = -zAxis.dotprd(vec1);
        break;
      case DOWN:
        vec2[0] = -xAxis.dotprd(vec1);
        vec2[2] = yAxis.dotprd(VMath.vecMultByScalar(vec1, -1));
        vec2[1] = zAxis.dotprd(vec1);
    }

    //if(vec2[2]>0) {
//		float x=Math.round(d*vec2[0]/vec2[2]+scx);
//		float y=Math.round(scy-d*vec2[1]/vec2[2]);
//		int ix=Math.abs(x)<Integer.MAX_VALUE?(int)x:(int)(Math.signum(x)*MAX_INT_MINUS_1);
//		int iy=Math.abs(y)<Integer.MAX_VALUE?(int)y:(int)(Math.signum(y)*MAX_INT_MINUS_1);
    //outval = new int[]{ix,iy};

    //}
    int ix = (int) (d * vec2[0] / vec2[2] + screenX);
    int iy = (int) (screenY - d * vec2[1] / vec2[2]);
    return new ScreenPoint(ix, iy, vec2[2] > 0);
  }

  public void rotateAroundArbitraryAxis(double[] axOfRot, double angle) {
    UVector arb = new UVector(axOfRot[0], axOfRot[1], axOfRot[2]);
    QRotate(xAxis, arb, angle);
    QRotate(yAxis, arb, angle);
    QRotate(zAxis, arb, angle);
  }

  public double[] rotatePointAroundAxis(AxisType axisType, double[] aPoint, double angle) {

    UVector uVec = new UVector(aPoint);
    switch (axisType) {
      case X:
        QRotate(uVec, new UVector(this.xAxis.getVectorForm()), angle);
        break;
      case Y:
        QRotate(uVec, new UVector(this.yAxis.getVectorForm()), angle);
        break;
      case Z:
        QRotate(uVec, new UVector(this.zAxis.getVectorForm()), angle);
    }
    return uVec.getVectorForm();
  }

//P' = P(N dot P)+cos(theta)(P-P(N dot P))+sin(theta)(N x P) 
/*given s,t number of tesselations in polar coordinates..  in other words, s=10,t=10 gives a 100 vertex sphere..  the number of triangles is 2(s-1)(t-1), if that helps
then the vertices are x=r sin phi cos theta, y = r sin phi sin theta, z = r cos phi, 
for an elastic collision, the impulse scalar j is j=(-(1+restitution)*V*N)/( ((I^-1(RxN))xR) + 1/mass )
V being the velocity, R being the point of collision in respect to center of mass*/

  private void QRotate(UVector ax2r, UVector axOfRot, double angle) { //Quaternion rotations

    double[][] QR = new double[3][3];
    double[] AP = new double[]{0, 0, 0};

    double s = FastMath.sin(angle * D2R_DIVIDED_BY_2);
    double w = FastMath.cos(angle * D2R_DIVIDED_BY_2);

//    double s = Math.sin(angle * D2R_DIVIDED_BY_2);
//    double w = Math.cos(angle * D2R_DIVIDED_BY_2);
    double x = s * axOfRot.X();
    double y = s * axOfRot.Y();
    double z = s * axOfRot.Z();

    QR[0][0] = 1 - 2 * y * y - 2 * z * z;
    QR[0][1] = 2 * x * y - 2 * w * z;
    QR[0][2] = 2 * x * z + 2 * w * y;
    QR[1][0] = 2 * x * y + 2 * w * z;
    QR[1][1] = 1 - 2 * x * x - 2 * z * z;
    QR[1][2] = 2 * y * z - 2 * w * x;
    QR[2][0] = 2 * x * z - 2 * w * y;
    QR[2][1] = 2 * y * z + 2 * w * x;
    QR[2][2] = 1 - 2 * x * x - 2 * y * y;

    double[] AV = ax2r.getVectorForm();
    for (int i = 0; i < 3; i++)
      for (int j = 0; j < 3; j++)
        AP[i] += AV[j] * QR[i][j];
    ax2r.setAsVector(AP);
    return;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = xAxis.hashCode();
    result = 31 * result + yAxis.hashCode();
    result = 31 * result + zAxis.hashCode();
    temp = Double.doubleToLongBits(x);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(z);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public Object clone() {
    CoordSys o = null;
    try {

      o = (CoordSys) super.clone();
      o.xAxis = (UVector) o.xAxis.clone();
      o.yAxis = (UVector) o.yAxis.clone();
      o.zAxis = (UVector) o.zAxis.clone();
      o.velocityVec = new double[]{velocityVec[0], velocityVec[1], velocityVec[2]};
      return o;
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return o;
  }

  @Override
  public boolean equals(Object o) {
    CoordSys aSys = (CoordSys) o;
    return (aSys.xAxis.equals(xAxis) && aSys.yAxis.equals(yAxis) && aSys.zAxis.equals(zAxis)
            && aSys.x == x && aSys.y == y && aSys.z == z);
  }

  public void setViewDirection(DirectionTypes viewDirection) {
    this.viewDirection = viewDirection;
  }

  public double[] getViewAxis() {
    switch (viewDirection) {
      case FORWARD:
        return zAxis().getVectorForm();
      case AFT:
        return VMath.vecMultByScalar(zAxis.getVectorForm(), -1);
      case LEFT:
        return xAxis.getVectorForm();
      case RIGHT:
        return VMath.vecMultByScalar(xAxis.getVectorForm(), 1);
      case UP:
        return yAxis.getVectorForm();
      case DOWN:
        return VMath.vecMultByScalar(yAxis.getVectorForm(), 1);
    }
    return null;
  }


  public void setD(double d) {
    this.d = d;
  }

  public double getD() {
    return d;
  }

  public void setVelocityAsVec(double[] aVec) {

    velocityVec = aVec;
  }

  public double[] getVelocityAsVec() {
    return velocityVec;
  }

  @Override
  public void draw(AbstractView view) {
    Graphics2D g2 = view.getViewCanvas().getG2();
    CoordSys cs = view.getCoordSys();
    Dimension d = view.getViewCanvas().getScreenCenter();
    double[] posVec = getPositionVec();
    xAxis.draw(posVec, cs, d, g2, 300);
    yAxis.draw(posVec, cs, d, g2, 300);
    zAxis.draw(posVec, cs, d, g2, 300);

  }



/*public float[] getAngularVelociyVec(){
    return VMath.crossprd(this.getPositionVec(),this.getVelocityAsVec());
}*/
}