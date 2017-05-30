package autopilot.pathPrecision;


public interface ICorridorSegment<I, O> {

  O getUpperBound();
  O getLowerBound();
  boolean isInCorridor(I input);
  O getCenter();
}
