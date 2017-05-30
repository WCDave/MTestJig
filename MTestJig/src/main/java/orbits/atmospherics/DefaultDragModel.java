package orbits.atmospherics;


public class DefaultDragModel implements IDragModel {

  private final double dragFactor;

  public DefaultDragModel(double dragFactor) {
    this.dragFactor = dragFactor;
  }

  @Override
  public double getDragFactor() {
    return dragFactor;
  }
}
