package orbits;

import domain.SolarSystemObject;
import main.AbstractView;

import java.awt.*;
import java.util.List;

public class PlanetBackgroundDecorator extends Drawable3DDecorator {
  private List<SolarSystemObject> objects;

  public PlanetBackgroundDecorator(List<SolarSystemObject> objects, Drawable3D d) {
    super(d);
    this.objects = objects;
  }

  @Override
  public void draw(AbstractView view) {
    super.draw(view);
    ScreenPoint planetVec;
    Dimension d = view.getViewCanvas().getScreenCenter();
    CoordSys viewingCoordSys = view.getCoordSys();
    for (SolarSystemObject obj : objects) {
      if (!"Sun".equals(obj.getName())) {
        planetVec = viewingCoordSys.getScrnCoords(obj.getGeoPositionVec(), d.width, d.height);
        if (planetVec.isInView()) {
          Graphics2D g2 = view.getViewCanvas().getG2();
          g2.setColor(obj.getColor());
          g2.fillOval(planetVec.x, planetVec.y, 4, 4);
          g2.drawString(obj.getName(), planetVec.x + 10, planetVec.y);
        }
      }
    }
  }
}
