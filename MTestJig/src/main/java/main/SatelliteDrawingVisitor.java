package main;


import VMath.VMath;
import orbits.Abstract3DModelObject;
import orbits.Facet;
import orbits.World3DContainer;
import surfaces3D.IShadowProducer;

import java.awt.*;

public class SatelliteDrawingVisitor implements IDrawingVisitor<Facet> {
  @Override
  public Color visit(Facet f) {

//    float shadeFactor = (float)VMath.dotprod(World3DContainer.getInstance().getLiteEmitterPositionUnitVector(),VMath.normalize(f.mv()));
    Abstract3DModelObject obj = f.getComposedObject();
    Color result = obj.getColor();
//    result = new Color(color.getRed(), (int)(color.getGreen()-(1-shadeFactor)*50),(int)(color.getBlue()-(1-shadeFactor)*50));

    for (IShadowProducer shadowProducer : World3DContainer.getInstance().getShadowProducers()) {
      if (shadowProducer.getShadowCalculator().isInShadow(f, shadowProducer) == 1) {
        result = new Color(result.getRed() / 3, result.getGreen() / 3, result.getBlue() / 3);
        break;
      }
    }

    return result;
  }

  @Override
  public void visit(Facet facet, AbstractView view) {

  }
}
