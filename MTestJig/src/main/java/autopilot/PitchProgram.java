package autopilot;

import Foundation.Utils;
import orbits.NavComputer;
import VMath.VMath;

public class PitchProgram extends AFCSTargetingStrategy {

  public PitchProgram(NavComputer computer) {
    super(computer);
  }

  @Override
  public void run() {
    super.run();
    rocket.nullRates();
    rocket.adjustPitchRate(.695f);
    computer.setAnnunMsg("Pitch Program");
    while (!targetReached()) {
      Utils.sleep(200);
    }
    computer.setAnnunMsg(null);
  }

  @Override
  public boolean targetReached() {
    double r[] = VMath.vecSubtract(cs.getPositionVec(), referencedObject.getCoordSys().getPositionVec());
    double nz[] = VMath.normalize(cs.zAxis().getVectorForm());
    r = VMath.normalize(r);
    double value = VMath.mag(VMath.crossprd(r, nz));
    //System.out.println(value);
    return value >= .95;
  }

}
