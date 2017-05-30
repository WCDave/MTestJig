package orbits.atmospherics;


public interface IAtmosLevel {

  double getTemperature(double h);

  double getPressure(double h);
}
