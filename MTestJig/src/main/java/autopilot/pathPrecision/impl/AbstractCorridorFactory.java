package autopilot.pathPrecision.impl;

import autopilot.pathPrecision.ICorridorSegment;

public abstract class AbstractCorridorFactory<I, O> {

  public abstract ICorridorSegment<I, O> getCorridor(final I input);
}
