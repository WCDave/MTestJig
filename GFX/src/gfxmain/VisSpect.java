package gfxmain;

import java.awt.Color;

/*
 * Created on Nov 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

/**
 * @author WCDave
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class VisSpect {
	
	public static Color getSpectralColor(float i){
		return getSpectralColor((double)i);
	}
	public static Color getSpectralColor(double i) {
		int   ri, gi, bi;	
		double r, gr, b, sss;		
		r=0;gr=0;b=0;sss=0;			
		if (( i >= 380.0 ) && (i <= 440.0 )) { 
					r = -1.0F *(i - 440.0F)/ 60.0F;
					gr = 0.0F;
					b = 1.0F; }
				else if (( i > 440.0 ) && (i <= 490.0 )) {
					r = 0.0F;
					gr = (i -  440.0F)/ 50.0F;
					b = 1.0F; }
				else if (( i > 490.0 ) && (i <= 510.0 )) {
					r = 0.0F;
					gr = 1.0F;
					b = -1.0F * (i -  510.0F)/ 20.0F; }
				else if (( i > 510.0 ) && (i <= 580.0 )) {
					r = (i -  510.0F)/ 70.0F;
					gr = 1.0F;
					b = 0.0F; }
				else if (( i > 580.0 ) && (i <=645.0 )) {
					r = 1.0F;
					gr = -1.0F * (i -645.0F)/65.0F;
					b = 0.0F; }
				else if (( i > 645.0) && (i <= 780.0 )) {
					r = 1.0F;
					gr = 0.0F;
					b = 0.0F; }
				if (i > 700.0 ) sss = 0.3F +0.7F*(780.0F - i )/80.0F;
				else if (i < 420.0) sss = 0.3F+0.7F*(i - 380.0F)/40.0F;
				else sss = 1.0F;
					ri = (int)  (sss*r*255.0F);
					gi = (int)  (sss*gr*255.0F);
					bi = (int)  (sss*b*255.0F);					
		return new Color(ri,gi,bi);		
	}	
	
}
