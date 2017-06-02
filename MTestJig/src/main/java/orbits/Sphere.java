package orbits;

import VMath.VMath;
import main.AbstractView;
import main.ColorModel;
import main.Shadeable;
import orbits.Object3D.IRebuildable;
import org.apache.commons.math3.util.FastMath;
import org.apache.log4j.Logger;
import surfaces3D.DefaultShadowCalculator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WCDave
 *         <APPLET ARCHIVE="classes.jar" CODE="MyMainClass.class" WIDTH=200 HEIGHT=200>< /APPLET>
 */
public abstract class Sphere extends Abstract3DModelObject implements Shadeable, Composed3DObject<Facet>, IRebuildable {

  private float sRadius;
  private float oblateness = 0;
  public static int DPHI = 5;
  public static int DTHETA = 5;
  private Dimension faceDim = new Dimension(3, 3);
  protected List<Facet> faceList;
  transient protected BufferedImage textureImage;
  private Map<Facet, List<Facet>> faceMap = new HashMap<Facet, List<Facet>>();
  //private ICachingDetailer<Facet> detailer = new CachingFaceDetailerWithFutures(11);
  //private ICachingDetailer<Facet> detailer = new CachingFaceDetailerWithFuturesArrayOptimized(11);
  //private ICachingDetailer<Facet> detailer = new CachingFaceDetailerWithFuturesTreeMapOptimized(11);
  private ICachingDetailer<Facet> detailer = new CachingFaceDetailerWithFuturesArray(11);
  transient protected ColorModel colorModel;


  transient Logger log = Logger.getLogger(Sphere.class);

  private double OUTLINELIMIT = 30000 * NavComputer.METERS_PER_MILE; //30,000 miles
  protected int buildFactor = 20;


  public Sphere(BufferedImage image, Dimension faceSize) {
    super();
    setShadowCalculator(new DefaultShadowCalculator(this));
    textureImage = image;
    faceList = new ArrayList<Facet>(4000);
    DPHI = faceSize.height;
    DTHETA = faceSize.width;
    faceDim = faceSize;
    World3DContainer.getInstance().registerWithListeners(this);
  }

  public static void buildSphere(Sphere sphere) {
    SphereBuilder.buildSphere(sphere);
    //System.out.println("building "+ sphere.getName()+", "+sphere.getFaceList().size());
//        double ul[] = {0,0,0};
//        double ll[] = {0,0,0};
//        double ur[] = {0,0,0};
//        double lr[] = {0,0,0};
//        sphere.faceList.clear();
//		Facet aFace;
//		int DPHI = sphere.getFaceDim().height;
//		int DTHETA = sphere.getFaceDim().width;
//		int thetaslice = 360/DPHI;
//		int phislice = 180/DPHI;
//
//		for (int i=0;i<phislice;i++) {
//			for (int j=0;j<thetaslice;j++) {
//
//				ul=getVectorForLatLon((float)i*DPHI, (float)j*DTHETA, sphere.sSys, obRad(i*DPHI, sphere.sRadius, sphere.oblateness));
//
//				ll=getVectorForLatLon((float)i*DPHI+DPHI,(float)j*DTHETA,sphere.sSys, obRad(i*DPHI+DPHI, sphere.sRadius, sphere.oblateness));
//
//				lr=getVectorForLatLon((float)i*DPHI+DPHI,(float)(j+1)*DTHETA,sphere.sSys, obRad(i*DPHI+DPHI, sphere.sRadius, sphere.oblateness));
//
//				ur=getVectorForLatLon((float)i*DPHI,(float)(j+1)*DTHETA,sphere.sSys, obRad(i*DPHI, sphere.sRadius, sphere.oblateness));
//
//				List<double[]> aList = new ArrayList<double[]>(4);
//				aList.add(ul);aList.add(ll);aList.add(lr);aList.add(ur);
//
//				aFace = new Facet(aList, sphere, true, Integer.toString(DPHI*i)+"|"+Integer.toString(DTHETA*j), null);
//				aFace.setColor(sphere.color);
////				if(sphere.textureImage != null)
////				{
////					aFace.setTextureImage(sphere.textureImage.getSubimage(j*DPHI, i*DTHETA,  (sphere.textureImage.getWidth()/360)*sphere.getFaceDim().width, (sphere.textureImage.getHeight()/180)*sphere.getFaceDim().height));
////				}
//				//aFace.setName(Integer.toString(dphi*i)+"|"+Integer.toString(dTheta*j));
//				//System.out.println(aFace.name());
//				//aFace.setComposedObject(this);
//				//if(aFace.name().equals("64|36"))
//				//		aFace.setColor(Color.yellow);
//				sphere.faceList.add(aFace);
//			}
//		}
  }

  @Override
  public void set3DObjectForDraw(AbstractView aView) {

    vectorFromView = aView.getObjectVectorFromView();
//        double zDotProdCheck = VMath.dotprod(aView.getCoordSys().getVectorForViewDirection(), VMath.normalize(vectorFromView) ) + Math.asin(sRadius/VMath.mag(vectorFromView));
//        if(zDotProdCheck < .2) return;
    aView.setDistanceDrawingFactorForView(sRadius / VMath.mag(vectorFromView) * -.998899);

    //boolean outlineFlag = VMath.mag(vectorFromView) < OUTLINELIMIT && drawFaceOutlines;
    synchronized (faceList) {
      for (Facet f : faceList) {
//  for (int i = 0;i<faceList.size();i++) {
//   Facet f=faceList.get(i);
        f.set3DObjectForDraw(aView);
        //f.setDrawFaceOutlines(outlineFlag);
      }
    }
    //Collections.sort(faceList);
  }

  public void setRadius(float aRadius) {
    sRadius = (float) (aRadius);
    if (sRadius > 100) {
      OUTLINELIMIT = 10 * sRadius;
    } else {
      OUTLINELIMIT = 45 * sRadius;
    }
    return;
  }

  public float getRadius() {
    return sRadius;
  }

  public void setOblateness(float oblateness) {
    this.oblateness = oblateness;
    buildSphere(this);
  }

  public float getOblateness() {
    return oblateness;
  }

  public float obRad(float lat) {
    return ((1 - oblateness) + 2 * oblateness * (float) FastMath.sin(FastMath.abs(lat))) * sRadius;
  }

  public List<Facet> getFaceList() {
    return faceList;
  }


  public Dimension getFaceDim() {
    return faceDim;
  }


  public Map<Facet, List<Facet>> getFaceMap() {
    return this.faceMap;
  }

  @Override
  public void rebuild(int factor) {
    if (factor != buildFactor) {
      buildFactor = factor;
      synchronized (faceList) {
        detailer.flushCache();
        faceList.clear();
        buildSphere(this);
      }
    }
  }

  @Override
  public double getDetailingDistance() {
    return this.sRadius;
  }

  @Override
  public int getBuildFactor() {
    return buildFactor;
  }

  @Override
  public ICachingDetailer<Facet> getDetailer() {
    return detailer;
  }
}
