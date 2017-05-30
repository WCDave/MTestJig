package surfaces3D;


import orbits.Facet;

public interface IShadowCalculator<T> {

  float isInShadow(double[] position);

  float isInShadow(T t, IShadowProducer shadeProducer);
}
