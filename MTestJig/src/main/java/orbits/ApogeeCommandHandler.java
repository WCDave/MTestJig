package orbits;

import autopilot.AFCSTargetingStrategy;
import autopilot.GravityTurn;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;


public class ApogeeCommandHandler implements ICommandHandler {
  private NavComputer computer;

  public ApogeeCommandHandler(NavComputer computer) {
    this.computer = computer;
  }

  @Override
  public List<AFCSTargetingStrategy> handle(Matcher m) {
    String resultCommand = m.group().substring(1);
    computer.setTargetAltitude(Float.parseFloat(resultCommand));
    return Arrays.asList(new GravityTurn(computer));
  }
}
