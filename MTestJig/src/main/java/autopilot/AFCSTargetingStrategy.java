package autopilot;

import main.Rocket;
import orbits.Abstract3DModelObject;
import orbits.CoordSys;
import orbits.NavComputer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AFCSTargetingStrategy extends AFCSStrategy {

  protected ExecutorService executorService = Executors.newFixedThreadPool(2);
  private double rotationRate;
  protected Rocket rocket;
  protected CoordSys cs;
  protected Abstract3DModelObject referencedObject;

  public AFCSTargetingStrategy(NavComputer computer) {
    super(computer);
    rocket = (Rocket) computer.getCraft();
    cs = computer.getCraft().getCoordSys();
    referencedObject = computer.getReferenceObject();
  }

  public abstract boolean targetReached();

  public void adjustRotationRate(double adjustment) {
    rotationRate += adjustment;
  }

  public double getRotationRate() {
    return rotationRate;
  }
}
