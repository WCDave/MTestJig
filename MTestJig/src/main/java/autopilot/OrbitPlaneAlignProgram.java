package autopilot;

import Foundation.Utils;
import VMath.VMath;
import enums.ComputerButtonKeys;
import gui.ComputerAbstractButton;
import orbits.IOrbitPlaneChangeTransformer;
import orbits.NavComputer;
import orbits.keplerian.KeplerCalc;
import org.apache.commons.math3.util.FastMath;
import org.apache.log4j.Logger;


public abstract class OrbitPlaneAlignProgram extends AFCSTargetingStrategy {
  protected IOrbitPlaneChangeTransformer transformer;
  protected ComputerAbstractButton button;
  private boolean alignmentInhibited;
  private double[] torqueVec;
  private double[] nodeXv;

  private Logger log = Logger.getLogger(OrbitPlaneAlignProgram.class);

  public OrbitPlaneAlignProgram(NavComputer computer) {
    super(computer);
  }

  protected boolean inPositionForBurn() {
    double value = FastMath.abs(VMath.dotprod(VMath.normalize(VMath.normalize(rocket.getPosition())), transformer.getNodeVector()));
    boolean result = false;
    if (computer.getControlAdapter().getThrottleSetting() > 0) {
      result = value >= .9;
    } else {
      result = value >= .997;
    }
    alignmentInhibited = result;
    //log.info("Angle to node: "+Math.acos(value)*180/Math.PI+" a="+alignmentInhibited);
    return result;
  }

  protected void alignForBurn() {

    if (!alignmentInhibited) {
      torqueVec = VMath.normalize(VMath.crossprd(rocket.getPosition(), rocket.getVelocity()));
      nodeXv = VMath.normalize(VMath.crossprd(transformer.getNodeVector(), rocket.getVelocity()));
      //log.debug("btn sel value: "+VMath.dotprod(torqueVec, nodeXv));
      if (VMath.dotprod(torqueVec, nodeXv) >= 0) {
        button = computer.getButton(ComputerButtonKeys.NORMAL);
        //	log.debug("clicking N button");
      } else {
        button = computer.getButton(ComputerButtonKeys.ANTINORMAL);
        //	log.debug("clicking AN button");
      }
      if (!button.isSelected()) {
        button.setSelected(true);
        button.doClick();
      }
      computer.setFlashAnnun(true);
      while (computer.isFlashAnnun()) {
        Utils.sleep(400);
      }
    }

  }

  @Override
  public boolean targetReached() {
    //log.info("align "+Math.acos(transformer.getOrbitalPlaneAlignment())*180/Math.PI);
    return transformer.getOrbitalPlaneAlignment() >= .99995d;
  }

  @Override
  public void run() {
    super.run();
    computer.setAnnunMsg("Plane Align");

    transformer.computeOrbit();
    log.info("Waiting for Ascending or Descending node for plane change burn");
    while (!targetReached()) {
      Utils.sleep(40);
      alignForBurn();
      if (inPositionForBurn()) {
        computer.getControlAdapter().setThrottle(110);
      } else {
        computer.getControlAdapter().setThrottle(0);
      }
      transformer.computeOrbit();
    }
    computer.getControlAdapter().setThrottle(0);
    button.setSelected(false);
    button.doClick();
    log.info("Plane align complete");
    try {
      executorService.submit(new KeplerCalc(rocket,true)).get().getKeplerianElements();
      executorService.submit(new KeplerCalc(computer.getReferenceObject(), true)).get().getKeplerianElements();
    }
    catch (Exception  e) {}
  }
}
