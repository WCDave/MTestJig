package autopilot;

import orbits.NavComputer;

public class RetrogradeStrategy extends AFCSOnOrbitStrategy {

  public RetrogradeStrategy(NavComputer computer) {
    super(computer);
    targetStrategy = new RetrogradeTarget(computer);
    this.setName("R");
  }

}
