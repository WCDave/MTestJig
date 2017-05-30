package orbits.atmospherics;


import org.apache.commons.math3.util.FastMath;

public class MesoSphere implements IAtmosLevel {

  private static IAtmosLevel instance = new MesoSphere();

  public static IAtmosLevel getInstance() {
    return instance;
  }

  @Override
  public double getTemperature(double h) {
    //-5 at -90 50k thru 80k
    if (h < 52500) {
      return -5 - .00283333 * (h - 52500);
    } else {
      return -5;
    }
  }

  @Override
  public double getPressure(double h) {
    return 22.65 * FastMath.exp(1.73 - .000157 * h);
  }
}
