package orbits;

import VMath.VMath;
import main.AbstractView;
import main.IDrawingVisitor;
import main.Shadeable;
import main.Visitable;
import mapUtils.MapHelper;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Facet extends Abstract3DModelObject implements Visitable {

  transient private GeneralPath itsPath;
  private boolean isIlluminated = false;
  private boolean isBlocked;
  private Abstract3DModelObject composedObject;
  //private List<double[]> surfaceFeatureData=new ArrayList<double[]>();
  //private final static Color shadowColor = new Color(50,50,50);
  private Color outlineColor = Color.black;
  private List<double[]> vectorList;
  private double[] mv;
  private boolean topLevel = true;
  private int lastDetailLevel;
  //private static final Color lineColor = new Color(40,119,183);
  private static final double[] starterArray = new double[]{0, 0, 0};

  //private double[] normalVec;

  private boolean hasDetailFacets;
  private List<Facet> detailList;
  transient private BufferedImage litTextureImage;
  public static double detailDot = -.9;


  private boolean paintBothSides = false;

  public Facet(List<double[]> vertexList, Abstract3DModelObject object, boolean hasDetailFacets, String name, Facet parentFace) {
    //if (vertexList.size() > 2) {
    vectorList = new ArrayList<double[]>(vertexList);

    double[] mvv = starterArray;
    for (double[] u : vectorList) {
      mvv = VMath.vecAdd(mvv, u);
    }

    mv = VMath.vecMultByScalar(mvv, 1.0 / (double) vectorList.size());

    composedObject = object;
    this.shadowColor = composedObject.shadowColor;
    this.name = name;

    this.hasDetailFacets = hasDetailFacets;
    detailList = Collections.EMPTY_LIST;

    if (parentFace != null) {
      this.drawFaceOutlines = parentFace.drawFaceOutlines;
      topLevel = false;
    }
    //color = determineFaceColor(this);
    //}
  }

  public Facet(List<double[]> vertexList, Abstract3DModelObject object) {
    vectorList = new ArrayList<double[]>(vertexList);
    composedObject = object;
  }

  public void setPaintBothSides(boolean paintBothSides) {
    this.paintBothSides = paintBothSides;
  }


  //	public static void printFacet(Facet f) {
//		System.out.println("Facet Print "+f.name);
//		System.out.println("x="+f.ul.x+" y="+f.ul.y+" z="+f.ul.z);
//		System.out.println("x="+f.ll.x+" y="+f.ll.y+" z="+f.ll.z);
//		System.out.println("x="+f.lr.x+" y="+f.lr.y+" z="+f.lr.z);
//		System.out.println("x="+f.ur.x+" y="+f.ur.y+" z="+f.ur.z);
//		return;
//	}
  public void setIsIlluminated(boolean aFlag) {
    isIlluminated = aFlag;
    return;
  }

  public boolean isIlluminated() {
    return isIlluminated;
  }

  public void setGeneralPath(GeneralPath aPath) {
    itsPath = aPath;
    return;
  }

  public GeneralPath itsPath() {
    return itsPath;
  }

  public double[] mv() {
    return mv;
  }


  public void setIsBlockedFromView(boolean isBlockedFromView) {
    this.isBlocked = isBlockedFromView;
  }

  public boolean isBlockedFromView() {
    return isBlocked;
  }

  private static Shape createShape(AbstractView aView, Facet face) {
    Dimension d = aView.getViewCanvas().getScreenCenter();
    CoordSys viewSys = aView.getCoordSys();
    ScreenPoint gpvec;
    GeneralPath aGp = null;
    double[] pv = face.composedObject.getCoordSys().getPositionVec();

    if (VMath.dotprod(face.vectorFromView, face.mv()) <= 0.0 || face.paintBothSides) {

      aGp = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 4);

      for (double[] u : face.vectorList) {

        gpvec = viewSys.getScrnCoords(VMath.vecAdd(u, pv), d.width, d.height);
        if (!gpvec.isInView())
          return null;

        if (aGp.getCurrentPoint() == null)
          aGp.moveTo(gpvec.x, gpvec.y);
        else
          aGp.lineTo(gpvec.x, gpvec.y);

      }
      aGp.closePath();
    }
    return aGp;
  }

  private static Shape createShapePoly(AbstractView aView, Facet face) {

    Shape polygon = null;
    if (VMath.dotprod(face.vectorFromView, face.mv()) <= 0.0 || face.paintBothSides) {
      double[] pv = face.getComposedObject().getCoordSys().getPositionVec();
      Dimension d = aView.getViewCanvas().getScreenCenter();

      CoordSys viewSys = aView.getCoordSys();

      int[] x = new int[36];
      int[] y = new int[36];

      int i = 0;
      for (double[] u : face.vectorList) {

        ScreenPoint gpvec = viewSys.getScrnCoords(VMath.vecAdd(u, pv), d.width, d.height);
        if (!gpvec.isInView())
          return null;

        x[i] = gpvec.x;
        y[i++] = gpvec.y;
      }
      polygon = new Polygon(x, y, i);
    }
    return polygon;
  }

  @Override
  public void draw(final AbstractView aView) {

    if (getDetailList().isEmpty()) {
      Shape aGp = createShapePoly(aView, this);
      if (aGp != null) {
        Color shadedColor = accept(((Shadeable) composedObject).getShadingVisitor());
        Graphics2D g2 = aView.getViewCanvas().getG2();
        //synchronized (g2 ){
        g2.setPaint(shadedColor);
        g2.fill(aGp);
        if (drawFaceOutlines) {
          g2.setColor(outlineColor);
        }
        //if(!paintBothSides) {
        g2.draw(aGp);
        // }
        //}
      }
    } else {
//      List<List<Facet>> chunk = Lists.partition(list, 500);
//      Thread t = null;
//      List<Thread> threadList = new ArrayList(chunk.size());
//      for(final List<Facet> subList : chunk) {
//           t = new Thread() {
//            public void run() {
//              for (final Facet f : subList) {
//                f.draw(aView);
//              }
//            }
//
//          };
//        t.start();
//        threadList.add(t);
//      }
//      Collections.reverse(threadList);
//      for(Thread tx : threadList) {
//        try {
//          tx.join();
//        } catch (InterruptedException e) {
//          e.printStackTrace();
//        }
//      }
      for (final Facet f : getDetailList()) {
        f.draw(aView);
      }
    }
  }


	/* private void drawSurfaceFeatures(Graphics2D g2, AbstractView aView, Facet face) {
		CoordSys viewSys = aView.getCoordSys();
		Dimension d = aView.getViewCanvas().getSize();
		g2.setColor(face.isIlluminated?Color.YELLOW:Color.red);

		ScreenPoint gpVec2;
		for(double[] point:face.surfaceFeatureData)
		{
			gpVec2=null;
			if(point!=null)
			{
			    gpVec2=viewSys.getScrnCoords(point,d.width,d.height);
			}

			if(gpVec2!=null && gpVec2.isInView())
			{
				g2.drawLine(gpVec2.x,gpVec2.y,gpVec2.x,gpVec2.y);
			}
			else
			{
				//System.out.println("can't draw");
			}
		}
	}


	public List<double[]> getSurfaceFeatureData() {
		return surfaceFeatureData;
	}    */


  @Override
  public void set3DObjectForDraw(AbstractView view) {

    double dotFromView = VMath.dotprod(VMath.normalize(view.getObjectVectorFromView()), VMath.normalize(mv));
    if (dotFromView < view.getDistanceDrawingFactorForView()) { //0
      //double faceDotFromView = VMath.dotprod(view.getCoordSys().getViewAxis(), VMath.normalize(VMath.vecAdd(view.getObjectVectorFromView(), mv))) ;
      //if(faceDotFromView < 0.5) return;
      ///color = determineFaceColor(this);
      //vectorFromView = VMath.vecAdd(view.getObjectVectorFromView(), mv);
      if (topLevel) {
        if(((Composed3DObject<Facet>)composedObject).getDetailer() != null) {
          ((Composed3DObject<Facet>)composedObject).getDetailer().createDetails(this, view);
        }
        //faceDetailer.createDetails(this, view);
        //if(getDetailList().isEmpty())
        super.set3DObjectForDraw(view);
      }
    }
  }

  /**
   * @return the vectorList
   */
  public List<double[]> getVectorList() {
    return vectorList;
  }

  @Override
  public Color accept(IDrawingVisitor v) {
    Color result = getComposedObject().getColor();
    if (v != null) {
      result = v.visit(this);
    }
    return result;
  }

  public Abstract3DModelObject getComposedObject() {
    return composedObject;
  }

//    public void setTextureImage(BufferedImage textureImage) {
//        litTextureImage = textureImage;
//        if(litTextureImage != null)
//        {
//            color = null;
//        }
////			unLitTextureImage = new BufferedImage(litTextureImage.getWidth(null), litTextureImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
////
////			// Copy non-RGB image to the RGB buffered image
////			Graphics2D g = unLitTextureImage.createGraphics();
////			g.drawImage(litTextureImage, 0, 0, null);
////			g.dispose();
////
////			RescaleOp op = new RescaleOp(.4f,0,null);
////			op.filter(this.unLitTextureImage, unLitTextureImage);
////			try {
////				litFaceTexture = new ImageTexture(litTextureImage, litTextureImage.getWidth(), litTextureImage.getHeight());
////				unLitFaceTexture = new ImageTexture(unLitTextureImage, unLitTextureImage.getWidth(), unLitTextureImage.getHeight());
////			} catch (InterruptedException e) {
////
////			}
////		}
//
//
//    }


  public static Color determineFaceColor(Facet f) {
    Color result = f.composedObject.color;
    if (f.composedObject instanceof Shadeable) {
      Shadeable object = (Shadeable) f.composedObject;
      BufferedImage im = object.getColorModel().getModelImage();
      if (im != null) {
        result = MapHelper.getColorFromMapForFace(im, f);
      }
    }
    return result;
  }

  @Override
  public String toString() {
    return this.color.toString();
  }

  public boolean hasDetailFacets() {
    return hasDetailFacets;
  }

  public List<Facet> getDetailList() {
    return detailList;
  }

  public void setDetailList(List<Facet> list) {
    this.detailList = list;
  }

  public void setMv(double[] vec) {
    mv = vec;
  }

  public int getLastDetailLevel() {
    return lastDetailLevel;
  }

  public void setLastDetailLevel(int lastDetailLevel) {
    this.lastDetailLevel = lastDetailLevel;
  }

  public void setComposedObject(Abstract3DModelObject composedObject) {
    this.composedObject = composedObject;
  }


  public void setOutlineColor(Color outlineColor) {
    this.outlineColor = outlineColor;
  }


  public static void individuateFacet(Facet f) {
    List<double[]> newVectorList = new ArrayList<double[]>(f.getVectorList().size());
    for (double[] d : f.getVectorList()) {
      newVectorList.add(VMath.vecSubtract(d, f.mv()));
    }
    f.mv = VMath.normalize(f.mv);
    f.vectorList = newVectorList;
    //f.setComposedObject(f);

  }
}
