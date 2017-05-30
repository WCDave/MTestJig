package orbits.atmospherics;


import orbits.Planet;
import org.apache.commons.math3.util.FastMath;

public class EarthAtmosFactor2 implements IAtmosFactor {
  private float sos = 0;
  private double T = 0;
  private double P = 0;

  public EarthAtmosFactor2(Planet p) {

  }

  @Override
  public double getAtmosDensityValue(double h) {

    IAtmosLevel level = AtmosSphereLevelFactory.getLevel(h);
    T = level.getTemperature(h);
    P = level.getPressure(h);

    double density = P / (.2869 * (T + 273.1));

    sos = (float) FastMath.sqrt(1.4 * (P * 10 / density)) * 10;

    return density;
  }

  @Override
  public double getAtmosSunScatteringValue() {
    return 0;
  }

  @Override
  public float getMachNumber(double speed) {
    return (float) (speed / sos);
  }

  @Override
  public float getTAT(double speed) {
    float M = getMachNumber(speed);
    return (float) (((1 + .2 * M * M) * (T + 273.1)) - 273.1);
  }


}
