package orbits;


import autopilot.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

public class MoonCommandHandler implements ICommandHandler<Matcher, AFCSTargetingStrategy> {

  private NavComputer computer;

  public MoonCommandHandler(NavComputer computer) {
    this.computer = computer;
  }
  @Override
  public List<AFCSTargetingStrategy> handle(Matcher m) {
    return Arrays.asList(new MoonOrbitPlaneAlignProgram(computer), new TLIProgram(computer),
            new CourseCorrectionManeuverProgram(computer, (Planet) World3DContainer.getInstance().getItem("Moon"), 80d),
            new CircularizeProgram(computer, new CircularizationLowerApoApsis(computer)));
  }
}
