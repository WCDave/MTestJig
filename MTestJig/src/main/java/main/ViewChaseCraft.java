package main;

import VMath.VMath;
import orbits.CoordSys;
import orbits.Point3D;

import java.util.Vector;


public class ViewChaseCraft extends Craft {

  private final Craft craftToChase;
  private final AbstractView view;
  public static final String CHASE_NAME = "CHASE";


  public ViewChaseCraft(Craft craftToChase, AbstractView view) {
    this.craftToChase = craftToChase;
    this.name = CHASE_NAME;
    this.view = view;
  }

  @Override
  public void applyAttitudeRates(double dt) {

  }

  @Override
  public double getDragFactor() {
    return 0;
  }

  @Override
  public Vector<Point3D> getDataList() {
    return null;
  }

  @Override
  public void setPositionVec(double[] position) {
    CoordSys tempSys = (CoordSys) craftToChase.getCoordSys().clone();
    tempSys.yRotate(-26);

    double[] displacedVec = VMath.vecAdd(tempSys.getPositionVec(), VMath.vecMultByScalar(tempSys.zAxis().getVectorForm(), -3000));
    super.setPositionVec(displacedVec);
//    view.getCoordSys().setPositionAsVec(getPosition());
    tempSys.zRotate(-90);
    getCoordSys().setXAxis(tempSys.xAxis().getVectorForm());
    getCoordSys().setYAxis(tempSys.yAxis().getVectorForm());
    getCoordSys().setZAxis(tempSys.zAxis().getVectorForm());

  }
}
