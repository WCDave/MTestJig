package main;

import orbits.CoordSys;

public class LaunchPointStrategy implements IExternalViewStrategy<ExternalView> {

  private double[] tmpRelVec;
  private CoordSys viewSys;

  @Override
  public void applyToView(ExternalView view) {

//    CoordSys lp = World3DContainer.getInstance().getItem("LP").getCoordSys();
//
//    viewSys = view.getCoordSys();
//    double[] launchPoint;
//    synchronized (lp) {
//      launchPoint = lp.getPositionVec();
//    }
//    launchPoint = SphereHelper.movePoint(launchPoint, 200, 800, (Sphere) World3DContainer.getInstance().getItem("Earth"));
//
//    launchPoint = VMath.vecAdd(launchPoint, VMath.vecMultByScalar(VMath.normalize(launchPoint), 300));
//    double[] relVec = VMath.vecSubtract(view.getCraft().getCoordSys().getPositionVec(), launchPoint);
//    //double[] relVec = VMath.vecSubtract(World3DContainer.getInstance().getItem("Muddy").getCoordSys().getPositionVec(), launchPoint);
//
//
//    if (VMath.dotprod(relVec, launchPoint) > 0 || tmpRelVec == null) {
//      tmpRelVec = relVec;
//
//      double[] zTemp = VMath.normalize(tmpRelVec);
//
//      double[] xTemp = VMath.normalize(VMath.crossprd(launchPoint, tmpRelVec));
//
//      viewSys.setZAxis(zTemp);
//      viewSys.setXAxis(xTemp);
//      viewSys.setYAxis(VMath.crossprd(zTemp, xTemp));
//
//    }
//    viewSys.setPositionAsVec(launchPoint);
//    viewSys.setD(view.getCraft().getCoordSys().getD());
  }

  @Override
  public void reset() {

  }

}
