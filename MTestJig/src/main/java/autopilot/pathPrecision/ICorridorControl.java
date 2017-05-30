package autopilot.pathPrecision;


public interface ICorridorControl<I, O, C> {

  boolean isInCorridor(ICorridorSegment<I, I> corridor, I input);
  void controlChange(ICorridorSegment<I, I> corridor);
  O deviation(ICorridorSegment<I, I> corridor, I input);
}
