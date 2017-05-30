package orbits.atmospherics;


import org.apache.commons.math3.util.FastMath;

public class UpperStratosphereLevel implements IAtmosLevel {

  private static IAtmosLevel instance = new UpperStratosphereLevel();

  public static IAtmosLevel getInstance() {
    return instance;
  }

  @Override
  public double getTemperature(double h) {
    return -131.21 + .00299 * h;
  }

  @Override
  public double getPressure(double h) {
    return 2.488 * FastMath.pow((getTemperature(h) + 273.1) / 216.6, -11.388);
  }
}
