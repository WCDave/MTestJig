package orbits.atmospherics;

import main.AbstractView;
import main.DrawingVisitor;
import orbits.Abstract3DModelObject;
import orbits.Facet;
import orbits.Planet;
import orbits.World3DContainer;

import java.awt.*;

public class EarthAtmosphereDraw implements IAtmosphereDraw {

  private static float[] dist;
  private static Color[] colors;
  private Planet earth;
  private AtmosphereShell atmosphereShell;

  static {
    dist = new float[]{0, 0.01f, 0.02f, 0.03f, 0.04f, 0.05f, 0.06f, 0.07f, 0.08f, 0.09f, 0.1f, 0.102f, 0.103f, 0.104f, 0.105f, .2f, .4f, .6f, .8f, 1.0f};
    colors = new Color[20];
    for (int i = 0; i < colors.length; i++) {
      int alpha = (int) (255.0 / (float) (i + 1)) - 1;
      alpha = i < 10 ? alpha : 0;
      colors[i] = new Color(Color.blue.getRed(), Color.blue.getGreen(), Color.blue.getBlue(), alpha);
      dist[i] = (float) (i + 1) / 20.0f;
    }
  }

  public EarthAtmosphereDraw(Planet p) {
    earth = p;
    atmosphereShell = new AtmosphereShell(p, 1.02f);
    atmosphereShell.setName("atmo");
    atmosphereShell.setDetailingFactor(5e5);
    atmosphereShell.setShadingVisitor(new DrawingVisitor() {
      @Override
      public Color visit(Facet f) {
        return f.getColor();
      }

  });
    World3DContainer.getInstance().addItemsAsArray(new Abstract3DModelObject[]{atmosphereShell});
  }


  @Override
  public void draw(AbstractView view) {
    atmosphereShell.draw(view);
  }

}
