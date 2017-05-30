package main.externalView.dynamicPositioning;

import VMath.VMath;
import main.Command;
import orbits.CoordSys;
import org.apache.commons.math3.util.FastMath;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class UpdateableCoordSysAligner implements IUpdateableCoordSysAligner {
  private CoordSys destCoordSys;
  private CoordSys targetCoordSys;
  private AlignerThread t;
  private Logger log = Logger.getLogger(UpdateableCoordSysAligner.class);
  private double dt;
  private long t0;

  public UpdateableCoordSysAligner(CoordSys destCoordSys, CoordSys targetCoordSys) {
    this.destCoordSys = destCoordSys;
    this.targetCoordSys = targetCoordSys;
    t0 = System.currentTimeMillis();
  }

  @Override
  public void updateTargetCoordSys(CoordSys target) {
//    long currTime = System.currentTimeMillis();
//    dt = (currTime - t0)/1000d;
//    t0 = currTime;
    this.targetCoordSys = target;

  }

  @Override
  public void start() {
    t = new AlignerThread();
    t.start();
  }

  @Override
  public void stop() {
   t.setHalt(true);
  }

  private class AlignerThread extends Thread {
    private boolean halt;
    protected double e1, e2;
    protected double dTheta = .000035;
    protected double e1Tolerance = Command.alignError * Command.alignError;
    private double displacementDelta = .1;
    private List<double[]> dispVecHist = new ArrayList<double[]>();

    @Override
    public void run() {
      double[] zTarget;
      double rotSign;
      boolean intercepted = false;
      //while (!halt) {
        do {

          do {
            double[] dPos = determineDisplacementVectorAdjustment(targetCoordSys.getPositionVec(), destCoordSys.getPositionVec(), intercepted);
            zTarget = targetCoordSys.zAxis().getVectorForm();

            double[] arbAxis = VMath.normalize(VMath.crossprd(destCoordSys.zAxis().getVectorForm(), zTarget));
            e1 = 1 - VMath.dotprod(destCoordSys.zAxis().getVectorForm(), zTarget);
            rotSign = -FastMath.signum(VMath.dotprod(VMath.crossprd(destCoordSys.zAxis().getVectorForm(), zTarget), arbAxis));
            if(VMath.mag(dPos) < .09 || intercepted) {
              intercepted = true;
              destCoordSys.rotateAroundArbitraryAxis(arbAxis, dTheta * rotSign * (2 * e1 + .05));
            }
            destCoordSys.setPositionAsVec(VMath.vecAdd(destCoordSys.getPositionVec(), dPos));

          } while (e1 > e1Tolerance && !halt);

          e2 = 1 - VMath.dotprod(destCoordSys.xAxis().getVectorForm(), targetCoordSys.xAxis().getVectorForm());
          rotSign = FastMath.signum(VMath.dotprod(destCoordSys.xAxis().getVectorForm(), targetCoordSys.yAxis().getVectorForm()));
          destCoordSys.zRotate(2.5 * dTheta * rotSign * (e2 + .05));
        } while (!halt && (e2 > Command.alignError || e1 > e1Tolerance));
     // }
      //destCoordSys = targetCoordSys;
      log.info(AlignerThread.this.getName()+" ended");
    }


    public void setHalt(boolean halt) {
      this.halt = halt;
    }

    double[] determineDisplacementVectorAdjustment(double[] targetLoc, double[] destLoc, boolean intercepted) {
      double dispMag = VMath.mag(VMath.vecSubtract(destLoc, targetLoc));
      //System.out.println(intercepted);
      return VMath.vecMultByScalar(VMath.normalize(VMath.vecSubtract(destLoc, targetLoc)), -FastMath.exp(intercepted ? (-500/dispMag)  : -1900/(FastMath.sqrt(dispMag))));
    }
  }
}
