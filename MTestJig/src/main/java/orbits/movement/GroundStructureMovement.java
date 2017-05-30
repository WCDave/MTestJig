package orbits.movement;


import enums.AxisType;
import orbits.Object3D.impl.AbstractGroundStructure;
import orbits.Planet;

public class GroundStructureMovement implements Movement {

  private final AbstractGroundStructure[] structures;
  private Planet planet;

  public GroundStructureMovement(AbstractGroundStructure[] structures, Planet planet) {
    this.structures = structures;
    this.planet = planet;
  }

  @Override
  public void execute(double time) {
    double angle = -time * planet.getAngVel()[2];
    for(AbstractGroundStructure struct : structures) {
      //struct.setPosition(planet.getCoordSys().rotatePointAroundAxis(AxisType.Z, struct.getCoordSys().getPositionVec(), angle));
      struct.getBuilder().build();
    }
  }
}
