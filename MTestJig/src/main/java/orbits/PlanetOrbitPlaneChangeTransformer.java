package orbits;


import VMath.VMath;

public class PlanetOrbitPlaneChangeTransformer implements IOrbitPlaneChangeTransformer {

  private Newtonian orbitingObject;
  private double[] targetNormal;

  private double[] sourceVec, targetVec;

  private NavComputer computer;

  public PlanetOrbitPlaneChangeTransformer(Newtonian orbitingObject, double[] targetNormal, NavComputer computer) {
    this.orbitingObject = orbitingObject;
    this.targetNormal = targetNormal;
    this.computer = computer;
  }

  @Override
  public void computeOrbitNormal() {
    this.sourceVec = IOrbitPlaneChangeTransformer.computeOrbitalNormal(orbitingObject, computer.getReferenceObject().getCoordSys());
  }

  @Override
  public double[] getNodeVector()  {
    return VMath.normalize(VMath.crossprd(sourceVec, targetNormal));
  }

  @Override
  public double getOrbitalPlaneAlignment() {
    return VMath.dotprod(VMath.normalize(sourceVec), targetNormal);
  }
}
