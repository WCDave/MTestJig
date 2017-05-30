package orbits.Object3D.impl;

import VMath.VMath;
import orbits.Facet;
import orbits.Object3D.IObjectBuilder;
import org.apache.commons.math3.util.FastMath;
import surfaces3D.Cylinder;

import java.util.ArrayList;
import java.util.List;

public class CylinderBuilder implements IObjectBuilder<Cylinder> {

  Cylinder c;

  public CylinderBuilder(Cylinder c) {
    this.c = c;
  }

  @Override
  public void build() {
    List<double[]> topVertexList = new ArrayList<double[]>();
    List<double[]> bottomVertexList = new ArrayList<double[]>();
    c.getFaceList().clear();
    for (int i = 0; i < 360; i += c.getFacetDim().width) {
      double x1 = c.getRadius() * FastMath.cos(FastMath.toRadians(i));
      double y1 = c.getRadius() * FastMath.sin(FastMath.toRadians(i));

      double x2 = c.getRadius() * FastMath.cos(FastMath.toRadians(i + c.getFacetDim().width));
      double y2 = c.getRadius() * FastMath.sin(FastMath.toRadians(i + c.getFacetDim().width));

      if (c.isCappedEnds()) {
        topVertexList.add(c.getCoordSys().transformVec(new double[]{x1, y1, c.getTotalHeight() / 2 + c.getFacetDim().height}));
        bottomVertexList.add(c.getCoordSys().transformVec(new double[]{x1, y1, -c.getTotalHeight() / 2}));
      }

      double z1 = -c.getTotalHeight() / 2 - c.getFacetDim().height;
      for (int j = 0; j <= c.getTotalHeight() / c.getFacetDim().height; j++) {
        z1 = z1 + c.getFacetDim().height;
        double z2 = c.getFacetDim().height + z1;

        List<double[]> pointList = new ArrayList<double[]>(4);
        pointList.add(c.getCoordSys().transformVec(new double[]{x1, y1, z1}));
        pointList.add(c.getCoordSys().transformVec(new double[]{x2, y2, z1}));
        pointList.add(c.getCoordSys().transformVec(new double[]{x2, y2, z2}));
        pointList.add(c.getCoordSys().transformVec(new double[]{x1, y1, z2}));

        Facet face = new Facet(pointList, c, true, x1 + "," + y1 + "," + z1, null);
        face.setMv(c.getCoordSys().transformVec(VMath.normalize(new double[]{x1, y1, 0})));
        c.getFaceList().add(face);
      }
    }
    if (c.isCappedEnds()) {
      Facet capTop = new Facet(topVertexList, c, true, "topcap", null);
      c.getFaceList().add(capTop);

      Facet capBottom = new Facet(bottomVertexList, c, true, "bottomcap", null);
      c.getFaceList().add(capBottom);
    }
//        if(c.getFaceList().size() != c.getNumFaces()) {
//            for(int i=0;i<c.getFaceList().size() - c.getNumFaces();i++)  {
//               c.getFaceList().remove(i);
//            }
//        }
  }
}
