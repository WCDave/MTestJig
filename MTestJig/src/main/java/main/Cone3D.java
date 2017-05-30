package main;

import VMath.VMath;
import gfxmain.GFXException;
import orbits.Abstract3DModelObject;
import orbits.Composed3DObject;
import orbits.CoordSys;
import orbits.DefaultFaceDetailer;
import orbits.Facet;
import orbits.IDetailer;
import org.apache.commons.math3.util.FastMath;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Cone3D extends Abstract3DModelObject implements Composed3DObject<Facet> {

  private float radius;
  private float height;
  private List<Facet> faceList;
  private IDetailer<Facet> detailer;

  /**
   * Defines a 3D Cone
   *
   * @param radius radius of the base of the cone
   * @param height height of the cone
   * @param color  color of the cone
   * @throws gfxmain.GFXException if cs is null (detected in super class)
   */
  public Cone3D(CoordSys cs, float radius, float height, Color color) throws GFXException {
    super(cs, color);
    this.radius = radius;
    this.height = height;
    drawFaceOutlines = true;
    //super.setColorModel3D(new DefaultColorModel3D(color));
    buildCone1();
  }

  public void buildCone1() {
    faceList = new ArrayList<Facet>();
    List<double[]> basePointList = new ArrayList<double[]>();
    double[] aPoint = null;
    ;
    for (int i = 0; i < 360; i += 30) {
      List<double[]> pointList = new ArrayList<double[]>();
      double ang1 = FastMath.toRadians(i);
      double ang2 = FastMath.toRadians(i + 30);
      aPoint = VMath.vecAdd(sSys.getPositionVec(), sSys.transformVec(new double[]{radius * FastMath.cos(ang1), radius * FastMath.sin(ang1), 0}));
      pointList.add(aPoint);
      basePointList.add(aPoint);
      aPoint = VMath.vecAdd(sSys.getPositionVec(), sSys.transformVec(new double[]{radius * FastMath.cos(ang2), radius * FastMath.sin(ang2), 0}));
      pointList.add(aPoint);
      basePointList.add(aPoint);
      aPoint = VMath.vecAdd(sSys.getPositionVec(), sSys.transformVec(new double[]{0, 0, height}));
      pointList.add(aPoint);
      //try {
      faceList.add(new Facet(pointList, this));
      //} catch (GFXException e) {
      //	System.out.println(e.getMessage());
      //	e.printStackTrace();
      //}
    }

    //try {
    Facet aFace = null;
    aFace = new Facet(basePointList, this);
    double[] mv = aFace.mv();
    if (VMath.dotprod(mv, aPoint) > 0) {
      aFace.setMv(VMath.vecMultByScalar(mv, -1));
    }
    faceList.add(aFace);
    //} catch (GFXException e) {
    //	System.out.println(e.getMessage());
    //	e.printStackTrace();
    //}
  }

  @Override
  public void set3DObjectForDraw(AbstractView view
  ) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<Facet> getFaceList() {
    // TODO Auto-generated method stub
    return faceList;
  }

  @Override
  public void draw(AbstractView view) {
    // TODO Auto-generated method stub

  }


  @Override
  public IDetailer getDetailer() {
    if (detailer == null) {
      detailer = new DefaultFaceDetailer(11);
    }
    return detailer;
  }
}
