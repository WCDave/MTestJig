package orbits.Object3D.impl;

import VMath.VMath;
import main.AbstractView;
import orbits.Abstract3DModelObject;
import orbits.Object3D.IViewPositionListener;
import orbits.Object3D.IRebuildable;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class DefaultPositionListener extends Listenable<Abstract3DModelObject> implements IViewPositionListener<Abstract3DModelObject, AbstractView<Abstract3DModelObject>> {

  @Override
  public void checkDistance(AbstractView view) {
    for (Abstract3DModelObject a3o : listeners) {
      if (a3o instanceof IRebuildable) {
        IRebuildable rb = (IRebuildable) a3o;
        double distanceFromView = VMath.mag(a3o.getVectorFromView());
        int initialFactor = (int) (distanceFromView / rb.getDetailingDistance());
        rb.rebuild(RebuildFactor.getFactorFor(initialFactor).getFactor());
      }
    }

  }
}
