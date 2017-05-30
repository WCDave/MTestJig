package orbits.Object3D.impl;

import mapUtils.MapHelper;
import orbits.Facet;
import orbits.Object3D.IObjectBuilder;
import orbits.Object3D.IObjectElaborator;
import orbits.Sphere;


public abstract class SphereBuilderBase implements IObjectBuilder<Sphere> {

  protected static double[] NORTH_POLE;
  protected static double[] SOUTH_POLE;

  protected Sphere s;
  protected final IObjectElaborator<Facet> elaborator;

  public SphereBuilderBase(Sphere s) {
    NORTH_POLE = MapHelper.convertGeographicalDegreesToCartesianCoords(s.getRadius(), 90, 0);
    SOUTH_POLE = MapHelper.convertGeographicalDegreesToCartesianCoords(s.getRadius(), -90, 0);
    this.s = s;
    elaborator = new FacetElaborator(s);
  }

  @Override
  public void build() {
    elaborator.elaborate(s.getFaceList());
  }

}
