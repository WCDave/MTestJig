package main.effects.model;

import VMath.VMath;
import main.AbstractView;
import main.Craft;
import orbits.Abstract3DModelObject;
import orbits.CoordSys;
import orbits.Facet;
import orbits.Point3D;
import orbits.atmospherics.DefaultDragModel;
import orbits.atmospherics.IDragModel;
import org.apache.commons.math3.util.FastMath;

import java.awt.*;
import java.util.Date;
import java.util.Vector;

public class Particle extends Craft {

  private IDragModel dragModel = new DefaultDragModel(10);

  public Abstract3DModelObject getImpl() {
    return impl;
  }

  private Abstract3DModelObject impl;
  private long millisecondLifeSpan;

  public Particle(float mass, Abstract3DModelObject impl, long millisecondLifeSpan) {
    this.mass = mass;
    this.impl = impl;
    this.millisecondLifeSpan = millisecondLifeSpan;
  }

  @Override
  public double getDragFactor() {
    return dragModel.getDragFactor();
  }

  @Override
  public void applyAttitudeRates(double dt) {
      if (pitchRate != 0)
        impl.getCoordSys().xRotate(pitchRate * dt);

      if (yawRate != 0)
        impl.getCoordSys().yRotate(yawRate * dt);

      if (rollRate != 0)
        impl.getCoordSys().zRotate(rollRate * dt);
  }

  @Override
  public void updatePosition(Date date, double[] newPosition, double[] currentPosition) {

  }

  @Override
  public void draw(AbstractView view) {
    this.impl.draw(view);
  }

  @Override
  public void setPositionVec(double[] vec) {
    impl.getCoordSys().setPositionAsVec(vec);
    super.setPositionVec(vec);
  }

  @Override
  public void set3DObjectForDraw(AbstractView view) {
    if(System.currentTimeMillis()%150 == 0) grayFade();

    if(impl instanceof Facet) {
      impl.setVectorFromView(VMath.vecAdd(view.getObjectVectorFromView(), ((Facet) impl).mv()));
      java.util.List<double[]> vecs = ((Facet) impl).getVectorList();
      for (double[] vec : vecs) {
        double[] tVec = impl.getCoordSys().transformVec(vec);
        vec[0] = tVec[0];
        vec[1] = tVec[1];
        vec[2] = tVec[2];
      }
    }
    super.set3DObjectForDraw(view);
  }

  private void grayFade() {
    Color c = impl.getColor();
    impl.setColor(new Color((int) Math.max(c.getRed() * .7, 32), (int) Math.max(c.getGreen() * .7, 32), (int) Math.min(c.getBlue() * 1.3, 32)));
  }

  @Override
  public Vector<Point3D> getDataList() {
    return null;
  }

  @Override
  public boolean decrementTimeToLive() {
    return millisecondLifeSpan-- <= 0;
  }

  @Override
  public void groundBehavior(double[] velVec, double[] planToBodyVec, Abstract3DModelObject a3o) {
    CoordSys tSys = VMath.createCoordSys(planToBodyVec, getPosition(), null);
    tSys.zRotate(FastMath.random()*-360);
    setVelocityVec(VMath.vecSubtract(VMath.vectorRandomizer(VMath.vecMultByScalar(tSys.xAxis().getVectorForm(), VMath.mag(getVelocity())), .2f),velVec));
  }
}
