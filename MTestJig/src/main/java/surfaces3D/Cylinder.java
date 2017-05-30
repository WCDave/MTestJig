package surfaces3D;

import VMath.VMath;
import main.AbstractView;
import main.ColorModel;
import main.IDrawingVisitor;
import main.SatelliteDrawingVisitor;
import main.Shadeable;
import orbits.Abstract3DModelObject;
import orbits.Composed3DObject;
import orbits.DefaultFaceDetailer;
import orbits.Facet;
import orbits.IDetailer;
import orbits.NavComputer;
import orbits.Object3D.impl.CylinderBuilder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Cylinder extends Abstract3DModelObject implements Composed3DObject<Facet>, Shadeable {

  private int radius;
  private int totalHeight;

  private Dimension facetDim; //holds the Facet Sizes. width = Theta, height = facet height
  private List<Facet> faceList;
  private double OUTLINELIMIT = 30000 * NavComputer.METERS_PER_MILE; //30,000 miles
  private boolean cappedEnds = true;
  private int numFaces = 122;
  private IDrawingVisitor drawingVisitor = new SatelliteDrawingVisitor();
  private IDetailer<Facet> detailer;
  private ColorModel colorModel;

  public Cylinder(int radius, int totalHeight, Dimension facetDim) {
    super();
    colorModel = new ColorModel() {
      @Override
      public Color getShadeForLightSourceFactor(Color color, float lightSourceFactor) {
        return null;
      }

      @Override
      public BufferedImage getModelImage() {
        return null;
      }
    };
    this.radius = radius;
    this.totalHeight = totalHeight;
    this.facetDim = facetDim;
    faceList = new ArrayList<Facet>();
    drawFaceOutlines = true;
    coordSysDraw = false;
    if (radius > 100) {
      OUTLINELIMIT = 10 * radius;
    } else {
      OUTLINELIMIT = 45 * radius;
    }
  }


  public Dimension getFacetDim() {
    return facetDim;
  }

  public static void construct(Cylinder c) {
    new CylinderBuilder(c).build();
  }

  @Override
  public void set3DObjectForDraw(AbstractView aView) {
    //CoordSys cs = aView.getCoordSys();
    vectorFromView = aView.getObjectVectorFromView();
    //setVectorFromView(t2);

    boolean outlineFlag = VMath.mag(vectorFromView) < OUTLINELIMIT && drawFaceOutlines;
    for (int i = 0; i < faceList.size(); i++) {
      Facet f = faceList.get(i);
      f.set3DObjectForDraw(aView);
      f.setDrawFaceOutlines(outlineFlag);
    }
    super.set3DObjectForDraw(aView);
  }

  @Override
  public void draw(AbstractView view) {
    if (coordSysDraw) {
      sSys.draw(view);
    }

  }

  @Override
  public List<Facet> getFaceList() {
    return faceList;
  }


  public int getRadius() {
    return radius;
  }


  public int getNumFaces() {
    return numFaces;
  }

  public void setNumFaces(int numFaces) {
    this.numFaces = numFaces;
  }

  public int getTotalHeight() {
    return totalHeight;
  }

  public void setCappedEnds(boolean cappedEnds) {
    this.cappedEnds = cappedEnds;
  }

  public boolean isCappedEnds() {
    return cappedEnds;
  }

  @Override
  public IDetailer getDetailer() {
    if (detailer == null) {
      detailer = new DefaultFaceDetailer(11);
    }
    return detailer;
  }

  @Override
  public IDrawingVisitor getShadingVisitor() {
    // TODO Auto-generated method stub
    return drawingVisitor;
  }

  @Override
  public void setShadingVisitor(IDrawingVisitor v) {
    drawingVisitor = v;

  }

  @Override
  public ColorModel getColorModel() {
    // TODO Auto-generated method stub
    return colorModel;
  }

}
