package autopilot.pathPrecision.impl;


import VMath.VMath;
import autopilot.pathPrecision.ICorridorSegment;
import enums.ControlInputSource;
import orbits.CoordSys;
import orbits.NavComputer;
import org.apache.commons.math3.util.FastMath;

public class GravityTurnThrustOnlyCorridorControl extends AbstractCorridorControlBase<Integer, Integer, Integer> {
  private NavComputer computer;
  private ICorridorSegment<Integer, Integer> mostRecentSegment;
  private int baselineThrustValue;


  public GravityTurnThrustOnlyCorridorControl(NavComputer c) {
    computer = c;
  }

  @Override
  public void controlChange(ICorridorSegment<Integer, Integer> corridor) {
    int pitch = getPitch();
    int alt = getAlt();
    int result = 0;

    computer.getControlAdapter().rampAdjustThrust(baselineThrustValue - computer.getControlAdapter().getThrottleSetting(), ControlInputSource.COMPUTER);
    if (!isInCorridor(corridor, alt) ) {
      result = -FastMath.round((alt - corridor.getCenter())/1000);
      computer.getControlAdapter().rampAdjustThrust(result, ControlInputSource.COMPUTER);
      //System.out.println("correcting "+result);
    }

  }

  @Override
  public Integer deviation(ICorridorSegment<Integer, Integer> corridor, Integer input) {
    return input - corridor.getCenter();
  }

  public void setBaselineThrustValue(int value) {
    this.baselineThrustValue = value;
  }

  private int getPitch() {
    CoordSys cs = computer.getCraft().getCoordSys();
    double[] rBar = VMath.vecSubtract(cs.getPositionVec(), computer.getReferenceObject().getCoordSys().getPositionVec());

    double zDotRad = VMath.dotprod(cs.zAxis().getVectorForm(), VMath.normalize(rBar));
    //double pitch = 1-zDotRad;
    //System.out.println(zDotRad+", "+ (90 - Math.toDegrees(Math.acos(zDotRad))));
    return (int) (90-FastMath.toDegrees(FastMath.acos(zDotRad)));
  }

  private int getAlt() {
    CoordSys cs = computer.getCraft().getCoordSys();
    return (int) VMath.mag(VMath.vecSubtract(cs.getPositionVec(), computer.getReferenceObject().getCoordSys().getPositionVec()));
  }


}
