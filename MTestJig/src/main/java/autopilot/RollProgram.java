package autopilot;

import Foundation.Utils;
import orbits.NavComputer;

public class RollProgram extends AFCSTargetingStrategy {

  private int rollDuration;

  public RollProgram(NavComputer computer) {
    super(computer);
  }

  @Override
  public void run() {
    super.run();
    computer.setAnnunMsg("Roll Program");
    while (!targetReached()) {
      rocket.adjustRollRate(-1.4f);
      rollDuration++;
      Utils.sleep(500);
    }

    //System.out.println("endroll");
    rollDuration = 2;
    while (!targetReached()) {
      rocket.adjustRollRate(+1.4f);
      rollDuration++;
      Utils.sleep(200);
    }
    rocket.nullRates();
    computer.setAnnunMsg(null);
    //computer.getControlAdapter().setThrottle(40);
  }

  @Override
  public boolean targetReached() {
    return rollDuration >= 9;
  }

}
