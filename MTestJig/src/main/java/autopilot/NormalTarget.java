package autopilot;

import java.util.List;
import java.util.Vector;

import orbits.NavComputer;
import orbits.Planet;
import VMath.VMath;

public class NormalTarget extends OnOrbitAlignmentStrategy {

  public NormalTarget(NavComputer computer) {
    super(computer);
  }

  @Override
  public List<double[]> acquireTarget() {
    double[] zTarget, yTarget;
    List<double[]> result = new Vector<double[]>(3);

    Planet p = (Planet) computer.getReferenceObject();
    yTarget = VMath.normalize(VMath.vecMultByScalar(VMath.vecSubtract(cs.getVelocityAsVec(), p.getVelocity()), -1));
    zTarget = VMath.normalize(VMath.crossprd(yTarget, VMath.vecSubtract(cs.getPositionVec(), refSys.getPositionVec())));

    result.add(VMath.crossprd(yTarget, zTarget));
    result.add(yTarget);
    result.add(zTarget);

    return result;
  }

}
