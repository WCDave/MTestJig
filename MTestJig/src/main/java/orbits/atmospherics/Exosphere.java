package orbits.atmospherics;


public class Exosphere implements IAtmosLevel {

  private static IAtmosLevel instance = new Exosphere();

  public static IAtmosLevel getInstance() {
    return instance;
  }

  @Override
  public double getTemperature(double h) {
    return -242;
  }

  @Override
  public double getPressure(double h) {
    return 0;
  }
}
