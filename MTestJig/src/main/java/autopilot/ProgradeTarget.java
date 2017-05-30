package autopilot;

import java.util.ArrayList;
import java.util.List;

import orbits.Abstract3DModelObject;
import orbits.NavComputer;
import orbits.Newtonian;
import VMath.VMath;

public class ProgradeTarget extends OnOrbitAlignmentStrategy {

  public ProgradeTarget(NavComputer computer) {
    super(computer);
  }

  @Override
  public List<double[]> acquireTarget() {
    double[] zTarget, yTarget, xTarget;
    List<double[]> result = new ArrayList<double[]>(3);

    Abstract3DModelObject planet = computer.getReferenceObject();
    zTarget = VMath.normalize(VMath.vecSubtract(cs.getVelocityAsVec(), ((Newtonian) planet).getVelocity()));
    yTarget = VMath.normalize(VMath.crossprd(VMath.vecSubtract(cs.getPositionVec(), refSys.getPositionVec()), zTarget));
    xTarget = VMath.crossprd(yTarget, zTarget);
    if (VMath.dotprod(yTarget, planet.getCoordSys().zAxis().getVectorForm()) < 0) {
      xTarget = VMath.vecMultByScalar(xTarget, -1);
      yTarget = VMath.vecMultByScalar(yTarget, -1);
    }

    result.add(xTarget);
    result.add(yTarget);
    result.add(zTarget);

    return result;
  }

}
