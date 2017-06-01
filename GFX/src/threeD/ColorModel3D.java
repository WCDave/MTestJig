package threeD;

import java.awt.Color;

public interface ColorModel3D {
	
	Color getColorForLightDotProd(double angle);
	float getAmbientLightLevel();
	Color getAmbientLightColor();
	Color getLightSourceColor();

}
