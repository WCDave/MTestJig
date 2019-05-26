package main;

import VMath.VMath;
import orbits.Facet;
import orbits.Planet;
import orbits.World3DContainer;
import org.apache.commons.math3.util.FastMath;

import java.awt.*;

public class DrawingVisitor implements IDrawingVisitor<Facet> {


  public DrawingVisitor() {

  }

  @Override
  public Color visit(Facet f) {
    //AbstractView view = f.getCurrentView();
    //Abstract3DModelObject liteEmitter = World3DContainer.getInstance().getLiteEmitter();
    float shadeFactor = getSunAngleFactor(f);
    Planet p = (Planet) f.getComposedObject();
    //f.setColor(p.getColorModel().getShadeForLightSourceFactor(shadeFactor));
    Color resultColor = p.getColorModel().getShadeForLightSourceFactor(f.getColor(), -shadeFactor);

//    for(IShadowProducer shadowProducer : World3DContainer.getInstance().getShadowProducers()) {
//      if(shadowProducer.getShadowCalculator().isInShadow(f, shadowProducer) == 1) {
//        resultColor = new Color(resultColor.getRed()/2, resultColor.getGreen()/2, resultColor.getBlue()/2);
//        break;
//
//      }
//    }

    return resultColor;
  }

  @Override
  public void visit(Facet facet, AbstractView view) {
    float sunAngleFactor = getSunAngleFactor(facet);
    sunAngleFactor = sunAngleFactor < 0 ? 1 : sunAngleFactor;
    double dotProdWithView = VMath.dotprod(view.getCoordSys().getViewAxis(), VMath.normalize(facet.mv()));
    double distanceOfFacetFromView = VMath.mag(facet.getVectorFromView());
    int alpha = FastMath.min((int)(255 * (1-sunAngleFactor)*(1 - FastMath.exp(-distanceOfFacetFromView/5e6))), 255);
    //if(alpha < 255) {
      //System.out.println("SA=" + sunAngleFactor +", Alpha="+alpha+", distFromViewFactorExp="+Math.exp(-distanceOfFacetFromView/1e7)+", distFromView="+distanceOfFacetFromView);
    //}
    Color c = new Color(facet.getComposedObject().getColor().getRed(), facet.getComposedObject().getColor().getGreen(), facet.getComposedObject().getColor().getBlue(), alpha);
    facet.setColor(c);
    facet.setDrawFaceOutlines(false);
    facet.setPaintBothSides(true);

  }

  private float getSunAngleFactor(Facet f) {
    double[] mv = VMath.normalize(f.mv());
    //double[] t1 = VMath.normalize(liteEmitter.getCoordSys().getPositionVec());
    return (float) VMath.dotprod(World3DContainer.getInstance().getLiteEmitterPositionUnitVector(f.getComposedObject()), mv);
  }
}
