package threeD;

import enums.GFXExceptionTypes;
import gfxmain.GFXException;
import gfxmain.GFXFramework;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import VMath.VMath;

public abstract class Abstract3DModelObject implements Drawable3D {
	protected double[] vectorFromView;
	protected Color color;
	protected CoordSys sSys;
	protected boolean drawFaceOutlines;
	protected List<Facet> faceList;
	protected String name;
	private ColorModel3D colorModel;
	
	/**
	 * Defines a 3D object in the Graphics Framework
	 * @param cs Coordinate system giving position and alignment of this cone (Cannot be null)
	 * @throws GFXException if cs is null
	 */
	public Abstract3DModelObject(CoordSys cs, Color color) throws GFXException {
		if(cs==null) {
			throw new GFXException(null, GFXExceptionTypes.IMPROPER_3D_OBJECT_DEF);
		}
		this.color=color;
		sSys = cs;
		faceList=new ArrayList<Facet>();
		vectorFromView = new double[]{0,0,0,0};
	}
	
	public void setVectorFromView(double[] vectorFromView) {
		this.vectorFromView = vectorFromView;
	}
	
	public double[] getVectorFromView() {
		return vectorFromView;
	}

	public Color getColor(){
		return color;
	}
	
	public void setColor(Color color) {
		this.color=color;
	}


	public void setDrawFaceOutlines(boolean drawFaceOutlines) {
		this.drawFaceOutlines = drawFaceOutlines;
	}

	public boolean isDrawFaceOutlines() {
		return drawFaceOutlines;
	}

	public CoordSys getCoordSys() {
		return sSys;
	}

	public String getName() {
		return name;
	}	
	/**
	 * @param gfx the Graphics Framework Object
	 * @param cs  the Coordsys indicating draw perspective
	 */
	
	/*
	 * double[] t2 = VMath.vecSubtract(cs.getPositionVec(),sSys.getPositionVec());
		super.setVectorFromView(t2);
	 */
	public void draw(GFXFramework gfx, final CoordSys cs) {
		Comparator<Facet> c = new Comparator<Facet>(){
			public int compare(Facet f1, Facet f2) {
				f1.setVectorFromView(cs);
				f2.setVectorFromView(cs);
				if(VMath.mag(f1.getVectorFromView()) > VMath.mag(f2.getVectorFromView()))
					return 1;
				else
					return -1;
			}
		};
		Collections.sort(faceList,c);
		for(Facet f:faceList) {
			f.draw(gfx, cs);
		}
	}
	
	public ColorModel3D getColorModel3D() {		
		return colorModel;
	}
	
	public void setColorModel3D(ColorModel3D model) {
		colorModel = model;
	}
}
