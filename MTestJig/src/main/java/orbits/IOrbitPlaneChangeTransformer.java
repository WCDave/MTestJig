package orbits;

import Foundation.Utils;
import VMath.VMath;

/**
 * Created by DaveMain on 3/18/2017.
 */
public interface IOrbitPlaneChangeTransformer {
  void computeOrbitNormal();

  double[] getNodeVector();

  double getOrbitalPlaneAlignment();

  static double[] computeOrbitalNormal(Newtonian object, CoordSys centralSys) {
    double[] vec1 = VMath.vecSubtract(object.getPosition(), centralSys.getPositionVec());
    //Utils.sleep(1000);
    double[] vec2 = VMath.vecSubtract(object.getVelocity(), centralSys.getVelocityAsVec());
    return VMath.crossprd(vec1, vec2);
  }
}
