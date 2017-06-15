package orbits;

import autopilot.AFCSTargetingStrategy;
import autopilot.GravityTurn;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;


public class ApogeeCommandHandler implements ICommandHandler<Matcher, AFCSTargetingStrategy> {
  private NavComputer computer;

  public ApogeeCommandHandler(NavComputer computer) {
    this.computer = computer;
  }

  @Override
  public List<AFCSTargetingStrategy> handle(Matcher m) {
    String resultCommand = m.group().substring(1);
    computer.setTargetAltitude(Float.parseFloat(resultCommand));
    return Collections.singletonList(new GravityTurn(computer));
  }
}
