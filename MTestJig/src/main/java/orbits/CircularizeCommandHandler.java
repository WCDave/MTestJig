package orbits;

import autopilot.AFCSTargetingStrategy;
import autopilot.AbstractCircularizationTarget;
import autopilot.CircularizationLowerApoApsis;
import autopilot.CircularizationRaisePeriApsis;
import autopilot.CircularizeProgram;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;


public class CircularizeCommandHandler implements ICommandHandler<Matcher, AFCSTargetingStrategy> {

  private NavComputer computer;

  public CircularizeCommandHandler(NavComputer computer) {
    this.computer = computer;
  }

  @Override
  public List<AFCSTargetingStrategy> handle(Matcher m) {
    AbstractCircularizationTarget target = null;
    if(m.group().contains("-")) {
      target = new CircularizationLowerApoApsis(computer);
    }
    else {
      target =  new CircularizationRaisePeriApsis(computer);
    }
    return Collections.singletonList(new CircularizeProgram(computer, target));
  }
}
