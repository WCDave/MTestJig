package orbits;

public class Orbit {
//c ? L2/(GMm2) is a constant called the semilatus rectum

  private double c;
  private double eccentricity;

  public Orbit() {

  }

  public void setEccentricity(double eccentricity) {
    this.eccentricity = eccentricity;
  }

  public double getEccentricity() {
    return eccentricity;
  }

  public void setC(double c) {
    this.c = c;
  }

  public double getC() {
    return c;
  }
}
