package orbits.keplerian;

public class Orbit implements IOrbit {


  private double[][] positionVectors;
  private double[] midVelVec;

  private KeplerianElements keplerianElements;

  public Orbit(double[][] positionVectors, double[] midVelVec) {
    this.positionVectors = positionVectors;
    this.midVelVec = midVelVec;
  }

  public Orbit(KeplerianElements ke) {
    keplerianElements = ke;
  }

  public double[][] getPositionVectors() {
    return positionVectors;
  }

  public void setPositionVectors(double[][] positionVectors) {
    this.positionVectors = positionVectors;
  }

  public double[] getMidVelVec() {
    return midVelVec;
  }

  public void setMidVelVec(double[] midVelVec) {
    this.midVelVec = midVelVec;
  }

  @Override
  public KeplerianElements getKeplerianElements() {
    return keplerianElements;
  }
}
