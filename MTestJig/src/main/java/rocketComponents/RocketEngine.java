package rocketComponents;

import main.Rocket;

import java.util.List;

public class RocketEngine {

  private double throttleSetting;
  private double powerRating;
  private FuelTank selectedTank;
  private List<FuelTank> fuelTankList;
  private double mass;
  private ECU ecu;
  private final Rocket rocket;

  public RocketEngine(float powerRating, List<FuelTank> tankList, Rocket rocket) {
    this.powerRating = powerRating;
    fuelTankList = tankList;
    for (FuelTank f : fuelTankList) {
      if (f.isSelected()) {
        selectedTank = f;
      }
    }
    ecu = new ECU(this, 20);
    this.rocket = rocket;
  }

  public double getThrust(double dt) {

    double dmdt = throttleSetting;
    double result = 0;
    if (selectedTank.getFuelRemaining() > 0) {
      if (dt > 0) {
        selectedTank.decrementFuel(dmdt * dt);
      }
      result = powerRating * dmdt;
    }
    return result;
  }


  /**
   * @param throttleSetting the throttleSetting to set
   */
  public void setThrottleSetting(double throttleSetting) {
    this.throttleSetting = throttleSetting;
    getEcu().getParticleThread().thrustChange();

  }

  /**
   * @return the throttleSetting
   */
  public double getThrottleSetting() {
    return throttleSetting;
  }

  public double getMass() {
    double result = mass;
    for (FuelTank f : fuelTankList) {
      result += f.getMass();
    }
    return result;
  }

  public ECU getEcu() {
    return ecu;
  }



  public Rocket getRocket() {
   return rocket;
  }


}
