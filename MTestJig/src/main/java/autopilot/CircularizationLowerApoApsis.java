package autopilot;

import VMath.VMath;
import enums.ComputerButtonKeys;
import enums.OrbitElementKeys;
import gui.ComputerAbstractButton;
import main.PhysicsRunnable;
import orbits.NavComputer;
import orbits.Planet;
import org.apache.commons.math3.util.FastMath;
import org.apache.log4j.Logger;

public class CircularizationLowerApoApsis extends AbstractCircularizationTarget {

  private double deltaV;
  private static final double deltaVDerivedTarget = .00002 / 350;
  private Logger log = Logger.getLogger(CircularizationLowerApoApsis.class);

  public CircularizationLowerApoApsis(NavComputer computer) {
    super(computer);

  }

  @Override
  public void initTargeter() {
    super.initTargeter();
    double periApsis = ((Double) computer.getOrbitElements().get(OrbitElementKeys.rPer)).doubleValue();
    double mu = -PhysicsRunnable.G * ((Planet) referencedObject).getMass();
    double targetV = FastMath.sqrt(mu / (periApsis * NavComputer.METERS_PER_MILE));
    deltaV = ((Double) computer.getOrbitElements().get(OrbitElementKeys.vPer)).doubleValue() * .44704 - targetV;
    log.info(((Double) computer.getOrbitElements().get(OrbitElementKeys.vPer)).doubleValue() * .44704 + "...deltav needed=-" + deltaV / .44704);
  }

  @Override
  public boolean targetReached() {
    double periApsis = (Double) computer.getOrbitElements().get(OrbitElementKeys.rPer);
    double radius = VMath.mag(VMath.vecSubtract(cs.getPositionVec(), referencedObject.getCoordSys().getPositionVec())) / NavComputer.METERS_PER_MILE;
    //System.out.println(new Date()+" "+Math.abs(radius - periApsis));
    return Math.abs(radius - periApsis) < VMath.mag(cs.getVelocityAsVec()) * .0002236f;
  }


  @Override
  public void alignForManeuver() {
    ComputerAbstractButton button = computer.getButton(ComputerButtonKeys.RETROGRADE);
    button.setSelected(true);
    button.doClick();
  }

}
