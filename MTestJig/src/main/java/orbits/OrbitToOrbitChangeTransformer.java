package orbits;

import VMath.VMath;

public class OrbitToOrbitChangeTransformer implements IOrbitPlaneChangeTransformer {

  private Newtonian target;
  private Newtonian source;

  private double[] sourceVec, targetVec;

  private NavComputer computer;


  public OrbitToOrbitChangeTransformer(Newtonian source, Newtonian target, NavComputer computer) {
    this.source = source;
    this.target = target;
    this.computer = computer;
  }

  /**
   * computes normal vectors for the planes of both the target orbit and source orbit
   */
  @Override
  public void computeOrbitNormal() {
    OrbitToOrbitChangeTransformer.this.sourceVec = IOrbitPlaneChangeTransformer.computeOrbitalNormal(source, computer.getReferenceObject().getCoordSys());

    if (targetVec == null) {
      OrbitToOrbitChangeTransformer.this.targetVec = IOrbitPlaneChangeTransformer.computeOrbitalNormal(target, computer.getReferenceObject().getCoordSys());
    }
  }

  /**
   * compute node vector (will be descending node based on meaning of src and target)
   *
   * @return double[]
   */
  @Override
  public double[] getNodeVector() {
    return VMath.normalize(VMath.crossprd(sourceVec, targetVec));
  }

  @Override
  public double getOrbitalPlaneAlignment() {
    return VMath.dotprod(VMath.normalize(sourceVec), VMath.normalize(targetVec));
  }
}
