package rocketComponents;

import java.util.HashMap;
import java.util.Map;

import main.Craft;
import orbits.CoordSys;
import VMath.VMath;

public class CargoBay {

  private Map<String, Craft> cargoMap = new HashMap<String, Craft>();
  private Craft owningCraft;

  public CargoBay(Craft c) {
    owningCraft = c;
  }

  public void setCargoMap(Map<String, Craft> cargoMap) {
    this.cargoMap = cargoMap;
  }

  public Map<String, Craft> getCargoMap() {
    return cargoMap;
  }

  public Craft extractCraft(String craftName) {
    Craft result = cargoMap.remove(craftName);
    CoordSys navObject = (CoordSys) owningCraft.getCoordSys().clone();

    if (result != null) {
      result.setAutoPilotInhibit(false);
      double[] posVec = navObject.getPositionVec();
      posVec = VMath.vecAdd(posVec, VMath.vecMultByScalar(navObject.zAxis().getVectorForm(), -75));
      navObject.setPositionAsVec(posVec);
    } else {
      result = cargoMap.entrySet().iterator().next().getValue();
      double[] velVec = VMath.vecMultByScalar(navObject.zAxis().getVectorForm(), 500000d);
      navObject.setVelocityAsVec(velVec);
    }
    result.setCoordSys(navObject);


    return result;
  }

  public void insertCraft(Craft craft) {
    cargoMap.put(craft.getName(), craft);
  }

}
