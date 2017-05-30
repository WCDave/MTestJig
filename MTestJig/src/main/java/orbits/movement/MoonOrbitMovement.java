package orbits.movement;

import VMath.VMath;
import orbits.MoonOrbitHelper;
import orbits.Newtonian;
import org.apache.commons.math3.util.FastMath;

import java.util.Date;
import java.util.Map;

public class MoonOrbitMovement implements Movement {

  private Newtonian moon;

  public MoonOrbitMovement(Newtonian p) {
    moon = p;
  }

  @Override
  public void execute(double dt) {
    Date d = new Date();
    Map<String, Double> results = MoonOrbitHelper.calcGeoCoordsForDate(d);
    double dist = results.get("DIST");
    double longitude = results.get("LONG");
    double lat = results.get("LAT");

    double x = FastMath.cos(FastMath.toRadians(longitude)) * (float) FastMath.cos(FastMath.toRadians(lat)) * dist;
    double y = FastMath.sin(FastMath.toRadians(longitude)) * (float) FastMath.cos(FastMath.toRadians(lat)) * dist;
    double z = FastMath.sin(FastMath.toRadians(lat)) * dist;
    double[] newPosition = new double[]{x, y, z};
    moon.updatePosition(d, newPosition, VMath.vecSubtract(newPosition, moon.getPosition()));

  }

}
