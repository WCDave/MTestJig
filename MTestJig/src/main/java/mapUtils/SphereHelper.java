package mapUtils;


import orbits.Planet;
import orbits.Sphere;
import orbits.World3DContainer;
import org.apache.commons.math3.geometry.euclidean.threed.SphericalCoordinates;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;

import java.awt.*;

public class SphereHelper {
  public static final double PI_DIV_2 =Math.PI/2;

  //movePoint(World3DContainer.getInstance().getLaunchPoint(), 90, 200, (Planet)anArray[2]);
  public static double[] movePoint(double[] point, double bearing, double distance, Sphere s) {
    /*
    φ2 = asin( sin φ1 ⋅ cos δ + cos φ1 ⋅ sin δ ⋅ cos θ )
λ2 = λ1 + atan2( sin θ ⋅ sin δ ⋅ cos φ1, cos δ − sin φ1 ⋅ sin φ2 )
     */
    bearing = Math.toRadians(bearing);
    SphericalCoordinates sc = new SphericalCoordinates(new Vector3D(point));
    double lat = PI_DIV_2 - sc.getPhi();
    double longitude = sc.getTheta();
     double d_div_r = distance / s.getRadius();

    double newLat = FastMath.asin( FastMath.sin(lat)*FastMath.cos(d_div_r) +  FastMath.cos(lat)*FastMath.sin(d_div_r)*FastMath.cos(bearing) );
    double newLong = longitude + FastMath.atan2(FastMath.sin(bearing)*FastMath.sin(d_div_r)*FastMath.cos(lat), FastMath.cos(d_div_r)-FastMath.sin(lat)*FastMath.sin(newLat));

    return new SphericalCoordinates(s.getRadius(), newLong, PI_DIV_2 - newLat).getCartesian().toArray();

  }

  public static void main(String[] args) {
    Planet p  = new  Planet((float) 5.99e24, (float) 6.37e6, Color.blue, new double[]{0, 0, 4.1780746480349296325186049083789e-6, 0},true, null, null);
    double[] result = SphereHelper.movePoint(new double[] {1,0,0}, 270, 1, p);
    int i=0;
  }
}
