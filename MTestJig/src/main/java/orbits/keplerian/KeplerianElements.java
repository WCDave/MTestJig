package orbits.keplerian;


public class KeplerianElements {

  double a;
  double e;
  double i;
  double raan;
  double aop;
  double ta;

  public KeplerianElements(double a, double e, double i, double raan, double aop, double ta) {
    this.a = a;
    this.e = e;
    this.i = i;
    this.raan = raan;
    this.aop = aop;
    this.ta = ta;
  }

  public double getA() {
    return a;
  }

  public double getE() {
    return e;
  }

  public double getI() {
    return i;
  }

  public double getRaan() {
    return raan;
  }

  public double getAop() {
    return aop;
  }

  public double getTa() {
    return ta;
  }
}
