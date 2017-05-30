package orbits;

import main.AbstractView;

import java.awt.*;


public class SkyDecorator extends Drawable3DDecorator {
  private final int r = 0, g = 191, b = 255;
  private final Color color = Color.BLACK;
  private Planet obj;

  public SkyDecorator(Drawable3D drawable3D, Abstract3DModelObject obj) {
    super(drawable3D);
    this.obj = (Planet) obj;
  }

  public void draw(AbstractView view) {
    Dimension d = view.getViewCanvas().getSize();
    //double factor = obj.getAtmosFactor().getAtmosDensityValue(1);
    //g2.setBackground(new Color((int)(r*factor), (int)(g*factor), (int)(b*factor)));
    Graphics2D g2 = view.getViewCanvas().getG2();
    g2.setBackground(color);
    g2.clearRect(0, 0, d.width, d.height);
  }
}
