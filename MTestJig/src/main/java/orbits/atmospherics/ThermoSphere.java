package orbits.atmospherics;


import org.apache.commons.math3.util.FastMath;

public class ThermoSphere implements IAtmosLevel {

  private static IAtmosLevel instance = new ThermoSphere();

  public static IAtmosLevel getInstance() {
    return instance;
  }

  @Override
  public double getTemperature(double h) {
    if (h > 90000) {
      return -90 + .0045 * (h - 90000);
    } else {
      return -90;
    }
  }

  @Override
  public double getPressure(double h) {
    return 22.65 * FastMath.exp(1.73 - .000157 * h);
  }
}
