package threeD;

import java.awt.Color;

public class DefaultColorModel3D implements ColorModel3D {
	
	private Color lightSourceColor;
	private Color ambientLightColor;
	private float ambientLightLevel;
	
	
	public DefaultColorModel3D( Color lightSourceColor){
		this.lightSourceColor=lightSourceColor;
	}

	public Color getColorForLightDotProd(double angle) {		
		return lightSourceColor;
	}


	public Color getAmbientLightColor() {
		return ambientLightColor;
	}


	public float getAmbientLightLevel() {
		return ambientLightLevel;
	}

	
	public Color getLightSourceColor() {
		return lightSourceColor;
	}

}
