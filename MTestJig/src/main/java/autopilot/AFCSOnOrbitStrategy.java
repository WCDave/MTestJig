package autopilot;

import Foundation.Utils;
import VMath.VMath;
import main.Command;
import orbits.CoordSys;
import orbits.NavComputer;
import org.apache.commons.math3.util.FastMath;

import java.util.List;

public abstract class AFCSOnOrbitStrategy extends AFCSStrategy {
  protected double e1, e2;
  protected double dTheta = .000035;
  protected OnOrbitAlignmentStrategy targetStrategy;

  public AFCSOnOrbitStrategy(NavComputer computer) {
    super(computer);
    this.setUncaughtExceptionHandler(new ThreadExceptionHandler());
  }

  public void run() {
    super.run();
    CoordSys cs = computer.getCraft().getCoordSys();

    double[] xTarget, yTarget, zTarget;
    double rotSign;
    computer.setFlashAnnun(true);
    computer.getCraft().nullRates();


    Thread currentThread = Thread.currentThread();
    while (currentThread == blinkerThread) {
      do {
        do {
          List<double[]> targetList = targetStrategy.acquireTarget();
          zTarget = targetList.get(2);
          yTarget = targetList.get(1);
          xTarget = targetList.get(0);

          double[] arbAxis = VMath.normalize(VMath.crossprd(cs.zAxis().getVectorForm(), zTarget));
          e1 = 1 - VMath.dotprod(cs.zAxis().getVectorForm(), zTarget);
          rotSign = -FastMath.signum(VMath.dotprod(VMath.crossprd(cs.zAxis().getVectorForm(), zTarget), arbAxis));
          cs.rotateAroundArbitraryAxis(arbAxis, dTheta * rotSign * (2 * e1 + .05));
        } while (e1 > e1Tolerance && currentThread == blinkerThread);

        e2 = 1 - VMath.dotprod(cs.xAxis().getVectorForm(), xTarget);
        rotSign = FastMath.signum(VMath.dotprod(cs.xAxis().getVectorForm(), yTarget));
        cs.zRotate(2.5 * dTheta * rotSign * (e2 + .05));
      } while (currentThread == blinkerThread && (e2 > Command.alignError || e1 > e1Tolerance));
      Utils.sleep(20);

      computer.setFlashAnnun(false);
    }
    computer.setFlashAnnun(false);
    //System.out.println("leaving"+this.getName());
  }

  private static class ThreadExceptionHandler implements UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
      System.out.println("Thread " + t.getName() + " has exceptioned: " + e.getMessage());
    }

  }
}
