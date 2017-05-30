package orbits;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import VMath.VMath;
import org.apache.commons.math3.util.FastMath;

public final class MoonOrbitHelper {

  private static final long initMs;
  private static final Map<String, double[]> propertyMap;
  private static MoonOrbitHelper instance = new MoonOrbitHelper();

  static {
    Calendar cal = Calendar.getInstance();
    cal.set(2000, 1, 1);
    initMs = cal.getTimeInMillis();

    propertyMap = new HashMap<String, double[]>(3);

    propertyMap.put("LC", new double[]{218.316d, 13.176396d});
    propertyMap.put("MC", new double[]{134.963d, 13.064993d});
    propertyMap.put("FC", new double[]{93.272d, 13.229350d});
  }

  private MoonOrbitHelper() {

  }

  public static MoonOrbitHelper getInstance() {
    return instance;
  }

  public static void main(String[] args) {
    Calendar initDate = Calendar.getInstance();
    initDate.setTimeInMillis(new Date().getTime());
    for (int i = 0; i < 10000; i++) {
      initDate.add(Calendar.DAY_OF_YEAR, 1);
      Date d = new Date();
      Map<String, Double> results = calcGeoCoordsForDate(d);
      double dist = results.get("DIST");
      double longitude = results.get("LONG");
      double lat = results.get("LAT");
      System.out.println(d.getTime() + " long=" + longitude + " , lat=" + lat + " ,dist=" + dist);
    }
  }

  private static double calcValueForProperty(String property, Date d) {
    double[] values = propertyMap.get(property);
    return values[0] + values[1] * ((d.getTime() - initMs) / 86400000d);
  }

  public static Map<String, Double> calcGeoCoordsForDate(Date d) {
    Map<String, Double> result = new HashMap<String, Double>(3);

    double M = FastMath.toRadians(calcValueForProperty("MC", d));
    double F = FastMath.toRadians(calcValueForProperty("FC", d));
    double L = calcValueForProperty("LC", d);

    double longitude = L + 6.289d * Math.sin(M);
    longitude = VMath.modulo(longitude + 50d, 360d);
    double lat = 5.128d * FastMath.sin(F);
    double dist = 385001000d - 20905000d * FastMath.cos(M);

    result.put("LONG", new Double(longitude));
    result.put("LAT", new Double(lat));
    result.put("DIST", new Double(dist));

    return result;
  }

  public static double[] determineAscendingNodeVector(double[] normal, double[] refNormal) {
    double[] result = {0, 0, 0, 0};
    result = VMath.crossprd(refNormal, normal);
    return result;
  }

  /*
   *     omega = arccos ((n dot e)/ (ne)
   */
  public double determineArgOfPeriApsis() {
    double result = 0;
    return result;
  }

}
