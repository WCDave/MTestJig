package orbits.Object3D;


import orbits.Object3D.impl.AbstractGroundStructure;

import java.util.List;

public interface IStructureContainer {

  List<AbstractGroundStructure> getStructures();
}
