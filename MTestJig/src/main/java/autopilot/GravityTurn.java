package autopilot;

import Foundation.Utils;
import VMath.VMath;
import autopilot.pathPrecision.ICorridorControl;
import autopilot.pathPrecision.impl.AbstractCorridorFactory;
import autopilot.pathPrecision.impl.EarthGravityTurnCorridorFactory;
import autopilot.pathPrecision.impl.GravityTurnThrustOnlyCorridorControl;
import enums.ControlInputSource;
import enums.OrbitElementKeys;
import main.Craft;
import orbits.CoordSys;
import orbits.NavComputer;
import orbits.Planet;
import orbits.keplerian.KeplerCalc;
import org.apache.commons.math3.util.FastMath;

import java.util.HashMap;
import java.util.Map;

public class GravityTurn extends AFCSTargetingStrategy {

  private static final double[] PITCH_RATE_BOUNDS = new double[]{-.1f, 4f};
  private static final float rateAdjustment = .04f;
  private ICorridorControl<Integer, Integer, Integer> corridorControl;
  private AbstractCorridorFactory<Integer, Integer> corridorFactory = new EarthGravityTurnCorridorFactory(100);
  private boolean runThread=true;

  public GravityTurn(NavComputer computer) {
    super(computer);
    corridorControl = new GravityTurnThrustOnlyCorridorControl(computer);
  }

  @Override
  public void run() {
    super.run();
    computer.setAnnunMsg("Gravity Turn");
    Craft c = computer.getCraft();
    //CoordSys navObject = rocket.getCoordSys();
    //Thread t = new MileStoneThread();

    //long t0 = System.currentTimeMillis();
    c.adjustPitchRate(5.39f);
    Utils.sleep(4000);
    c.nullRates();
    //((GravityTurnThrustOnlyCorridorControl)corridorControl).setBaselineThrustValue(computer.getControlAdapter().getThrottleSetting());
    //t.start();
//    double pitchValue = VMath.dotprod(cs.zAxis().getVectorForm(), VMath.normalize(VMath.vecSubtract(cs.getPositionVec(), referencedObject.getCoordSys().getPositionVec())));
//    System.out.println(pitchValue);
    //computer.getControlAdapter().rampThrust(60, ControlInputSource.COMPUTER);

    while (FastMath.abs(VMath.dotprod(cs.zAxis().getVectorForm(), VMath.normalize(c.getRelativeWind()))) < .9999) {
      Utils.sleep(30);
    }
    c.setPitchRate(1.5f);

    while (!targetReached()) {
      Utils.sleep(10);
      double adj = rateAdjustment * FastMath.signum(VMath.dotprod(VMath.crossprd(cs.zAxis().getVectorForm(), c.getRelativeWind()), cs.xAxis().getVectorForm()));

      if (adj < 0) {
        if (!(c.getPitchRate() <= PITCH_RATE_BOUNDS[0]))
          c.adjustPitchRate((float) adj);
      } else {
        if (!(c.getPitchRate() > PITCH_RATE_BOUNDS[1]))
          c.adjustPitchRate((float) (2 * adj));
      }

      //corridorControl.controlChange(corridorFactory.getCorridor((int)((System.currentTimeMillis() - t0) /1000)));
    }
    //c.setPitchRate(.1f);

    computer.getControlAdapter().setThrottle(0);
    //computer.getControlAdapter().rampAdjustThrust(0,ControlInputSource.COMPUTER);
    //((GravityTurnThrustOnlyCorridorControl)corridorControl).setBaselineThrustValue(0);
    runThread = false;

    //rocket.setThrottleSetting(0);
    c.nullRates();
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
    double r[] = VMath.vecSubtract(cs.getPositionVec(), referencedObject.getCoordSys().getPositionVec());
    double alt = VMath.mag(r);
    double nz[] = VMath.normalize(cs.zAxis().getVectorForm());
    r = VMath.normalize(r);
//    double value = VMath.mag(VMath.crossprd(r, nz));
//    double value=VMath.mag(VMath.crossprd(VMath.normalize(r),cs.zAxis().getVectorForm()));
//    System.out.println(VMath.mag(VMath.crossprd(r, nz)));
    return ((Double) computer.getOrbitElements().get(OrbitElementKeys.rAph)).doubleValue() >= ((Planet) referencedObject).getRadius() / NavComputer.METERS_PER_MILE + computer.getTargetAltitude();
  }

  private int getPitch(double[] rBar) {
    double zDotRad = VMath.dotprod(cs.zAxis().getVectorForm(), VMath.normalize(rBar));
    double pitch = VMath.mag(VMath.crossprd(VMath.normalize(rBar), cs.zAxis().getVectorForm()));
    return (int) FastMath.round(FastMath.signum(zDotRad)*FastMath.toDegrees(FastMath.acos(pitch)));
  }

  private class MileStoneThread extends Thread {

    Map<Double, Double> resultMap = new HashMap<Double, Double>();
    @Override
    public void run() {
      //long t0 = System.currentTimeMillis();

      while(runThread) {
        Utils.sleep(10000);
        corridorControl.controlChange(corridorFactory.getCorridor((int)getPitch()));
      }
      computer.getControlAdapter().rampThrust(0, ControlInputSource.COMPUTER);
      System.out.println("End corridor control " + computer.getControlAdapter().getThrottleSetting());
//      long t0 = System.currentTimeMillis();
//      long dt = 0;
//      while(runThread) {
//          Utils.sleep(5000);
//          resultMap.put(getPitch() , new Double(VMath.mag(VMath.vecSubtract(cs.getPositionVec(), referencedObject.getCoordSys().getPositionVec()))) ) ;
//          //System.out.println((double)(dt/1000.0)+" - "+ VMath.mag(VMath.vecSubtract(cs.getPositionVec(), referencedObject.getCoordSys().getPositionVec())));
//
//      }
//      for (Double key : resultMap.keySet()) {
//        System.out.println(key+" --- "+resultMap.get(key));
//      }
    }

    private double getPitch() {
      CoordSys cs = computer.getCraft().getCoordSys();
      double[] rBar = VMath.vecSubtract(cs.getPositionVec(), computer.getReferenceObject().getCoordSys().getPositionVec());

      double zDotRad = VMath.dotprod(cs.zAxis().getVectorForm(), VMath.normalize(rBar));
      //double pitch = 1-zDotRad;
      //System.out.println(zDotRad+", "+ (90 - Math.toDegrees(Math.acos(zDotRad))));
      return  FastMath.round(90-FastMath.toDegrees(FastMath.acos(zDotRad)));
    }
  }

}
