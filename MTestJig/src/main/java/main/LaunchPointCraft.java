package main;

import VMath.VMath;
import enums.ViewTypes;
import orbits.Abstract3DModelObject;
import orbits.CoordSys;
import orbits.Object3D.IObjectBuilder;
import orbits.Object3D.impl.AbstractGroundStructure;

import java.util.Vector;

/**
 * Created by dbutterw on 5/16/2017.
 */
public class LaunchPointCraft extends AbstractGroundStructure {

  private ExternalView eView;
  private double[] tmpRelVec;
  private CoordSys viewSys;

  public LaunchPointCraft(double[] position, ExternalView eView, Abstract3DModelObject a3o) {
    super(position, a3o);
    sSys.setPositionAsVec(position);
    this.eView = eView;
    viewSys = eView.getCoordSys();
  }

  @Override
  public Vector getDataList() {
    return null;
  }

  @Override
  public double getDragFactor() {
    return 0;
  }


  @Override
  public IObjectBuilder<AbstractGroundStructure> getBuilder() {
    return null;
  }

  @Override
  public float getMass() {
    return 5;
  }

  @Override
  public void set3DObjectForDraw(AbstractView view) {
    //setVectorFromView(VMath.vecSubtract(sSys.getPositionVec(), view.getCoordSys().getPositionVec()));
    super.set3DObjectForDraw(view);
  }

  @Override
  public void draw(AbstractView view) {

  }

  @Override
  public void setPositionVec(double[] pos) {
    super.setPositionVec(pos);
    if (eView.isVisible() && eView.getViewType().equals(ViewTypes.ROCKET)) {
      new Thread(() -> {
        CoordSys viewSys = eView.getCoordSys();
        double[] launchPoint = pos;
        launchPoint = VMath.vecAdd(launchPoint, VMath.vecMultByScalar(VMath.normalize(launchPoint), 300));
        double[] relVec = VMath.vecSubtract(eView.getCraft().getCoordSys().getPositionVec(), launchPoint);
        //double[] relVec = VMath.vecSubtract(World3DContainer.getInstance().getItem("Muddy").getCoordSys().getPositionVec(), launchPoint);

        if (VMath.dotprod(relVec, launchPoint) > 0 || tmpRelVec == null)  {
          tmpRelVec = relVec;

          double[] zTemp = VMath.normalize(tmpRelVec);

          double[] xTemp = VMath.normalize(VMath.crossprd(launchPoint, tmpRelVec));

          viewSys.setZAxis(zTemp);
          viewSys.setXAxis(xTemp);
          viewSys.setYAxis(VMath.crossprd(zTemp, xTemp));

        }
        viewSys.setPositionAsVec(launchPoint);
        viewSys.setD(eView.getCraft().getCoordSys().getD());
      }).start();
    }

  }

  @Override
  public void rebuild(int factor) {

  }

  @Override
  public double getDetailingDistance() {
    return 0;
  }

  @Override
  public int getBuildFactor() {
    return 0;
  }
}
