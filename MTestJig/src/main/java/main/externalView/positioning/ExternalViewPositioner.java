package main.externalView.positioning;


import domain.SolarSystemObject;
import orbits.Abstract3DModelObject;
import orbits.CoordSys;
import orbits.World3DContainer;

import java.util.List;

public class ExternalViewPositioner implements IExtViewPositioner {
  private int planetSelector;
  final private CoordSys viewingSys;
  final private double[] positionVectorOfObject;
  private final String objectToPointTo;

  public ExternalViewPositioner(int planetSelector, CoordSys viewingSys) {
    this.planetSelector = planetSelector;
    this.viewingSys = viewingSys;
    List<SolarSystemObject> objects = World3DContainer.getInstance().getSolarSystemObjects();
    SolarSystemObject object = objects.get(planetSelector);
    positionVectorOfObject = object.getGeoPositionVec();
    objectToPointTo = "Sun";
  }

  public ExternalViewPositioner(String nameOfObject, CoordSys viewingSys, String objectToPointTo) {
    this.viewingSys = viewingSys;
    Abstract3DModelObject a3o = World3DContainer.getInstance().getItem(nameOfObject);
    positionVectorOfObject = a3o.getCoordSys().getPositionVec();
    this.objectToPointTo = objectToPointTo;
  }


  @Override
  public void postionViewingSys() {
    viewingSys.setPositionAsVec(positionVectorOfObject);
    World3DContainer.getInstance().aimCoordSysAtObject(viewingSys, objectToPointTo);
    viewingSys.zRotate(-90);
  }
}
