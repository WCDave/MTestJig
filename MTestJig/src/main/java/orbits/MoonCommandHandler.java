package orbits;


import autopilot.AFCSTargetingStrategy;
import autopilot.MoonOrbitPlaneAlignProgram;
import autopilot.TLIProgram;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

public class MoonCommandHandler implements ICommandHandler {

  private NavComputer computer;

  public MoonCommandHandler(NavComputer computer) {
    this.computer = computer;
  }
  @Override
  public List<AFCSTargetingStrategy> handle(Matcher m) {
    return Arrays.asList(new MoonOrbitPlaneAlignProgram(computer), new TLIProgram(computer));

  }
}
