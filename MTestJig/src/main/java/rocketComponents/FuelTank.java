package rocketComponents;

import java.util.Vector;

import orbits.Point3D;
import main.Craft;

public class FuelTank extends Craft {

  private double fuelRemaining;  //mass
  private boolean selected;

  public FuelTank(float fuelAmount, float emptyMass) {
    this.fuelRemaining = fuelAmount;
    mass = emptyMass;
  }

  /**
   * @param fuelRemaining the fuelRemaining to set
   */
  public void setFuelRemaining(float fuelRemaining) {
    this.fuelRemaining = fuelRemaining;
  }

  /**
   * @return the fuelRemaining
   */
  public double getFuelRemaining() {
    if (fuelRemaining <= 0) {
      return 0;
    }
    return fuelRemaining;
  }

  public void decrementFuel(double amount) {
    this.fuelRemaining -= amount;
  }

  @Override
  public float getMass() {
    return mass + (float) fuelRemaining;
  }

  /**
   * @param selected the selected to set
   */
  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  /**
   * @return the selected
   */
  public boolean isSelected() {
    return selected;
  }

  @Override
  public Vector<Point3D> getDataList() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public double getDragFactor() {
    return 55;
  }
}
