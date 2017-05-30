package autopilot;

import orbits.NavComputer;

public class NormalStrategy extends AFCSOnOrbitStrategy {

  public NormalStrategy(NavComputer computer) {
    super(computer);
    targetStrategy = new NormalTarget(computer);
    this.setName("N");
  }

}
