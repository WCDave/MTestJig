package orbits;

import VMath.VMath;
import main.AbstractView;
import main.ColorModel;
import main.Craft;
import main.IDrawingVisitor;
import main.Shadeable;
import orbits.atmospherics.DefaultDragModel;
import orbits.atmospherics.IDragModel;
import org.apache.log4j.Logger;
import surfaces3D.Cylinder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class GenericSatellite extends Craft implements Shadeable, Composed3DObject<Facet> {

  private Abstract3DModelObject structure;
  private IDragModel dragModel;
  private IGenericSatelliteStructureBehavior structureBehavior;

  private Logger log = Logger.getLogger(GenericSatellite.class);


  public GenericSatellite() {
    super();

    //structure = new Sphere(null, new Dimension(15,15));
    //((Sphere)structure).setRadius(70);
    //((Sphere)structure).setOblateness(0.01f);
    structure = new Cylinder(18, 40, new Dimension(15, 10));
    structure.setColor(new Color(Color.cyan.getRed(), Color.cyan.getGreen(), Color.cyan.getBlue(), 255));
    structure.sSys = getCoordSys();
    structure.coordSysDraw = false;
    structure.setDrawFaceOutlines(true);
    structure.setDetailingFactor(.00003);
    structure.shadowColor = Color.cyan;
    Cylinder.construct((Cylinder) structure);
    dragModel = new DefaultDragModel(10000);

    structureBehavior = new CylinderStructureBehavior(this, (Cylinder) structure);
  }

  public GenericSatellite(Facet f, CoordSys navObject, String name) {
//        double[] vel = navObject.getVelocityAsVec();
//        vel[0]+=Math.random()*5;
//        vel[1]+=Math.random()*5;
//        vel[2]+=Math.random()*5;
    structure = f;
    f.sSys = navObject;

    f.setColor(Color.cyan);
    f.setComposedObject(this);
    this.sSys = navObject;
    this.setColor(f.getColor());
    //f.setHasDetailFacets(false);
    f.setPaintBothSides(true);
    this.setMass((float) (5000 * Math.random()));
    f.setDrawFaceOutlines(true);
    // this.adjustPitchRate((float)Math.random()/10);
    this.nullRates();
    dragModel = new DefaultDragModel(100 * Math.random());
    this.setName(name);

    structureBehavior = new FacetStructureBehavior(this, f);
  }

  @Override
  public Vector<Point3D> getDataList() {                                  
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public void set3DObjectForDraw(AbstractView aView) {
    //Sphere.buildSphere((Sphere)structure);
    structureBehavior.processStructure(aView);
  }


  @Override
  public void setCoordSys(CoordSys navObject) {
    super.setCoordSys(navObject);
    structure.sSys = navObject;
  }

  @Override
  public IDrawingVisitor getShadingVisitor() {
    return null;
  }

  @Override
  public void setShadingVisitor(IDrawingVisitor v) {

  }

  @Override
  public ColorModel getColorModel() {
    return ColorModel.DEFAULT_COLOR_MODEL;
  }

  @Override
  public double getDragFactor() {
    return dragModel.getDragFactor();
  }

  @Override
  public List<Facet> getFaceList() {
    return null;
  }


  @Override
  public IDetailer<Facet> getDetailer() {
    return null;
  }

//    @Override
//    public void finalize() {
//        System.out.println("finalize"+this.getName());
//    }
}
