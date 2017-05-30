package orbits.movement;


import enums.AxisType;
import orbits.Planet;
import orbits.World3DContainer;

public class LaunchPointMovement implements Movement {

  private final Planet planet;

  public LaunchPointMovement(Planet planet) {
    this.planet = planet;
  }

  @Override
  public void execute(double time) {
    double angle = -time * planet.getAngVel()[2];
    World3DContainer.getInstance().setLaunchPoint(planet.getCoordSys().rotatePointAroundAxis(AxisType.Z, World3DContainer.getInstance().getLaunchPoint(), angle));
  }
}
