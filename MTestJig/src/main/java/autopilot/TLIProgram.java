package autopilot;

import Foundation.Utils;
import VMath.VMath;
import enums.ComputerButtonKeys;
import enums.OrbitElementKeys;
import gui.ComputerAbstractButton;
import main.ConfigurationManager;
import orbits.NavComputer;
import orbits.Planet;
import orbits.World3DContainer;
import orbits.keplerian.KeplerCalc;
import orbits.keplerian.KeplerianElements;
import org.apache.commons.math3.util.FastMath;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TLIProgram extends AFCSTargetingStrategy {

  private Planet moon, earth;
  private double[] moonToEarthVector;
  private double tliCritAngle;
  private int diffCount;

  private static final String TLI_CRIT_ANGLE = "TLI_CRIT_ANGLE";

  private float burnTargetVelocity = 26000 / 2.236f;

  private Logger log = Logger.getLogger(TLIProgram.class);

  public TLIProgram(NavComputer computer) {
    super(computer);
    moon = (Planet) World3DContainer.getInstance().getItem("Moon");
    earth = (Planet) World3DContainer.getInstance().getItem("Earth");
  }

  @Override
  public void run() {
    super.run();
    try {
      tliCritAngle = Double.parseDouble(ConfigurationManager.getInstance().getProperty(TLI_CRIT_ANGLE));
    } catch (NullPointerException e) {
      tliCritAngle = .9d;
    }
    computer.setAnnunMsg("TLI Program");
    ComputerAbstractButton button = computer.getButton(ComputerButtonKeys.PROGRADE);
    button.setSelected(true);
    button.doClick();
    log.info("Waiting for TLI burn target point " + tliCritAngle);
    //DebugThread dt = new DebugThread();
    //dt.start();
    while (!targetReached()) {
      Utils.sleep(50);
    }
    //dt.runThread = false;

    log.info("TLI burn started*****" + Math.toDegrees(Math.acos(VMath.dotprod(VMath.normalize(moonToEarthVector), VMath.normalize(rocket.getPosition())))) + " degrees");
    log.info("*********************" + Math.toDegrees(Math.acos(VMath.dotprod(rocket.getCoordSys().zAxis().getVectorForm(), VMath.normalize(moonToEarthVector)))) + " degrees");
    log.info("vs*******************" + VMath.dotprod(VMath.vecSubtract(moon.getVelocity(), rocket.getVelocity()), VMath.normalize(VMath.vecSubtract(moon.getPosition(), rocket.getPosition()))));
    log.info("DP*******************" + VMath.dotprod(rocket.getCoordSys().zAxis().getVectorForm(), VMath.normalize(VMath.vecSubtract(moon.getCoordSys().getPositionVec(), rocket.getCoordSys().getPositionVec()))));
    computer.getControlAdapter().setThrottle(80);
    //Double rAph_miles = ((Double)computer.getOrbitElements().get(OrbitElementKeys.rAph))/NavComputer.METERS_PER_MILE;
    log.info("rAph_miles1=" + ((Double) computer.getOrbitElements().get(OrbitElementKeys.rAph)));
    while (VMath.mag(rocket.getVelocity()) < burnTargetVelocity) {
      //log.info("rAph_miles2="+((Double)computer.getOrbitElements().get(OrbitElementKeys.rAph)));
      Utils.sleep(50);
    }
    log.info("rAph_miles3=" + ((Double) computer.getOrbitElements().get(OrbitElementKeys.rAph)));

    computer.getControlAdapter().setThrottle(0);
    try {
      executorService.submit(new KeplerCalc(rocket,true)).get().getKeplerianElements();
      executorService.submit(new KeplerCalc(moon, true)).get().getKeplerianElements();
    }
    catch (Exception  e) {}

  }

  @Override
  public boolean targetReached() {
    //Mta-(Rta+(Rap-Map))
    KeplerianElements rke = null;
    KeplerianElements mke = null;
    try {
      rke = executorService.submit(new KeplerCalc(rocket)).get().getKeplerianElements();
      mke = executorService.submit(new KeplerCalc(moon)).get().getKeplerianElements();
    }
    catch (Exception  e) {}
    moonToEarthVector = VMath.vecSubtract(moon.getCoordSys().getPositionVec(), earth.getCoordSys().getPositionVec());
//    double[] bodyToObjectVec = VMath.vecSubtract(moon.getPosition(), rocket.getPosition());
//    double[] rocketRelVelocity = VMath.vecSubtract(moon.getVelocity(), rocket.getVelocity());
//    double vs = VMath.dotprod(rocketRelVelocity, VMath.normalize(bodyToObjectVec));
//
//    double critAngle = FastMath.toDegrees(FastMath.acos(VMath.dotprod(rocket.getCoordSys().zAxis().getVectorForm(), VMath.normalize(moonToEarthVector))));
//    double[] xProd = VMath.crossprd(rocket.getCoordSys().zAxis().getVectorForm(), VMath.normalize(moonToEarthVector));
//
//    return vs < 0 && critAngle >= tliCritAngle && critAngle <= tliCritAngle + .02d && VMath.dotprod(xProd, earth.getCoordSys().zAxis().getVectorForm()) > 0;
    //2.36
    double angleDiff = (4 * Math.PI +(mke.getAop()+mke.getTa() - (rke.getAop()+rke.getTa()))) % (2 * Math.PI);
    if(angleDiff >2.25 && angleDiff < 2.27) {
      diffCount++;
    }
    else {
      diffCount = 0;
    }
    //System.out.println(Math.toDegrees(angleDiff));
    return  diffCount == 2;
  }

  private class DebugThread extends Thread {

    public boolean runThread = true;

    public DebugThread()   {

    }

    @Override
    public void run() {
      System.out.println("start thread");
       while (runThread) {
         log.info("TLI burn started*****" + Math.toDegrees(Math.acos(VMath.dotprod(VMath.normalize(moonToEarthVector), VMath.normalize(rocket.getPosition())))) + " degrees1");
         log.info("*********************" + Math.toDegrees(Math.acos(VMath.dotprod(rocket.getCoordSys().zAxis().getVectorForm(), VMath.normalize(moonToEarthVector)))) + " degrees2");
         log.info("vs*******************" + VMath.dotprod(VMath.vecSubtract(moon.getVelocity(), rocket.getVelocity()), VMath.normalize(VMath.vecSubtract(moon.getPosition(), rocket.getPosition()))));
         log.info("DP*******************" + VMath.dotprod(rocket.getCoordSys().zAxis().getVectorForm(), VMath.normalize(VMath.vecSubtract(moon.getCoordSys().getPositionVec(), rocket.getCoordSys().getPositionVec()))));
         log.info("xprod****************" + VMath.dotprod(VMath.crossprd(rocket.getCoordSys().zAxis().getVectorForm(), VMath.normalize(moonToEarthVector)), earth.getCoordSys().zAxis().getVectorForm()));
         System.out.println("Thread");
         Utils.sleep(180000);
       }
      System.out.println("end thread");
    }
  }

}
