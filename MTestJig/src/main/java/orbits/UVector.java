package orbits;

import VMath.VMath;
import main.AbstractView;

import java.awt.*;
import java.io.Serializable;

public class UVector implements Cloneable, Serializable, Drawable3D {
  double x;
  double y;
  double z;
  //private final double[] underlyingVector = new double[]{0,0,0,0};
  private Color color;

  public UVector(double xx, double yy, double zz, Color c) {
    x = xx;
    y = yy;
    z = zz;
    //underlyingVector = new double[]{x,y,z,w};
    color = c;
  }

  public UVector(double xx, double yy, double zz) {
    this(xx, yy, zz, Color.BLACK);

  }

  public UVector(double[] aVec) {
    this(aVec[0], aVec[1], aVec[2]);
  }

  public double dotprd(double[] aVec) {
    return dotprd(aVec[0], aVec[1], aVec[2]);
    //return VMath.dotprod(aVec, underlyingVector);
  }

  public double dotprd(double xx, double yy, double zz) {
    return xx * x + yy * y + zz * z;
    //return xx*underlyingVector[0]+yy*underlyingVector[1]+zz*underlyingVector[2];
  }

  public double X() {
    return x;
  }

  public void setX(double aValue) {
    x = aValue;
    return;
  }

  public double Y() {
    return y;
  }

  public void setY(double aValue) {
    y = aValue;
    return;
  }

  public double Z() {
    return z;
  }

  public void setZ(double aValue) {
    z = aValue;
    return;
  }

  public double[] getVectorForm() {
    return new double[]{x, y, z};
  }

  public void setAsVector(double[] aVector) {
    x = aVector[0];
    y = aVector[1];
    z = aVector[2];
    //w=aVector[3];
        /* underlyingVector[0] = aVector[0];
        underlyingVector[1] = aVector[1];
        underlyingVector[2] = aVector[2];
        underlyingVector[3] = aVector[3];  */
    return;
  }

  public boolean equals(Object o) {
    UVector aUVec = (UVector) o;
    return (aUVec.x == x && aUVec.y == y && aUVec.z == z);
  }

  public Object clone() {
    UVector o = null;
    try {
      o = (UVector) super.clone();
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

  public void draw(double[] posVec, CoordSys refSys, Dimension d, Graphics2D g2, float length) {

    ScreenPoint anOriginVec = refSys.getScrnCoords(posVec, d.width, d.height);
    double[] anExtendVec = VMath.vecAdd(posVec, VMath.vecMultByScalar(getVectorForm(), length));
    if (anExtendVec != null) {
      ScreenPoint aResultVec = refSys.getScrnCoords(anExtendVec, d.width, d.height);
      if (aResultVec.isInView() && anOriginVec.isInView()) {
        g2.setColor(color);
        g2.drawLine(anOriginVec.x, anOriginVec.y, aResultVec.x, aResultVec.y);
      }
    }
  }

  @Override
  public void draw(AbstractView view) {
    CoordSys refSys = view.getCoordSys();
    double[] posVec = view.getCoordSys().getPositionVec();  //need to find posVec of object not this
    Dimension d = view.getViewCanvas().getScreenCenter();
    Graphics2D g2 = view.getViewCanvas().getG2();

    ScreenPoint anOriginVec = refSys.getScrnCoords(posVec, d.width, d.height);
    double[] anExtendVec = VMath.vecAdd(posVec, VMath.vecMultByScalar(getVectorForm(), 300));
    if (anExtendVec != null) {
      ScreenPoint aResultVec = refSys.getScrnCoords(anExtendVec, d.width, d.height);
      if (aResultVec.isInView() && anOriginVec.isInView()) {
        g2.setColor(color);
        g2.drawLine(anOriginVec.x, anOriginVec.y, aResultVec.x, aResultVec.y);
      }
    }
  }
}