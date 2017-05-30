package orbits.atmospherics;


import orbits.Newtonian;

public interface IAeroDynamics extends Newtonian {

  double getDragFactor();
}
