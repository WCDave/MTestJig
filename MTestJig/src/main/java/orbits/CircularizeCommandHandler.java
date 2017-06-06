package orbits;

import autopilot.AFCSTargetingStrategy;
import autopilot.AbstractCircularizationTarget;
import autopilot.CircularizationLowerApoApsis;
import autopilot.CircularizationRaisePeriApsis;
import autopilot.CircularizeProgram;

import java.util.regex.Matcher;

/**
 * Created by dbutterw on 5/22/2017.
 */
public class CircularizeCommandHandler implements ICommandHandler {

  private NavComputer computer;

  public CircularizeCommandHandler(NavComputer computer) {
    this.computer = computer;
  }

  @Override
  public AFCSTargetingStrategy handle(Matcher m) {
    AbstractCircularizationTarget target = null;
    if(m.group().contains("-")) {
      target = new CircularizationLowerApoApsis(computer);
    }
    else {
      target =  new CircularizationRaisePeriApsis(computer);
    }
    return new CircularizeProgram(computer, target);
  }
}
