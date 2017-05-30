package main;

import orbits.NavComputer;
import orbits.Planet;
import Foundation.Utils;
import VMath.VMath;

public class TestThread extends Thread {

  private NavComputer computer;

  public TestThread(NavComputer computer) {
    this.computer = computer;
  }

  @Override
  public void run() {
    while (true) {
      int ii = 9;
      VMath.dotprod(((Rocket) computer.getCraft()).getCoordSys().zAxis().getVectorForm(), VMath.normalize(((Planet) computer.getReferenceObject()).getVelocity()));
      Utils.sleep(3000000);
    }
  }
}
