package orbits;

import main.Craft;
import org.apache.log4j.Logger;
import Foundation.Utils;
import VMath.VMath;

public class SatelliteSpinUp implements Runnable {

  private Craft craft;
  private Logger log = Logger.getLogger(SatelliteSpinUp.class);

  public SatelliteSpinUp(Craft c) {
    craft = c;
  }

  @Override
  public void run() {

    int i = 0;
    CoordSys navObject = craft.getCoordSys();
    while (i++ < 180) {
      navObject.rotateAroundArbitraryAxis(navObject.xAxis().getVectorForm(), .5);
      try {
        Thread.sleep(80);
      } catch (InterruptedException e) {
        craft.nullRates();
        return;
      }
    }

    i = 0;
    while (i++ < 100) {
      craft.adjustRollRate(.25f);
      try {
        Thread.sleep(80);
      } catch (InterruptedException e) {
        craft.nullRates();
        return;
      }
    }

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      craft.nullRates();
      return;
    }
    log.debug("Spin-up complete");
    craft.setVelocityVec(VMath.vecAdd(craft.getVelocity(), VMath.vecMultByScalar(navObject.zAxis().getVectorForm(), .6f)));
    craft = null;
  }

}
