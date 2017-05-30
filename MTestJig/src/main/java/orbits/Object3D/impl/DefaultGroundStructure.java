package orbits.Object3D.impl;

import main.AbstractView;
import main.ColorModel;
import main.IDrawingVisitor;
import main.Shadeable;
import orbits.Abstract3DModelObject;
import orbits.Composed3DObject;
import orbits.Facet;
import orbits.IDetailer;
import orbits.Object3D.IObjectBuilder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class DefaultGroundStructure extends AbstractGroundStructure implements Composed3DObject<Facet>, Shadeable {

  private int length;
  private int width;
  private int height;
  private List<Facet> faceList;
  private ColorModel colorModel;
  private IDrawingVisitor drawingVisitor;

  public DefaultGroundStructure(int length, int width, int height, double[] location, Color color, Abstract3DModelObject parent) {
    super(location, parent);
    this.length = length;
    this.width = width;
    this.height = height;
    faceList = new ArrayList<Facet>(100);
    this.color = color;
    this.name = "Muddy";
    colorModel = ColorModel.DEFAULT_COLOR_MODEL;

    drawingVisitor = new IDrawingVisitor<Facet>() {
      @Override
      public Color visit(Facet f) {
        return f.getColor();
      }

      @Override
      public void visit(Facet facet, AbstractView view) {

      }
    };
  }

  @Override
  public IDetailer<Facet> getDetailer() {
    return null;
  }

  @Override
  public List<Facet> getFaceList() {
    return faceList;
  }

  @Override
  public void draw(AbstractView view) {
//    synchronized (faceList) {
//      for (Facet f : faceList) {
//        double[] normMv = VMath.normalize(f.mv());
//        UVector u = new UVector(normMv[0], normMv[1], normMv[2]);
//        Graphics2D g2 = view.getViewCanvas().getG2();
//        u.setColor(f.getColor());
//        CoordSys cs = view.getCoordSys();
//        Dimension d = view.getViewCanvas().getSize();
//        double[] posVec = ((DefaultGroundStructure)f.getComposedObject()).getGeometricCenter();
//        u.draw(posVec, cs, d, g2, 250);
//      }
//    }
  }

  @Override
  public void set3DObjectForDraw(AbstractView view) {
    vectorFromView = view.getObjectVectorFromView();
    synchronized (faceList) {
//      Color oldColor = this.color;
//      faceList.get(0).getComposedObject().setColor(new Color(oldColor.getRed(), oldColor.getGreen(), oldColor.getBlue(), 64));
      for (Facet f : faceList) {
        //Color c = f.getColor();
        f.set3DObjectForDraw(view);
        //f.setColor((new Color(c.getRed(), c.getGreen(), c.getBlue(), 128)));
        f.setVectorFromView(vectorFromView);
      }
    }
    super.set3DObjectForDraw(view);
  }

  @Override
  public void rebuild(int factor) {

  }

  @Override
  public double getDetailingDistance() {
    return 0;
  }

  @Override
  public int getBuildFactor() {
    return 0;
  }


  public int getHeight() {
    return height;
  }

  public int getLength() {
    return length;
  }

  public int getWidth() {
    return width;
  }


  @Override
  public IObjectBuilder<AbstractGroundStructure> getBuilder() {
    return new DefaultGroundStructureBuilder(this);
  }

  @Override
  public IDrawingVisitor getShadingVisitor() {
    return drawingVisitor;
  }

  @Override
  public void setShadingVisitor(IDrawingVisitor v) {

  }

  @Override
  public ColorModel getColorModel() {
    return colorModel;
  }

  @Override
  public Vector getDataList() {
    return null;
  }

  @Override
  public double getDragFactor() {
    return 0;
  }

  @Override
  public void setPositionVec(double[] position) {
    new Thread(() -> {
      getBuilder().build();
    }).start();
    super.setPositionVec(position);
  }
}
