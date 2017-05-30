package orbits.Object3D.impl;

import VMath.VMath;
import orbits.Facet;
import orbits.Object3D.IObjectElaborator;
import orbits.Object3D.IViewPositionListener;
import orbits.Sphere;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FacetElaborator implements IObjectElaborator<Facet> {

  private Sphere s;

  public FacetElaborator(Sphere s) {
    this.s = s;
  }


  @Override
  public void elaborate(final List<Facet> list) {
    if (list.size() > s.getBuildFactor()) return;

    Iterator<Facet> it = list.iterator();
    List<Facet> newFaces = new ArrayList<Facet>();
    while (it.hasNext()) {
      newFaces.addAll(elaborateFace(it.next()));
      it.remove();
    }

    list.addAll(newFaces);
    elaborate(list);
  }

  List<Facet> elaborateFace(Facet f) {

    List<Facet> result = new ArrayList<Facet>();
    double[] p1 = f.getVectorList().get(0);
    double[] p2 = f.getVectorList().get(1);
    double[] p3 = f.getVectorList().get(2);

    double[] newVert1 = VMath.vecMultByScalar(VMath.normalize(VMath.vecAdd(p1, VMath.vecMultByScalar(VMath.vecSubtract(p2, p1), 0.5))), s.getRadius());
    double[] newVert2 = VMath.vecMultByScalar(VMath.normalize(VMath.vecAdd(p2, VMath.vecMultByScalar(VMath.vecSubtract(p3, p2), 0.5))), s.getRadius());
    double[] newVert3 = VMath.vecMultByScalar(VMath.normalize(VMath.vecAdd(p3, VMath.vecMultByScalar(VMath.vecSubtract(p1, p3), 0.5))), s.getRadius());

    Facet newFace = new Facet(Arrays.asList(newVert1, newVert2, newVert3), s, s.getBuildFactor() > IViewPositionListener.RebuildFactor.firstLevelFactorThreshold(), null, null);
    //newFace.setColor(Facet.determineFaceColor(newFace));
    result.add(newFace);

    newFace = new Facet(Arrays.asList(p1, newVert1, newVert3), s, s.getBuildFactor() > IViewPositionListener.RebuildFactor.firstLevelFactorThreshold(), null, null);
    //newFace.setColor(Facet.determineFaceColor(newFace));
    result.add(newFace);

    newFace = new Facet(Arrays.asList(newVert1, p2, newVert2), s, s.getBuildFactor() > IViewPositionListener.RebuildFactor.firstLevelFactorThreshold(), null, null);
    //newFace.setColor(Facet.determineFaceColor(newFace));
    result.add(newFace);

    newFace = new Facet(Arrays.asList(newVert2, p3, newVert3), s, s.getBuildFactor() > IViewPositionListener.RebuildFactor.firstLevelFactorThreshold(), null, null);
    //newFace.setColor(Facet.determineFaceColor(newFace));
    result.add(newFace);

    return result;
  }
}
