package main.effects.model;


import orbits.Abstract3DModelObject;

public interface IParticle<T extends Abstract3DModelObject>  {

  Abstract3DModelObject getImpl();
}
