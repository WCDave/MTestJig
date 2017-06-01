package threeD;

import enums.GFXExceptionTypes;
import gfxmain.GFXException;
import gfxmain.GFXFramework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import VMath.VMath;

public class Facet implements Drawable3D {
	private double[] mv;
	private boolean isIlluminated=true;
	private boolean isBlocked=false;
	private double[] vectorFromView;
	private Abstract3DModelObject composedObject;
	private ArrayList<double[]> surfaceFeatureData;
	private List<double[]> uVectorList;
	private final static Color shadowColor = new Color(50,50,50);
	private double[] normal;
	
/**
 * Constructs a 3D Face for the composed object given a list of points defining the face
 * @param pointList list of points defining the face
 * @param composedObject object to which this face belongs
 * @throws GFXException if pointList doesn't consist of at least 3 points
 */
	public Facet(List<double[]> pointList, Abstract3DModelObject composedObject) throws GFXException {
		if(pointList.size()<3) {
			throw new GFXException(null,GFXExceptionTypes.INVALID_FACET_DEFINITION);
		}
		surfaceFeatureData = new ArrayList<double[]>();
		this.composedObject=composedObject;
		uVectorList=pointList;
		mv = new double[]{0,0,0};
		for(double[] uv:uVectorList){
			mv = VMath.vecAdd(mv, VMath.normalize(uv));
		}
		mv = VMath.vecMultByScalar(mv, 1/(float)uVectorList.size());
		
		double[] vec1 = uVectorList.get(0);
		double[] vec2 = uVectorList.get(1);
		double[] vec3 = uVectorList.get(2);
		double[] vecA = VMath.vecSubtract(vec2, vec1);
		double[] vecB = VMath.vecSubtract(vec3, vec1);
		normal = VMath.normalize(VMath.crossprd(vecA, vecB));
	}
	
	public void setIsBlockedFromView(boolean isBlockedFromView) {
		this.isBlocked = isBlockedFromView;
	}
	
	public boolean isBlockedFromView() {
		return isBlocked;
	}
	
	/**
	 * Sets vector pointing from coordinate system cs to geometric center of this face
	 * @param cs viewing coordinate system
	 */
	public void setVectorFromView(CoordSys cs) {
		this.vectorFromView=VMath.vecSubtract(VMath.vecAdd(composedObject.getCoordSys().getPositionVec(),mv), cs.getPositionVec());
	}
	
	/**
	 * Returns current vector from viewing coordinate system
	 * @return 3D vector
	 */
	public double[] getVectorFromView() {
		return vectorFromView;
	}
	
	private GeneralPath createGeneralPath(Dimension d, CoordSys viewSys){
		ScreenPoint gpvec;
		GeneralPath aGp=null;
        double[] pv=composedObject.getCoordSys().getPositionVec();
        Abstract3DModelObject liteEmitter=World3DContainer.getInstance().getLiteEmitter();
        
		if (VMath.dotprod(VMath.normalize(vectorFromView),normal) <= 0)	{			
			
			aGp = new GeneralPath(GeneralPath.WIND_EVEN_ODD,4);
			
			for(double[] point:uVectorList) {				
     			gpvec=viewSys.getScrnCoords(VMath.vecAdd(point,pv),d);
     			//gpvec=viewSys.getScrnCoords(point,d);
				if(!gpvec.isInView())
					return null;
				
				if(aGp.getCurrentPoint()==null) 
				{
				   aGp.moveTo(gpvec.x,gpvec.y);   
				}
				else 
				{
				   aGp.lineTo(gpvec.x,gpvec.y);
				}				
			}
			aGp.closePath();	
			
			if(liteEmitter!=null){
				double[] t1 = VMath.normalize(liteEmitter.getCoordSys().getPositionVec());
			    isIlluminated=VMath.dotprod(t1,normal)>0 || liteEmitter.equals(composedObject);
			}		
		}
		return aGp;
	}
	
	public void draw(GFXFramework gfx, CoordSys cs){
		GeneralPath aGp=createGeneralPath(gfx.getSize(), cs);
		Graphics2D g2 = gfx.getG2();
		
		if(aGp!=null){
			
		   if(isIlluminated) {
		       g2.setColor(composedObject.color);
		   }
		   else {
			   g2.setColor(shadowColor);
		   }
		   
		   g2.fill(aGp);
		   if(composedObject.drawFaceOutlines && !isBlocked) {
			   g2.setColor(Color.black);
		   }
			   
		   g2.draw(aGp);
		   if(surfaceFeatureData.size()>0) {
			   drawSurfaceFeatures( gfx, cs);
		   }
		}
	}
	
	private void drawSurfaceFeatures(GFXFramework gfx, CoordSys cs) {		
		Graphics2D g2 = gfx.getG2();
		Dimension d = gfx.getSize();
		g2.setColor(isIlluminated?Color.YELLOW:Color.red);
		
		ScreenPoint gpVec=null;
		for(double[] point:surfaceFeatureData) {			
			gpVec=null;
			if(point!=null)
			    gpVec=cs.getScrnCoords(point,d);
			if(gpVec!=null && gpVec.isInView()){
				g2.drawLine(gpVec.x,gpVec.y,gpVec.x,gpVec.y);
			}			
		}		
	}
	
	/**
	 * Returns object that this face helps compose
	 * @return composed object
	 */
	public Abstract3DModelObject getComposedObject() {
		return composedObject;
	}
	
	public void setSurfaceFeatureData(ArrayList<double[]> coastlineData) {
		this.surfaceFeatureData = coastlineData;
	}
	
	/**
	 * Returns vector that represents the geometric center of this face,
	 * @return 3D vector
	 */
	public double[] getGeomCenterVector() {
		return mv;
	}
	
	public void setGeomCenterVector(double[] mv)
	{
		this.mv = mv;
	}
	
	public void setNormal(double[] normal)
	{
		this.normal = normal;
	}


	/**
	 * @return the normal
	 */
	public double[] getNormal() {
		return normal;
	}
}
