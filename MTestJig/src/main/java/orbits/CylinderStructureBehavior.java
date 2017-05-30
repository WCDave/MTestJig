package orbits;

import VMath.VMath;
import main.AbstractView;
import main.Craft;
import org.apache.log4j.Logger;
import surfaces3D.Cylinder;

/**
 * Created by DaveMain on 5/25/2017.
 */
public class CylinderStructureBehavior implements IGenericSatelliteStructureBehavior {

  private Craft craft;
  private Cylinder cylinder;
  private Logger log = Logger.getLogger(CylinderStructureBehavior.class);


  public CylinderStructureBehavior(Craft craft, Cylinder cylinder) {
    this.craft = craft;
    this.cylinder = cylinder;
  }

  @Override
  public void processStructure(AbstractView view) {
    float drag = (float) VMath.mag(craft.getDrag()) / (NavComputer.EarthG * craft.getMass());
    if (drag < 2 || cylinder.getFaceList().isEmpty()) {
      synchronized (cylinder.getCoordSys()) {
        Cylinder.construct(cylinder);
        cylinder.set3DObjectForDraw(view);
      }
    } else {
        for(Thread t : craft.getBehaviorThreads()) {
          t.interrupt();
        }
      int i = 0;
      for (Facet f : cylinder.getFaceList()) {
        Facet.individuateFacet(f);
        craft.nullRates();
        //World3DContainer.getInstance().addCraft(new GenericSatellite(f, (CoordSys) this.getCoordSys().clone(),""+i++));
        World3DContainer.getInstance().addForEventualRemoval(new GenericSatellite(f, (CoordSys) craft.getCoordSys().clone(), "" + i++));
      }
      World3DContainer.getInstance().removeCraft(craft);
      World3DContainer.getInstance().createRemovalJob(1440000);

      log.info("satellite blew up " + World3DContainer.getInstance().getCraftList().size() + ", drag=" + drag);
    }
  }
}
