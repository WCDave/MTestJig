package orbits.atmospherics;

import VMath.VMath;
import main.AbstractView;
import orbits.CoordSys;
import orbits.Planet;
import orbits.ScreenPoint;
import orbits.atmospherics.IAtmosphereDraw;
import org.apache.commons.math3.util.FastMath;

import java.awt.*;

public class SunAtmosphereDraw extends Planet implements IAtmosphereDraw{

  private static float[] dist;
  private static Color[] colors;
  private Planet sun;

  static {
    dist = new float[20];
    colors = new Color[20];
    for (int i = 0; i < colors.length; i++) {
      int alpha = (int) (255.0 / (float) (i + 1)) - 1;
      alpha = i < 10 ? alpha : 0;
      colors[i] = new Color(Color.yellow.getRed(), Color.yellow.getGreen(), Color.yellow.getBlue(), alpha);
      dist[i] = (float) (i + 1) / 20.0f;
    }
  }

  public SunAtmosphereDraw(Planet p) {
    super(null);
    sun = p;
  }

  @Override
  public void draw(AbstractView view) {
    Graphics2D g2 = view.getViewCanvas().getG2();
    Dimension d = view.getViewCanvas().getScreenCenter();
    ScreenPoint screen2DVec = view.getCoordSys().getScrnCoords(sun.getCoordSys().getPositionVec(), d.width, d.height);
    if (screen2DVec.isInView()) {
      double distanceToPlanet = VMath.mag(sun.getVectorFromView());
      float radius = (int) (25000 * 5 / 3 * FastMath.atan(sun.getRadius() / distanceToPlanet) / FastMath.PI / 3);
      RadialGradientPaint p = new RadialGradientPaint(screen2DVec, FastMath.max(1, 2 * radius), dist, colors);
      g2.setPaint(p);
      g2.fillOval(screen2DVec.x - (int) (radius / 2), screen2DVec.y - (int) (radius / 2), (int) radius, (int) radius);
    }
    //sun.draw(view);
  }

  @Override
  public void set3DObjectForDraw(AbstractView aView) {
    sun.set3DObjectForDraw(aView);
  }

  @Override
  public String getName() {
    return sun.getName();
  }

  @Override
  public CoordSys getCoordSys() {
    return sun.getCoordSys();
  }
}
