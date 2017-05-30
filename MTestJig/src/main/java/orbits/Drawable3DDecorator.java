package orbits;

import main.AbstractView;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;


public class Drawable3DDecorator implements Drawable3D {
  private Drawable3D drawable3D;

  public Drawable3DDecorator(Drawable3D drawable3D) {
    this.drawable3D = drawable3D;
  }

  @Override
  public void draw(AbstractView view) {
    if (drawable3D != null)
      drawable3D.draw(view);
  }
}
