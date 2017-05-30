package autopilot;

import orbits.NavComputer;
import orbits.Planet;
import Foundation.Utils;
import VMath.VMath;
import enums.OrbitElementKeys;
import orbits.keplerian.KeplerCalc;

public class OrbitInsertion extends AFCSTargetingStrategy {

  private float targetAltitude = 220;

  public OrbitInsertion(NavComputer computer) {
    super(computer);
    targetAltitude = computer.getTargetAltitude();
  }

  @Override
  public void run() {
    super.run();
    computer.setAnnunMsg("Orbit Insertion ");
    boolean hFlag = false;
    computer.getControlAdapter().setThrottle(105);

    while (!targetReached()) {
      double z[] = cs.zAxis().getVectorForm();
      double dp = VMath.dotprod(VMath.normalize((double[]) computer.getOrbitElements().get(OrbitElementKeys.radiusVec)), z);
      if (hFlag) {
        rocket.adjustPitchRate(.5f);
        hFlag = false;
      }
      if (dp < 0.00) {
        rocket.adjustPitchRate(-rocket.getPitchRate() / 1.001f);
        hFlag = true;
      }
      Utils.sleep(30);
      //System.out.println("PR= "+rocket.getPitchRate());
    }
    computer.getControlAdapter().setThrottle(0);
    //rocket.setThrottleSetting(0);
    rocket.nullRates();
    computer.setAnnunMsg("MECO");
    computer.setFlashAnnun(true);
    Utils.sleep(7000);
    computer.setFlashAnnun(false);
    try {
      executorService.submit(new KeplerCalc(rocket,true)).get().getKeplerianElements();
      executorService.submit(new KeplerCalc(computer.getReferenceObject(), true)).get().getKeplerianElements();
    }
    catch (Exception  e) {}
  }

  @Override
  public boolean targetReached() {
    return ((Double) computer.getOrbitElements().get(OrbitElementKeys.rAph)).doubleValue() >= ((Planet) referencedObject).getRadius() / NavComputer.METERS_PER_MILE + targetAltitude;
    //double spd=VMath.mag(computer.getCraft().getNavObject().getVelocityAsVec());
    //return spd>=17450/2.236f;
  }

}
