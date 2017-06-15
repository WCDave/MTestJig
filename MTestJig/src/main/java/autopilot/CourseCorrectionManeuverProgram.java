package autopilot;

import Foundation.Utils;
import VMath.VMath;
import enums.OrbitElementKeys;
import orbits.IOrbitPlaneChangeTransformer;
import orbits.NavComputer;
import orbits.Planet;
import orbits.keplerian.KeplerCalc;
import org.apache.commons.math3.util.FastMath;
import org.apache.log4j.Logger;

public class CourseCorrectionManeuverProgram extends AFCSTargetingStrategy {

  private Planet planet;
  private double periapsis;
  private Logger log = Logger.getLogger(CourseCorrectionManeuverProgram.class);
  private static final double tliAdjustmentStart = 50000;

  public CourseCorrectionManeuverProgram(NavComputer computer, Planet planet, double periapsis) {
    super(computer);
    this.planet = planet;
    this.periapsis = periapsis * NavComputer.METERS_PER_MILE + planet.getRadius();
    System.out.println("planet name=" + planet.getName() + ", planet.getRadius()=" + planet.getRadius() + " periapsis=" + periapsis);
  }

  @Override
  public void run() {
    super.run();

    computer.setReferenceObject(planet);
    computer.getControlAdapter().selectReferenceObject(planet);
    //computer.getControlAdapter().toggleEnablePlanetSelect();
    log.info("referenced object=" + computer.getReferenceObject().getName());
    Utils.sleep(1000);

    log.info("awaiting CCM distance " + 240000 * NavComputer.METERS_PER_MILE + "@ " + VMath.mag((double[]) computer.getRocketOrbitElements().get(OrbitElementKeys.radiusVec)));

    executorService.submit(new KeplerCalc(rocket, true));
    executorService.submit(new KeplerCalc(planet, true));
    Utils.sleep(1800000);
    makePlaneAdjustment();
    executorService.submit(new KeplerCalc(rocket, true));
    executorService.submit(new KeplerCalc(planet, true));

    log.info("plane Adjustment completed...waiting for TLI adjustment " + tliAdjustmentStart + ", " + VMath.mag((double[]) computer.getRocketOrbitElements().get(OrbitElementKeys.radiusVec)) / NavComputer.METERS_PER_MILE);

    while (!targetReached()) {
      Utils.sleep(500);
    }

    log.info("TLI Adjustment starting, periluna=" + VMath.mag((double[]) computer.getRocketOrbitElements().get(OrbitElementKeys.radiusVec)) / NavComputer.METERS_PER_MILE);

    makeTLIAdjustment();

    log.info("TLI Adjustment ended, periluna=" + VMath.mag((double[]) computer.getRocketOrbitElements().get(OrbitElementKeys.radiusVec)) / NavComputer.METERS_PER_MILE);


//		log.info("turning retrograde");
//		
//		ComputerAbstractButton button = computer.getButton(ComputerButtonKeys.RETROGRADE);
//		button.setSelected(true);
//		button.doClick();
//		
//		computer.setFlashAnnun(true);
//		while(computer.isFlashAnnun())
//		{
//			Utils.sleep(400);
//		}
//		
//		computer.getControlAdapter().setThrottle(40);
//		log.info("starting 40% burn to set periapsis to "+this.periapsis);
//		log.info("@ "+ ((Double)computer.getOrbitElements().get(OrbitElementKeys.rPer))*NavComputer.METERS_PER_MILE);
//		while(!targetTwo())
//		{
//			Utils.sleep(100);
//		}
//		computer.getControlAdapter().setThrottle(0);
//		log.info("ending burn "+((Double)computer.getOrbitElements().get(OrbitElementKeys.rPer))*NavComputer.METERS_PER_MILE);
//		
//
//		button.setSelected(false);
//		button.doClick();

    try {
      executorService.submit(new KeplerCalc(rocket,true)).get().getKeplerianElements();
      executorService.submit(new KeplerCalc(computer.getReferenceObject(), true)).get().getKeplerianElements();
    }
    catch (Exception  e) {}

    //computer.getControlAdapter().toggleEnablePlanetSelect();

  }

  private void makeTLIAdjustment() {
    boolean continueBurn = true;
    double dotProd = 0;
    while (continueBurn) {
      double[] rockToMoonVec = VMath.vecSubtract(planet.getPosition(), rocket.getPosition());
      dotProd = VMath.dotprod(rocket.getVelocity(), VMath.vecSubtract(VMath.vecMultByScalar(rocket.getCoordSys().zAxis().getVectorForm(), VMath.dotprod(rockToMoonVec, rocket.getCoordSys().zAxis().getVectorForm())), rockToMoonVec));
      continueBurn = (Double) computer.getOrbitElements().get(OrbitElementKeys.rPer) < 0
              || FastMath.abs((Double) computer.getOrbitElements().get(OrbitElementKeys.rPer) - planet.getRadius() / NavComputer.METERS_PER_MILE - 150) > 5
              || dotProd < 0;
      if (continueBurn) {
        //log.info("inner>> "+((Double)computer.getOrbitElements().get(OrbitElementKeys.rPer)-planet.getRadius()/NavComputer.METERS_PER_MILE - 150)+", "+dotProd);
        if (dotProd > 0.0 && ((Double) computer.getOrbitElements().get(OrbitElementKeys.rPer) - planet.getRadius() / NavComputer.METERS_PER_MILE > 150)) {
          computer.getControlAdapter().rcsThrustLeft();
        } else {
          computer.getControlAdapter().rcsThrustRight();
        }
      }
    }
    computer.getControlAdapter().rcsThrustOff();
    log.info("burn ended " + ((Double) computer.getOrbitElements().get(OrbitElementKeys.rPer) - planet.getRadius() / NavComputer.METERS_PER_MILE - 150) + ", " + dotProd);

  }

  @Override
  public boolean targetReached() {
    double[] r = (double[]) computer.getRocketOrbitElements().get(OrbitElementKeys.radiusVec);
    return VMath.mag(r) <= tliAdjustmentStart * NavComputer.METERS_PER_MILE;
  }

  private boolean targetTwo() {
    return ((Double) computer.getOrbitElements().get(OrbitElementKeys.rPer)) * NavComputer.METERS_PER_MILE <= this.periapsis;
  }

  private void makePlaneAdjustment() {
    double[] moonOrbitPlaneNormal = VMath.normalize(IOrbitPlaneChangeTransformer.computeOrbitalNormal(planet, planet.getCentralBody().getCoordSys()));
    double correctionDot = VMath.dotprod(moonOrbitPlaneNormal, VMath.normalize(rocket.getVelocity()));

    if (correctionDot > 0) {
      computer.getControlAdapter().rcsThrustDown();
    } else {
      computer.getControlAdapter().rcsThrustUp();
    }

    log.info("making plane adjustment correction " + correctionDot);
    while (FastMath.abs(correctionDot) > .0005) {
      correctionDot = VMath.dotprod(moonOrbitPlaneNormal, VMath.normalize(rocket.getVelocity()));
      Utils.sleep(50);
    }

    computer.getControlAdapter().rcsThrustOff();
    log.info("plane adjustment correction complete " + correctionDot);
  }

}
