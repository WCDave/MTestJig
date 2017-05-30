package autopilot;

import orbits.Abstract3DModelObject;
import orbits.CoordSys;
import orbits.NavComputer;


public abstract class AbstractCircularizationTarget implements ITarget {

  protected Abstract3DModelObject referencedObject;
  protected CoordSys cs;
  protected NavComputer computer;

  public AbstractCircularizationTarget(NavComputer computer) {
    referencedObject = computer.getReferenceObject();
    cs = computer.getCraft().getCoordSys();
    this.computer = computer;
  }

  public void initTargeter() {
    referencedObject = computer.getReferenceObject();
  }

}
