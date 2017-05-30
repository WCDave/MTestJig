package orbits.atmospherics;

import VMath.VMath;
import main.AbstractView;
import main.ColorModel;
import main.IDrawingVisitor;
import main.Shadeable;
import orbits.Facet;
import orbits.Planet;
import orbits.Sphere;

import java.awt.*;
import java.awt.image.BufferedImage;


public class AtmosphereShell extends Planet implements Shadeable {

  private Planet planet;
  private float radiusMultiple;
  private IDrawingVisitor<Facet> drawingVisitor;

  public AtmosphereShell(Planet p, float radiusMultiple) {
    this(null, new Dimension(4,4));
    planet = p;
    this.radiusMultiple = radiusMultiple;
    this.sSys = p.getCoordSys();
    this.setRadius(radiusMultiple * p.getRadius());
  }

  private  AtmosphereShell(BufferedImage image, Dimension faceSize) {
    super(image);
    this.buildFactor = 0;
  }

  @Override
  public void set3DObjectForDraw(AbstractView view) {
    //super.set3DObjectForDraw(view);

    synchronized (this.getFaceList()) {
      for(Facet face : this.getFaceList()) {
        face.setVectorFromView(VMath.vecAdd(view.getObjectVectorFromView(), face.mv()));
        getShadingVisitor().visit(face, view);
        //face.setColor(Color.RED);
        view.getDrawableList().add(face);
      }
    }
  }

    @Override
  public void draw(AbstractView view) {

  }

  @Override
  public IDrawingVisitor getShadingVisitor() {
    return drawingVisitor;
  }

  @Override
  public void setShadingVisitor(IDrawingVisitor v) {
    drawingVisitor = v;
  }

  @Override
  public ColorModel getColorModel() {
    return null;
  }

  @Override
  public Color getColor() {
    return planet.getColor();
  }
}
