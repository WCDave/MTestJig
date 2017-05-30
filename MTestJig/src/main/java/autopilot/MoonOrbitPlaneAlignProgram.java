package autopilot;

import orbits.*;

public class MoonOrbitPlaneAlignProgram extends OrbitPlaneAlignProgram {

  private Planet moon;

  public MoonOrbitPlaneAlignProgram(NavComputer computer) {
    super(computer);
    moon = (Planet) World3DContainer.getInstance().getItem("Moon");
    transformer = new OrbitToOrbitChangeTransformer(rocket, moon, computer);
  }



}
