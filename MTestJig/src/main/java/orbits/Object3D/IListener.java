package orbits.Object3D;

import orbits.Abstract3DModelObject;


public interface IListener<T extends Abstract3DModelObject> {
  void register(T t);
}
