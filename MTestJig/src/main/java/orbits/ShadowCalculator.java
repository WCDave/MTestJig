package orbits;


public class ShadowCalculator {

  public static double getShadowFactor(Planet p, Abstract3DModelObject testObject, Abstract3DModelObject lightSource) {
    //to what degree is testObject shadowed from light source lightSource by planet p
    double[] testObjPosition = testObject.getCoordSys().getPositionVec();
    double[] lightSourcePosition = lightSource.getCoordSys().getPositionVec();
    double[] pPosition = p.getPosition();
    double pRadius = p.getRadius();
    return 0;
  }
}
