package orbits.Object3D;


import orbits.Abstract3DModelObject;

public interface IObjectBuilder<T extends Abstract3DModelObject> {
  void build();

}
