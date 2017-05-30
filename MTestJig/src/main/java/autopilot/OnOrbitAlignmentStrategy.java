package autopilot;

import java.util.List;

import orbits.CoordSys;
import orbits.NavComputer;

public abstract class OnOrbitAlignmentStrategy {

  protected NavComputer computer;
  protected CoordSys cs, refSys;

  public OnOrbitAlignmentStrategy(NavComputer computer) {
    this.computer = computer;
    cs = computer.getCraft().getCoordSys();
    refSys = computer.getReferenceObject().getCoordSys();
  }

  public abstract List<double[]> acquireTarget();

}
