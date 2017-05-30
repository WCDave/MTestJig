package autopilot;

import orbits.NavComputer;

public class ProgradeStrategy extends AFCSOnOrbitStrategy {

  public ProgradeStrategy(NavComputer computer) {
    super(computer);
    targetStrategy = new ProgradeTarget(computer);
    this.setName("P");
  }
}
