package orbits.Object3D.impl;

import orbits.Abstract3DModelObject;

import java.util.ArrayList;
import java.util.List;


public abstract class Listenable<T extends Abstract3DModelObject> {

  protected List<T> listeners= new ArrayList<T>();

  public void addListener(T t) {
    listeners.add(t);
  }

}
