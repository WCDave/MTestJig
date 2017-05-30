package orbits;

import VMath.VMath;
import main.AbstractView;
import main.Craft;
import surfaces3D.Cylinder;

import java.awt.*;

/**
 * Created by DaveMain on 5/25/2017.
 */
public class FacetStructureBehavior implements IGenericSatelliteStructureBehavior {

  private Craft craft;
  private Facet facet;
  private CoordSys craftSys;

  public FacetStructureBehavior(Craft craft, Facet facet) {
    this.craft = craft;
    this.facet = facet;
    craftSys = craft.getCoordSys();
  }

  @Override
  public void processStructure(AbstractView view) {
    //log.info("processing satellite piece");
    facet.setVectorFromView(VMath.vecSubtract(craftSys.getPositionVec(), view.getCoordSys().getPositionVec()));
    java.util.List<double[]> vecs =  facet.getVectorList();
    for (double[] vec : vecs) {
      double[] tVec = craft.getCoordSys().transformVec(vec);
      vec[0] = tVec[0];
      vec[1] = tVec[1];
      vec[2] = tVec[2];
    }
    facet.setOutlineColor(new Color((int) Math.min(255, 255 * VMath.mag(craft.getDrag()) / 1000), (int) Math.min(128, 128 * VMath.mag(craft.getDrag()) / 1000), 0));
    facet.set3DObjectForDraw(view);

  }
}
