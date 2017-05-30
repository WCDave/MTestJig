package orbits;

import autopilot.AFCSTargetingStrategy;
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
    return new CircularizeProgram(computer, new CircularizationRaisePeriApsis(computer));
  }
}
