package autopilot;

import Foundation.Utils;
import VMath.VMath;
import enums.OrbitElementKeys;
import orbits.NavComputer;
import orbits.Planet;
import orbits.keplerian.KeplerCalc;
import org.apache.commons.math3.util.FastMath;
import org.apache.log4j.Logger;

public class CircularizeProgram extends AFCSTargetingStrategy {
  private int omsBurnNumber = 2;
  private double eccThreshold;
  private static final double EARTH_MASS = 5.99e24;
  private Logger log = Logger.getLogger(CircularizeProgram.class);
  private AbstractCircularizationTarget targeter = null;
  ;

  public CircularizeProgram(NavComputer computer, AbstractCircularizationTarget targeter) {
    super(computer);
    this.targeter = targeter;
  }

  @Override
  public void run() {
    super.run();
    referencedObject = computer.getReferenceObject();
    //ComputerAbstractButton button = null;
    eccThreshold = .00009 * EARTH_MASS / (((Planet) referencedObject).getMass());

    double[] bodyToObjectVec = VMath.vecSubtract(computer.getCraft().getCoordSys().getPositionVec(), computer.getReferenceObject().getCoordSys().getPositionVec());
    double[] rocketVelocity = computer.getCraft().getCoordSys().getVelocityAsVec();
    double[] rocketRelVelocity = VMath.vecSubtract(rocketVelocity, ((Planet) (computer.getReferenceObject())).getVelocity());
    double vs = VMath.dotprod(rocketRelVelocity, VMath.normalize(bodyToObjectVec));
    targeter.initTargeter();
//    if (VMath.mag(computer.getCraft().getDrag()) > 0) {
//      targeter = new CircularizationRaisePeriApsis(computer);
//    } else {
//      targeter = new CircularizationLowerApoApsis(computer);
//    }

    targeter.alignForManeuver();
    computer.setFlashAnnun(false);

    while (VMath.mag((double[]) computer.getOrbitElements().get(OrbitElementKeys.ecc)) > (20 / 3) * eccThreshold) {
      computer.setAnnunMsg("Apsis coast");
      log.info("waiting for burn point");

      while (!targetReached()) {
        Utils.sleep(100);
      }

      log.info("at burn point");

      computer.setAnnunMsg("OMS " + String.format("%2d", omsBurnNumber++) + " Burn");

      double testEcc = 100;
      while (VMath.mag((double[]) computer.getOrbitElements().get(OrbitElementKeys.ecc)) > eccThreshold && VMath.mag((double[]) computer.getOrbitElements().get(OrbitElementKeys.ecc)) <= testEcc) {
        testEcc = VMath.mag((double[]) computer.getOrbitElements().get(OrbitElementKeys.ecc));
        computer.getControlAdapter().setThrottle((int) (FastMath.min(110, testEcc * 15000)));
        Utils.sleep(2);
      }
      computer.getControlAdapter().setThrottle(0);
      log.info("burn complete");
    }
    rocket.setAutoPilotInhibit(false);
    computer.setAnnunMsg("OMS " + String.format("%2d", omsBurnNumber) + " Not Req");
    computer.setFlashAnnun(true);
    Utils.sleep(10000);
    try {
      executorService.submit(new KeplerCalc(rocket,true)).get().getKeplerianElements();
      executorService.submit(new KeplerCalc(computer.getReferenceObject(), true)).get().getKeplerianElements();
    }
    catch (Exception  e) {}
//		button.setSelected(false);
//		button.doClick();
  }

  @Override
  public boolean targetReached() {
//		double apsis = ((Double)computer.getOrbitElements().get(apsisKey)).doubleValue();
//		double oppositeApsis = ((Double)computer.getOrbitElements().get(oppositeApsisKey)).doubleValue();
//		double radius = VMath.mag(VMath.vecSubtract(cs.getPositionVec(),referencedObject.getCoordSys().getPositionVec()));
//		return (apsis-radius/NavComputer.METERS_PER_MILE)/(apsis - oppositeApsis)<.00003;
    return targeter.targetReached();
  }
}
