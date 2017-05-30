package mapUtils;

import VMath.VMath;
import orbits.Facet;
import orbits.Planet;
import org.apache.commons.math3.util.FastMath;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Calendar;

public class MapHelper {

  private static final float RECT_SIZE = 10;
  private static final double TWO_PI = Math.PI * 2;
  private static final double PI_OVER_180 = Math.PI / 180;
  private static final double PI_OVER_12 = Math.PI / 12;

  private static Logger log = Logger.getLogger(MapHelper.class);
  static double testTime;

  static {

    Calendar c = Calendar.getInstance();
    int hr = c.get(Calendar.HOUR_OF_DAY);
    int min = c.get(Calendar.MINUTE);
    int sec = c.get(Calendar.SECOND);
    int ms = c.get(Calendar.MILLISECOND);
    testTime = hr + min / 60.0 + sec / 3600.0 + ms / (3600000.0);

  }

  //GFXFramework gfx;


//	public static void main(String[] args) throws GFXException
//	{
//		MapHelper mh = new MapHelper();
//		JFrame jf = new JFrame();
//		jf.setPreferredSize(new Dimension(50,50));
//		mh.gfx = new GFXFramework(jf, false);
//		mh.gfx.init();
//		mh.gfx.setVisible(true);
//	}

  public static double[] getFlatMapCoordsFor(Facet f, Dimension dim) {
    //double[] vec3D = f.getComposedObject().getCoordSys().rotatePointAroundAxis(AxisType.X, f.mv(), -((Planet)f.getComposedObject()).getObliquity());
    double[] vec3D = VMath.transform(f.mv(), f.getComposedObject().getCoordSys().getInverseMatrix());
    double x = vec3D[0];
    double y = vec3D[1];
    double z = vec3D[2];

    //Map<String, Double> result = new HashMap<String,Double>();
    double theta = FastMath.acos(x / FastMath.sqrt(x * x + y * y));

    if (y < 0) {
      theta = TWO_PI - theta;
    }
    // position rocket's longitude  //+ 72*Math.PI/180
    theta = (theta + (-getDecimalTimeOfDay() * PI_OVER_12 + 555 * PI_OVER_180)) % TWO_PI;

    double phi = FastMath.atan(z / (Math.sqrt(x * x + y * y)));

    // LN(tan(x)+sec(x))
    // result[1] = getHeight()/2 * (1 - Math.log(Math.abs(Math.tan(phi)+
    // 1/Math.cos(phi))));
    // Math.log(Math.abs(Math.tan(phi)+      1/Math.cos(phi))
    //double yy = dim.height / 2 - dim.height / 2 * Math.log(Math.tan(phi)+1/Math.cos(phi));

//        result.put("Y", new Double(dim.height / 2 * (1 - Math.sin(phi))));
//        //System.out.println(result.get("Y")) ;
//        result.put("X", new Double(dim.width * theta / (TWO_PI)));
//        result.put("THETA", new Double(Math.toDegrees(theta)));
//        result.put("PHI", new Double(Math.toDegrees(phi)));
    return new double[]{dim.width * theta / (TWO_PI), dim.height / 2 * (1 - Math.sin(phi))};
  }

  public static double getDecimalTimeOfDay() {
    Calendar c = Calendar.getInstance();
    int hr = c.get(Calendar.HOUR_OF_DAY);
    int min = c.get(Calendar.MINUTE);
    int sec = c.get(Calendar.SECOND);
    int ms = c.get(Calendar.MILLISECOND);
    double result = (hr + min / 60.0 + sec / 3600.0 + ms / (3600000.0));
    return result;
  }

  public static Color getColorFromMapForFace(BufferedImage map, Facet f) {
    Color resultColor = null;
    double[] result = getFlatMapCoordsFor(f, new Dimension(map.getWidth(), map.getHeight()));
//        double x = result[0];
//        double y = result[1];
    //double phi = Math.toRadians(resultMap.get("PHI"));
    //double theta = Math.toRadians(resultMap.get("THETA"));

    //double expFact = Math.exp(-distance/100);
    //int rectSize = Math.max(Math.round((float)(RECT_SIZE*(1 - expFact))),1);
    //int rectSize = 1;

    //log.debug("RectSize="+rectSize);


//        int ulx = (int) (x-rectSize/2);
//        int uly = (int) (y-rectSize/2);

    try {

      resultColor = new Color(map.getRGB((int) result[0], (int) result[1]));
    } catch (Exception e) {
      resultColor = Color.WHITE;
      //log.error("maphelper raster error");
    }
    //System.out.println("rectsize= "+rectSize+" "+resultColor);
    return resultColor;
  }


  public static double[] convertGeographicalRadiansToCartesianCoords(double radius, double latitude, double longitude) {
    double[] result = new double[3];

    result[0] = radius * FastMath.cos(latitude) * FastMath.cos(longitude);
    result[1] = radius * FastMath.cos(latitude) * FastMath.sin(longitude);
    result[2] = radius * FastMath.sin(latitude);
    return result;
  }

  public static double[] convertGeographicalDegreesToCartesianCoords(double radius, double latitude, double longitude) {
    return convertGeographicalRadiansToCartesianCoords(radius, FastMath.toRadians(latitude), FastMath.toRadians(longitude));
  }

  public static double[] convertGeographicalDegreesToCartesianCoords(Planet p, double latitude, double longitude) {
    double[] result = convertGeographicalRadiansToCartesianCoords(p.getRadius(), FastMath.toRadians(latitude), FastMath.toRadians(longitude));
    return VMath.transform(result, p.getCoordSys().getInverseMatrix());
    //return VMath.transform(result, p.getCoordSys().getCoordSysAsMatrix());
    //return p.getCoordSys().rotatePointAroundAxis(AxisType.X, result, latitude);
    //return result;
  }
}
