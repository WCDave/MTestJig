package orbits;

import autopilot.AFCSTargetingStrategy;
import autopilot.PlanetPlaneChangePlaneAlignProgram;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by dbutterw on 5/22/2017.
 */
public class PlaneChangeCommandHandler implements ICommandHandler {

  private NavComputer computer;

  public PlaneChangeCommandHandler(NavComputer computer) {
    this.computer = computer;
  }
  @Override
  public List<AFCSTargetingStrategy> handle(Matcher m) {
    String resultCommand = m.group().substring(1);
    float targetPlaneAngle = Float.parseFloat(resultCommand);
    CoordSys cs = (CoordSys) computer.getReferenceObject().getCoordSys().clone();
    cs.xRotate(targetPlaneAngle);
    PlanetPlaneChangePlaneAlignProgram planeChangeAlignProgram = new PlanetPlaneChangePlaneAlignProgram(computer, cs.zAxis().getVectorForm());
    return Arrays.asList(planeChangeAlignProgram);
  }
}
