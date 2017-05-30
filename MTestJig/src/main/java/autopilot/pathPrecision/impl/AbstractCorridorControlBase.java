package autopilot.pathPrecision.impl;


import autopilot.pathPrecision.ICorridorControl;
import autopilot.pathPrecision.ICorridorSegment;

public abstract class AbstractCorridorControlBase<I, O, C> implements ICorridorControl<I,O, C> {

  @Override
  public boolean isInCorridor(ICorridorSegment<I, I> corridor, I input) {
    return corridor.isInCorridor(input);
  }


}
