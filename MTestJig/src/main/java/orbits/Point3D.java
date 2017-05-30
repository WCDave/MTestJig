package orbits;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.List;

import main.AbstractView;
import VMath.VMath;

public class Point3D extends Abstract3DModelObject {

  private double[] position;
  private Color color;

  public Point3D(double x, double y, double z, Color color) {
    this(new double[]{x, y, z}, color);
  }

  public Point3D(double[] point, Color color) {
    position = point;
    this.color = color;
  }

  @Override
  public void draw(AbstractView view) {
    Dimension d = view.getViewCanvas().getScreenCenter();
    CoordSys viewSys = view.getCoordSys();
    ScreenPoint screenPoint = viewSys.getScrnCoords(position, d.width, d.height);

    if (screenPoint.isInView()) {
      Graphics2D g2 = view.getViewCanvas().getG2();
      g2.setColor(color);
      g2.drawLine(screenPoint.x, screenPoint.y, screenPoint.x, screenPoint.y);
    }
  }


  public double[] getPosition() {
    return position;
  }

  @Override
  public void set3DObjectForDraw(AbstractView view) {
    CoordSys viewSys = view.getCoordSys();
    setVectorFromView(VMath.vecSubtract(position, viewSys.getPositionVec()));
    view.getDrawableList().add(this);
  }

}
