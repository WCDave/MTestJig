package orbits.atmospherics;


public interface IAtmosFactor {

  double getAtmosDensityValue(double distance);

  double getAtmosSunScatteringValue();

  float getMachNumber(double speed);

  float getTAT(double speed);
}
