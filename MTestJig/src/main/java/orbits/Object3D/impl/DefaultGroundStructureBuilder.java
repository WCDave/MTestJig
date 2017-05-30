package orbits.Object3D.impl;

import VMath.VMath;
import orbits.Facet;
import orbits.Object3D.IObjectBuilder;

import java.awt.*;
import java.util.Arrays;

public class DefaultGroundStructureBuilder implements IObjectBuilder<AbstractGroundStructure> {

  private DefaultGroundStructure structure;

  public DefaultGroundStructureBuilder(DefaultGroundStructure struct) {
    structure = struct;
  }

  @Override
  public void build() {

    double[] p1 = new double[]{0,0,0};
    double[] p2 = VMath.vecAdd(p1, VMath.vecMultByScalar(structure.getCoordSys().yAxis().getVectorForm(), structure.getLength()));
    double[] p3 = VMath.vecAdd(p1, VMath.vecMultByScalar(structure.getCoordSys().xAxis().getVectorForm(), structure.getWidth()));
    double[] p4 = VMath.vecAdd(p2, p3);

    double[] p5 = VMath.vecAdd(p1, VMath.vecMultByScalar(structure.getCoordSys().zAxis().getVectorForm(), structure.getHeight()));
    double[] p6 = VMath.vecAdd(p2, VMath.vecMultByScalar(structure.getCoordSys().zAxis().getVectorForm(), structure.getHeight()));
    double[] p7 = VMath.vecAdd(p3, VMath.vecMultByScalar(structure.getCoordSys().zAxis().getVectorForm(), structure.getHeight()));
    double[] p8 = VMath.vecAdd(p4, VMath.vecMultByScalar(structure.getCoordSys().zAxis().getVectorForm(), structure.getHeight()));
    //double[] p8 = VMath.vecAdd(p7, p6);
    double[] geoCenter = VMath.vecAdd(VMath.vecAdd(VMath.vecAdd(VMath.vecAdd(VMath.vecAdd(VMath.vecAdd(VMath.vecAdd(p1, p2), p3), p4), p5), p6), p7), p8);
    structure.setGeometricCenter(VMath.vecAdd(structure.getCoordSys().getPositionVec(), VMath.vecMultByScalar(geoCenter, .125)));


    Facet f1 = new Facet(Arrays.asList(p1, p2, p6, p5), structure, false, null, null);
    f1.setMv(VMath.vecMultByScalar(structure.getCoordSys().xAxis().getVectorForm(), -1));
    f1.setDrawFaceOutlines(true);
    f1.setColor(Color.GREEN);
    Facet f2 = new Facet(Arrays.asList(p1, p5, p7, p3), structure, false, null, null);
    f2.setMv(VMath.vecMultByScalar(structure.getCoordSys().yAxis().getVectorForm(), -1));
    f2.setDrawFaceOutlines(true);
    f2.setColor(Color.red);
    Facet f3 = new Facet(Arrays.asList(p2, p4, p8, p6), structure, false, null, null);
    f3.setMv(structure.getCoordSys().yAxis().getVectorForm());
    f3.setDrawFaceOutlines(true);
    f3.setColor(Color.BLUE);
    Facet f4 = new Facet(Arrays.asList(p4, p3, p7, p8), structure, false, null, null);
    f4.setMv(structure.getCoordSys().xAxis().getVectorForm());
    f4.setDrawFaceOutlines(true);
    f4.setColor(Color.cyan);
    Facet f5 = new Facet(Arrays.asList(p6, p8, p7, p5), structure, false, null, null);
    f5.setMv(structure.getCoordSys().zAxis().getVectorForm());
    f5.setDrawFaceOutlines(true);
    f5.setColor(Color.MAGENTA);
    Facet f6 = new Facet(Arrays.asList(p1, p3, p4, p2), structure, false, null, null);
    f6.setMv(VMath.vecMultByScalar(structure.getCoordSys().zAxis().getVectorForm(), -1));
    f6.setDrawFaceOutlines(true);
    f6.setColor(Color.ORANGE);

    synchronized (structure.getFaceList()) {
      structure.getFaceList().clear();
      structure.getFaceList().add(f1);
      structure.getFaceList().add(f2);
      structure.getFaceList().add(f3);
      structure.getFaceList().add(f4);
      structure.getFaceList().add(f5);
      structure.getFaceList().add(f6);
    }
  }
}
