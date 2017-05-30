package main;

import VMath.VMath;
import enums.AxisType;
import orbits.Abstract3DModelObject;
import orbits.CoordSys;
import orbits.Point3D;
import orbits.ScreenPoint;
import orbits.atmospherics.DefaultDragModel;
import orbits.atmospherics.IDragModel;
import org.apache.log4j.Logger;
import rocketComponents.CargoBay;
import rocketComponents.FuelTank;
import rocketComponents.RocketEngine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class Rocket extends Craft {

  private float fuelMass;
  private double throttleSetting = 0;
  private float engPower;
  private boolean tracking;
  private final float initialFuel;
  private RocketEngine engine;
  private List<FuelTank> fuelTankList;
  private CargoBay bay = new CargoBay(this);
  private AxisType rcsThrustAxis = AxisType.Z;
  private double rcsThrustValue = 0;
  private Logger log = Logger.getLogger(Rocket.class);

  private IDragModel dragModel = new DefaultDragModel(55);

  public static final float RCS_THRUST = 1000000;

  private Vector<Point3D> dataList = new Vector<Point3D>(3000);

  public Rocket(float emptyMass, float fuel, float aPowerRating) {
    fuelMass = fuel;
    initialFuel = fuel;
    fuelTankList = new ArrayList<FuelTank>(3);
    FuelTank tank = new FuelTank(fuel, 0);
    tank.setSelected(true);
    fuelTankList.add(tank);
    this.mass = emptyMass;
    this.engPower = aPowerRating;
    engine = new RocketEngine(aPowerRating, fuelTankList, this);
    coordSysDraw = true;
  }

  public void setFuelMass(float fuelMass) {
    this.fuelMass = fuelMass;
  }

  public float getFuelMass() {
    return fuelMass;
  }

  public double[] computeThrust(double dt) {
    double[] mainEngineThrust = VMath.vecMultByScalar(sSys.zAxis().getVectorForm(), engine.getThrust(dt));
    double[] rcsThrust = null;

    switch (rcsThrustAxis) {
      case X: {
        rcsThrust = VMath.vecMultByScalar(sSys.xAxis().getVectorForm(), rcsThrustValue);
        break;
      }
      case Y: {
        rcsThrust = VMath.vecMultByScalar(sSys.yAxis().getVectorForm(), rcsThrustValue);
        break;
      }
      case Z: {
        rcsThrust = VMath.vecMultByScalar(sSys.zAxis().getVectorForm(), rcsThrustValue);
      }
    }

    setThrust(VMath.vecAdd(mainEngineThrust, rcsThrust));
    //log.debug(VMath.mag(getThrust()) + " "+ VMath.mag(rcsThrust));
    return getThrust();
  }

  public void setThrottleSetting(double throttleSetting) {
    engine.setThrottleSetting(throttleSetting);
  }

  public double getThrottleSetting() {
    return throttleSetting;
  }


  @Override
  public void draw(AbstractView aView) {
    CoordSys cs = aView.getCoordSys();
    Dimension d = aView.getViewCanvas().getScreenCenter();

    ScreenPoint aVec = cs.getScrnCoords(sSys.getPositionVec(), d.width, d.height);
    if (aVec.isInView()) {
      Graphics2D g2 = aView.getViewCanvas().getG2();
      g2.setColor(Color.CYAN);
      g2.fillOval(aVec.x - 1, aVec.y - 1, 2, 2);
    }

    if (aView instanceof ExternalView) {
        super.draw(aView);
    }
  }

  public void setEngPower(float engPower) {
    this.engPower = engPower;
  }

  public float getEngPower() {
    return engPower;
  }

  public void setTracking(boolean tracking) {
    this.tracking = tracking;
  }

  public boolean isTracking() {
    return tracking;
  }


  @Override
  public Vector<Point3D> getDataList() {
    if (!tracking) {
      return null;
    }
    return dataList;
  }


  public float getInitialFuel() {
    return initialFuel;
  }

  /**
   * @param engine the engine to set
   */
  public void setEngine(RocketEngine engine) {
    this.engine = engine;
  }

  /**
   * @return the engine
   */
  public RocketEngine getEngine() {
    return engine;
  }

  public double getFuelRemaining() {
    double result = 0;
    for (FuelTank tank : fuelTankList) {
      result += tank.getFuelRemaining();
    }
    return result;
  }

  public float getMass() {
    float result = super.mass + (float) getFuelRemaining();
    return result;
  }


  public void loadBay(Craft craft) {
    bay.insertCraft(craft);
  }

  public Craft unloadBay(String name) {
    return bay.extractCraft(name);
  }

  public void setThrustAxis(AxisType axisType) {

  }

  public double[] getThrustAxis() {
    double[] result = sSys.zAxis().getVectorForm();
    return result;
  }

  public void setRcsThrustAxis(AxisType rcsThrustAxis) {
    this.rcsThrustAxis = rcsThrustAxis;
  }

  public AxisType getRcsThrustAxis() {
    return rcsThrustAxis;
  }

  public void setRcsThrustValue(double rcsThrustValue) {
    this.rcsThrustValue = rcsThrustValue;
  }

  public double getRcsThrustValue() {
    return rcsThrustValue;
  }

  @Override
  public double getDragFactor() {
    return dragModel.getDragFactor();
  }

  @Override
  public void groundBehavior(double[] vec1, double[] vec2, Abstract3DModelObject a3o) {
    super.groundBehavior(vec1, vec2, a3o);
    CoordSys tSys = VMath.createCoordSys(vec2, vec2, a3o.getCoordSys().yAxis().getVectorForm());
    getCoordSys().setZAxis(tSys.zAxis().getVectorForm());
//    getCoordSys().setYAxis(tSys.yAxis().getVectorForm());
//    getCoordSys().setXAxis(tSys.xAxis().getVectorForm());
  }
}
