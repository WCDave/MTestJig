package threeD;

import gfxmain.GFXException;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import VMath.VMath;

public class Cone3D extends Abstract3DModelObject {

	private float radius;
	private float height;
	private final int  angleSlice = 60;
	
/**
 * Defines a 3D Cone
 * @param radius radius of the base of the cone
 * @param height height of the cone
 * @param color color of the cone
 * @throws GFXException if cs is null (detected in super class)
 */
	public Cone3D(CoordSys cs, float radius, float height, Color color) throws GFXException  {
		super(cs,color);		
		this.radius=radius;
		this.height=height;
		drawFaceOutlines=true;
		super.setColorModel3D(new DefaultColorModel3D(color));
		buildCone();		
	}
	
	public void buildCone() {	
		faceList=new ArrayList<Facet>();
		List<double[]> basePointList = new ArrayList<double[]>();
		double[] aPoint = null;
		double[] topPoint = sSys.transformVec(new double[]{0,0,height});
		for(int i=0;i<360;i+=angleSlice) {
			List<double[]> pointList = new ArrayList<double[]>();
			double ang1 = Math.toRadians(i);
			double ang2 = Math.toRadians(i+angleSlice);
			
			aPoint = sSys.transformVec( new double[]{radius*Math.cos(ang1),radius*Math.sin(ang1),0});
			pointList.add(aPoint);
			basePointList.add(aPoint);
			
			aPoint=sSys.transformVec( new double[]{radius*Math.cos(ang2),radius*Math.sin(ang2),0});
			pointList.add(aPoint);
			basePointList.add(aPoint);
			
			pointList.add(topPoint);			
			
			try {
				Facet aFace = new Facet(pointList,this);
				double[] normal = aFace.getNormal();
				if(VMath.dotprod(normal, topPoint) <= 0 )
				{
					aFace.setNormal(VMath.vecMultByScalar(normal, -1));
				}
				faceList.add(aFace);
				
			} catch (GFXException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}		
		
		try {
			Facet aFace = new Facet(basePointList,this);
			double[] normal = aFace.getNormal();
			if(VMath.dotprod(normal, topPoint) > 0 )
			{
				aFace.setNormal(VMath.vecMultByScalar(normal, -1));
			}
			faceList.add(aFace);
		} catch (GFXException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}				
	}
}
