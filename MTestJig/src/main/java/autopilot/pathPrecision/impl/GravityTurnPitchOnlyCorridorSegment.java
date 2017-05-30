package autopilot.pathPrecision.impl;

import autopilot.pathPrecision.ICorridorSegment;


public class GravityTurnPitchOnlyCorridorSegment implements ICorridorSegment<Integer, Integer> {
  private int upper, lower, center;

  public GravityTurnPitchOnlyCorridorSegment(Integer upper, Integer lower, Integer center) {
    this.upper = upper;
    this.lower = lower;
    this.center = center;
  }

  @Override
  public Integer getUpperBound() {

    return upper;
  }

  @Override
  public Integer getLowerBound() {
    return lower;
  }

  @Override
  public boolean isInCorridor(Integer input) {
    //System.out.println(input-getCenter());
    return input >= getLowerBound() && input <= getUpperBound();
  }

  @Override
  public Integer getCenter() {
    return center;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    GravityTurnPitchOnlyCorridorSegment that = (GravityTurnPitchOnlyCorridorSegment) o;

    if (upper != that.upper) return false;
    if (lower != that.lower) return false;
    return center == that.center;

  }

  @Override
  public int hashCode() {
    int result = upper;
    result = 31 * result + lower;
    result = 31 * result + center;
    return result;
  }
}
