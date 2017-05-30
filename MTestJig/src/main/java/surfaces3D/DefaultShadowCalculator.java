package surfaces3D;


import VMath.VMath;
import orbits.Facet;
import orbits.ShadowCalculator;
import orbits.Sphere;
import orbits.World3DContainer;
import org.apache.commons.math3.util.FastMath;

import java.util.List;

public class DefaultShadowCalculator implements IShadowCalculator<Facet> {

  private Sphere sphere;
  private static final double PI_DIV_2 = Math.PI / 2;

  public DefaultShadowCalculator(Sphere s) {
    sphere = s;
  }

  @Override
  public float isInShadow(double[] position) {
    double[] sunPosition = World3DContainer.getInstance().getLiteEmitter().getCoordSys().getPositionVec();
    double[] spherePos = sphere.getCoordSys().getPositionVec();
    double[] resultList = VMath.triangleSSS(VMath.mag(VMath.vecSubtract(spherePos, sunPosition)),
            VMath.mag(VMath.vecSubtract(position, spherePos)),
            VMath.mag(VMath.vecSubtract(position, sunPosition)));
    //el 1 comp with crit angle
    //el 2 comp with 90 degrees

    if (resultList[2] > PI_DIV_2) {
      double critAngle = FastMath.atan(sphere.getRadius() / VMath.mag(sunPosition));
      return resultList[1] < critAngle ? 1 : 0;
    }
    return 0;
  }

  @Override
  public float isInShadow(Facet f, IShadowProducer shadeProducer) {
    if (f.getComposedObject() == shadeProducer) return 0;
    return isInShadow(VMath.vecAdd(f.getComposedObject().getCoordSys().getPositionVec(), f.mv()));
  }
}
