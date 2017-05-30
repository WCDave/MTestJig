package orbits;

import VMath.VMath;
import main.AbstractView;
import main.ColorModel;
import main.IDrawingVisitor;
import main.Shadeable;
import surfaces3D.IShadowCalculator;
import surfaces3D.IShadowProducer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.List;

public abstract class Abstract3DModelObject implements Comparable<Abstract3DModelObject>, Drawable3D, Serializable, IShadowProducer {

  protected double[] vectorFromView;
  protected Color color;
  protected Color shadowColor = new Color(50, 50, 50);
  protected CoordSys sSys;
  protected boolean drawFaceOutlines;
  protected String name;
  protected boolean coordSysDraw = false;
  private double detailingFactor = 1;
  transient private IShadowCalculator shadowCalculator;

  public Abstract3DModelObject() {
    sSys = new CoordSys(0, 0, 0);
    vectorFromView = new double[]{0, 0, 0, 0};
  }

  public Abstract3DModelObject(CoordSys cs, Color color) {
    this.color = color;
    this.sSys = cs;
  }

  public void set3DObjectForDraw(AbstractView view) {
    view.getDrawableList().add(this);
  }

  public int compareTo(Abstract3DModelObject o) {
    if (o == null) return 0;
    if (VMath.mag(vectorFromView) > VMath.mag(o.vectorFromView))
      return -1;
    else
      return 1;
  }

  public void setVectorFromView(double[] vectorFromView) {
    this.vectorFromView = vectorFromView;
  }

  public void setColor(Color aColor) {
    color = aColor;
    return;
  }

  public Color getColor() {
    return color;
  }

  public void setDrawFaceOutlines(boolean drawFaceOutlines) {
    this.drawFaceOutlines = drawFaceOutlines;
  }

  public CoordSys getCoordSys() {
    return sSys;
  }

  public String getName() {
    return name;
  }

  public void setName(String aNameString) {
    name = aNameString;
    return;
  }

  /**
   * @param detailingFactor the detailingFactor to set
   */
  public void setDetailingFactor(double detailingFactor) {
    this.detailingFactor = detailingFactor;
  }

  /**
   * @return the detailingFactor
   */
  public double getDetailingFactor() {
    return detailingFactor;
  }


  @Override
  public IShadowCalculator getShadowCalculator() {
    return shadowCalculator;
  }

  public void setShadowCalculator(IShadowCalculator shadowCalculator) {
    this.shadowCalculator = shadowCalculator;
  }

  public double[] getVectorFromView() {
    return vectorFromView;
  }

  public static Abstract3DModelObject MINIMAL_IMPL = new STUBA3O();

  public static class STUBA3O extends Abstract3DModelObject implements Shadeable, Composed3DObject<Facet> {

    Color shadowColor = Color.white;

    public STUBA3O() {

    }

    @Override
    public void draw(AbstractView view) {

    }

    @Override
    public IDrawingVisitor getShadingVisitor() {
      return new IDrawingVisitor<Facet>() {
        @Override
        public Color visit(Facet f) {
          return f.color;
        }

        @Override
        public void visit(Facet facet, AbstractView view) {

        }
      };
    }

    @Override
    public void setShadingVisitor(IDrawingVisitor v) {

    }

    @Override
    public ColorModel getColorModel() {
      return null;
    }

    @Override
    public List<Facet> getFaceList() {
      return null;
    }

    @Override
    public IDetailer<Facet> getDetailer() {
      return null;
    }
  }


}
