package autopilot;

import enums.ComputerButtonKeys;
import gui.ComputerAbstractButton;
import main.PhysicsRunnable;
import orbits.NavComputer;
import VMath.VMath;
import enums.OrbitElementKeys;
import orbits.Planet;
import org.apache.commons.math3.util.FastMath;
import org.apache.log4j.Logger;

public class CircularizationRaisePeriApsis extends AbstractCircularizationTarget {

  private double deltaV;
  private static final double deltaVDerivedTarget = .00001 / 350;
  private Logger log = Logger.getLogger(CircularizationRaisePeriApsis.class);

  public CircularizationRaisePeriApsis(NavComputer computer) {
    super(computer);

  }

  @Override
  public void initTargeter() {
    super.initTargeter();
    double apsis = ((Double) computer.getOrbitElements().get(OrbitElementKeys.rAph)).doubleValue();
    double mu = -PhysicsRunnable.G * ((Planet) referencedObject).getMass();
    double targetV = FastMath.sqrt(mu / (apsis * NavComputer.METERS_PER_MILE));
    deltaV = targetV - ((Double) computer.getOrbitElements().get(OrbitElementKeys.vAph)).doubleValue() * .44704;
    log.info(((Double) computer.getOrbitElements().get(OrbitElementKeys.vAph)).doubleValue() * .44704 + "...deltav needed=+" + deltaV / .44704);
  }

  @Override
  public boolean targetReached() {

    double apsis = ((Double) computer.getOrbitElements().get(OrbitElementKeys.rAph)).doubleValue();
    double oppositeApsis = ((Double) computer.getOrbitElements().get(OrbitElementKeys.rPer)).doubleValue();
    double radius = VMath.mag(VMath.vecSubtract(cs.getPositionVec(), referencedObject.getCoordSys().getPositionVec()));
    return (apsis - radius / NavComputer.METERS_PER_MILE) / (apsis - oppositeApsis) < deltaVDerivedTarget * deltaV;
  }

  @Override
  public void alignForManeuver() {
    ComputerAbstractButton button = computer.getButton(ComputerButtonKeys.PROGRADE);
    button.setSelected(true);
    button.doClick();
  }
}
