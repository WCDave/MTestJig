package orbits.atmospherics;


public class AtmosSphereLevelFactory {

  public static IAtmosLevel getLevel(double h) {
    if (h > 25000 && h <= 50000) {
      return UpperStratosphereLevel.getInstance();
    }
    if (h > 11000 && h <= 25000) {
      return StratosphereLevel.getInstance();
    }
    if (h <= 11000) {
      return TroposphereLevel.getInstance();
    }
    if (h > 50000 & h <= 80000) {
      return MesoSphere.getInstance();
    }
    if (h > 80000 && h <= 700000) {
      return ThermoSphere.getInstance();
    }
    if (h > 700000) {
      return Exosphere.getInstance();
    }
    return null;
  }
}
