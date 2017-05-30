package autopilot;

import orbits.NavComputer;

public class AntiNormalStrategy extends AFCSOnOrbitStrategy {

  public AntiNormalStrategy(NavComputer computer) {
    super(computer);
    this.targetStrategy = new AntiNormalTarget(computer);
    this.setName("AN");
  }

}
