package autopilot;

import java.util.List;
import java.util.Vector;

import orbits.NavComputer;
import orbits.Planet;
import VMath.VMath;

public class RetrogradeTarget extends OnOrbitAlignmentStrategy {

  public RetrogradeTarget(NavComputer computer) {
    super(computer);
  }

  @Override
  public List<double[]> acquireTarget() {
    double[] zTarget, yTarget;
    List<double[]> result = new Vector<double[]>(3);

    Planet p = (Planet) computer.getReferenceObject();
    zTarget = VMath.vecMultByScalar(VMath.normalize(VMath.vecSubtract(cs.getVelocityAsVec(), p.getVelocity())), -1);
    yTarget = VMath.normalize(VMath.crossprd(zTarget, VMath.vecSubtract(cs.getPositionVec(), refSys.getPositionVec())));

    result.add(VMath.crossprd(yTarget, zTarget));
    result.add(yTarget);
    result.add(zTarget);

    return result;
  }

}
