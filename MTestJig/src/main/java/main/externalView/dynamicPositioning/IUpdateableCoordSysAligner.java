package main.externalView.dynamicPositioning;


import orbits.CoordSys;

public interface IUpdateableCoordSysAligner {
  void updateTargetCoordSys(CoordSys target);
  void start();
  void stop();
}
