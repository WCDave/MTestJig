package autopilot;

import orbits.NavComputer;
import orbits.PlanetOrbitPlaneChangeTransformer;


public class PlanetPlaneChangePlaneAlignProgram extends OrbitPlaneAlignProgram {

  public PlanetPlaneChangePlaneAlignProgram(NavComputer computer, double[] targetNormal) {
    super(computer);
    if(targetNormal == null) {
      targetNormal = computer.getReferenceObject().getCoordSys().zAxis().getVectorForm();
    }
    transformer = new PlanetOrbitPlaneChangeTransformer(rocket, targetNormal, computer);
  }

}
