package orbits.Object3D.impl;


import enums.AxisType;
import mapUtils.MapHelper;
import orbits.CoordSys;
import orbits.Facet;
import orbits.Sphere;
import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;
import java.util.List;

public class RegularIcosahedronSphereBuilder extends SphereBuilderBase {

  private static float VERT_LAT = (float) FastMath.toDegrees(FastMath.atan(.5));

  public RegularIcosahedronSphereBuilder(Sphere s) {
    super(s);
  }

  @Override
  public void build() {
    List<double[]> northernVertices = new ArrayList<double[]>();
    List<double[]> southernVertices = new ArrayList<double[]>();
    CoordSys cs = s.getCoordSys();
    for (int longitude = 0; longitude <= 360; longitude += 72) {
      double[] vert = MapHelper.convertGeographicalDegreesToCartesianCoords(s.getRadius(), VERT_LAT, longitude);
      northernVertices.add(vert);
    }

    for (double[] vert : northernVertices) {
      double[] sVert = cs.rotatePointAroundAxis(AxisType.Z, vert, 36);
      sVert[2] = -sVert[2];
      southernVertices.add(sVert);
    }

    for(int i = 0; i< southernVertices.size(); i++) {
      southernVertices.set(i, southernVertices.get(i));
      northernVertices.set(i, northernVertices.get(i));
    }

    List<Facet> sideFaces = new ArrayList<Facet>();
    for (int i = 0; i < 5; i++) {
      List<double[]> vertList = new ArrayList<double[]>();

      vertList.addAll(northernVertices.subList(i, i + 2));
      vertList.add(southernVertices.get(i + 1));
      Facet f1 = new Facet(vertList, s, true, "tm" + i, null);
      f1.setColor(s.getColor());

      vertList.clear();
      vertList.addAll(southernVertices.subList(i, i + 2));
      vertList.add(northernVertices.get(i));
      Facet f2 = new Facet(vertList, s, true, "bm" + i, null);
      f2.setColor(s.getColor());

      vertList.clear();
      vertList.add(NORTH_POLE);
      vertList.addAll(northernVertices.subList(i, i + 2));
      Facet f3 = new Facet(vertList, s, true, "tp" + i, null);
      f3.setColor(s.getColor());

      vertList.clear();
      vertList.add(SOUTH_POLE);
      vertList.addAll(southernVertices.subList(i, i + 2));
      Facet f4 = new Facet(vertList, s, true, "bt" + i, null);
      f4.setColor(s.getColor());

      sideFaces.add(f1);
      sideFaces.add(f2);
      sideFaces.add(f3);
      sideFaces.add(f4);
    }
    s.getFaceList().addAll(sideFaces);

    super.build();
  }


}
