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
  private int targetCounter;

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

    double mu = -PhysicsRunnable.G * ((Planet) referencedObject).getMass();
    double targetV = FastMath.sqrt(mu / (periApsis * NavComputer.METERS_PER_MILE));
    deltaV = ((Double) computer.getOrbitElements().get(OrbitElementKeys.vPer)).doubleValue() * .44704 - targetV;
    //System.out.println(Math.abs(radius - periApsis)+", deltaV="+deltaV);
    //return Math.abs(radius - periApsis) < VMath.mag(cs.getVelocityAsVec()) * .0002236f;
    boolean result = false;

    if(Math.abs(radius - periApsis) < Math.abs(deltaV)/1900) {
      targetCounter++;
      result = targetCounter > 3;
    }
    else {
      targetCounter = 0;
    }
    return result;
  }


  @Override
  public void alignForManeuver() {
    ComputerAbstractButton button = computer.getButton(ComputerButtonKeys.RETROGRADE);
    button.setSelected(true);
    button.doClick();
    targetCounter = 0;
  }
}
