package orbits.atmospherics;


import org.apache.commons.math3.util.FastMath;

public class TroposphereLevel implements IAtmosLevel {

  private static IAtmosLevel instance = new TroposphereLevel();

  public static IAtmosLevel getInstance() {
    return instance;
  }

  @Override
  public double getTemperature(double h) {
    return 15.04 - .00649 * h;
  }

  @Override
  public double getPressure(double h) {
    //return 101.29 * FastMath.pow((getTemperature(h) + 273.1) / 288.08, 5.256);
    return 101.29 * FastMath.pow((getTemperature(h) + 273.1) / 288.08, 5.256);
  }
}
