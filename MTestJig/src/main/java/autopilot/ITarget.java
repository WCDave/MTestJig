package autopilot;

import gui.ComputerAbstractButton;
import orbits.NavComputer;

public interface ITarget {

  boolean targetReached();

  void alignForManeuver();
}
