package orbits.atmospherics;


import org.apache.commons.math3.util.FastMath;

public class StratosphereLevel implements IAtmosLevel {

  private static IAtmosLevel instance = new StratosphereLevel();

  public static IAtmosLevel getInstance() {
    if (instance == null) {
      instance = new StratosphereLevel();
    }
    return instance;
  }

  @Override
  public double getTemperature(double h) {
    return -56.46;
  }

  @Override
  public double getPressure(double h) {
    return 22.65 * FastMath.exp(1.73 - .000157 * h);
  }
}
