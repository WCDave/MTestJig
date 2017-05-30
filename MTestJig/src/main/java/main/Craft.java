package main;

import VMath.VMath;
import main.effects.model.ILifeSpanLimited;
import orbits.Abstract3DModelObject;
import orbits.CoordSys;
import orbits.Plottable;
import orbits.Point3D;
import orbits.atmospherics.IAeroDynamics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Craft extends Abstract3DModelObject implements Plottable, IAeroDynamics, ILifeSpanLimited {


  protected float mass = 0;

  private double[] grav = new double[3];
  private double[] thrust = new double[3];
  private double[] drag = new double[3];
  private double[] norm = new double[3];
  private double[] relativeWind = new double[3];
  private List<Thread> behaviorThreads = new ArrayList<Thread>();

  protected float rollRate;
  protected float pitchRate;
  protected float yawRate;

  private float mach;
  private float TAT;

  private boolean autoPilotInhibit;


  public List<Thread> getBehaviorThreads() {
    return behaviorThreads;
  }

  public void setPositionVec(double[] aVec) {
    sSys.setPositionAsVec(aVec);
  }

  public void setVelocityVec(double[] velocityVec) {
    sSys.setVelocityAsVec(velocityVec);
  }

  public void setCoordSys(CoordSys navObject) {
    this.sSys = navObject;
  }

  public void setMass(float mass) {
    this.mass = mass;
  }

  public float getMass() {
    return mass;
  }

  public void applyAttitudeRates(double dt) {
    if (!autoPilotInhibit) {
      if (pitchRate != 0)
        sSys.xRotate(pitchRate * dt);

      if (yawRate != 0)
        sSys.yRotate(yawRate * dt);

      if (rollRate != 0)
        sSys.zRotate(rollRate * dt);
    }
  }

  public void adjustRollRate(float rollRate) {
    this.rollRate += rollRate;
  }

  public float getRollRate() {
    return rollRate;
  }

  public void adjustPitchRate(float pitchRate) {
    this.pitchRate += pitchRate;
  }

  public void setPitchRate(float rate) {
    pitchRate = rate;
  }

  public float getPitchRate() {
    return pitchRate;
  }

  public void adjustYawRate(float yawRate) {
    this.yawRate += yawRate;
  }

  public float getYawRate() {
    return yawRate;
  }

  public void nullRates() {
    rollRate = 0;
    yawRate = 0;
    pitchRate = 0;
  }


  public void setGrav(double[] grav) {
    this.grav = grav;
  }

  public double[] getGrav() {
    return grav;
  }

  public void setThrust(double[] thrust) {
    this.thrust = thrust;
  }

  public double[] getThrust() {
    return thrust;
  }

  public void setDrag(double[] drag) {
    this.drag = drag;
  }

  public double[] getDrag() {
    return drag;
  }

  public void setNorm(double[] norm) {
    this.norm = norm;
  }

  public double[] getNorm() {
    return norm;
  }

  public void setAutoPilotInhibit(boolean apInhibit) {
    this.autoPilotInhibit = apInhibit;
  }

  @Override
  public void draw(AbstractView view) {
    sSys.draw(view);
  }

  @Override
  public void set3DObjectForDraw(AbstractView view) {

    if (getDataList() != null) {
      List<Point3D> aList = new ArrayList<Point3D>(getDataList());
      for (Point3D point : aList) {
        point.set3DObjectForDraw(view);
      }
    }

    setVectorFromView(VMath.vecSubtract(sSys.getPositionVec(), view.getCoordSys().getPositionVec()));
    super.set3DObjectForDraw(view);
  }


  @Override
  public double[] getPosition() {
    return sSys.getPositionVec();
  }

  @Override
  public double[] getVelocity() {
    return sSys.getVelocityAsVec();
  }

  @Override
  public void updatePosition(Date date, double[] newPosition,
                             double[] currentPosition) {
    // TODO Auto-generated method stub

  }

  public double[] getRelativeWind() {
    return relativeWind;
  }

  public void setRelativeWind(double[] relWind) {
    relativeWind = relWind;
  }

  public float getMach() {
    return mach;
  }

  public void setMach(float mach) {
    this.mach = mach;
  }

  public float getTAT() {
    return TAT;
  }

  public void setTAT(float TAT) {
    this.TAT = TAT;
  }

  public void groundBehavior(double[] velVec, double[] planToBodyVec, Abstract3DModelObject a3o) {
    setVelocityVec(velVec);
    double[] launchVec = VMath.vecAdd(thrust, grav);
    norm = new double[]{0,0,0};
    if (VMath.dotprod(launchVec, VMath.normalize(planToBodyVec)) < 0 ) {
      relativeWind = new double[]{0,0,0};
      norm = VMath.vecMultByScalar(launchVec, -1);
    }
  }

  public boolean decrementTimeToLive() {
    return false;
  }
}
