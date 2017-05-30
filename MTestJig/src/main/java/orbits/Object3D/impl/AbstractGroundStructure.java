package orbits.Object3D.impl;


import VMath.VMath;
import main.Craft;
import orbits.Abstract3DModelObject;
import orbits.CoordSys;
import orbits.Object3D.IObjectBuilder;
import orbits.Object3D.IRebuildable;
import orbits.World3DContainer;

public abstract class AbstractGroundStructure extends Craft implements IRebuildable {

  /* this is the location with respect to the CoordSys of the planet where the building exists */
  protected Abstract3DModelObject parentObject;
  protected double[] geometricCenter = new double[]{};

  public AbstractGroundStructure(double[] location, Abstract3DModelObject p) {
    super();
    setShadowCalculator(null);
    World3DContainer.getInstance().registerWithListeners(this);
    sSys = VMath.createCoordSys(location, location, p.getCoordSys().zAxis().getVectorForm());
    this.parentObject = p;
  }

  public abstract IObjectBuilder<AbstractGroundStructure> getBuilder();

    public double[] getGeometricCenter() {
    return geometricCenter;
  }

  public void setGeometricCenter(double[] geometricCenter) {
    this.geometricCenter = geometricCenter;
  }

  @Override
  public float getMass() {
    return 5;
  }

  @Override
  public void groundBehavior(double[] vec1, double[] vec2, Abstract3DModelObject a3o) {
    super.groundBehavior(vec1, vec2, a3o);
    CoordSys tSys = VMath.createCoordSys(vec2, vec2, a3o.getCoordSys().yAxis().getVectorForm());
    getCoordSys().setZAxis(tSys.zAxis().getVectorForm());
   getCoordSys().setYAxis(tSys.yAxis().getVectorForm());
   getCoordSys().setXAxis(tSys.xAxis().getVectorForm());
  }
  

}
