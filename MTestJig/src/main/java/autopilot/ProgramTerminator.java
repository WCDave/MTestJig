package autopilot;

import Foundation.Utils;
import enums.ComputerButtonKeys;
import gui.ComputerAbstractButton;
import orbits.NavComputer;

public class ProgramTerminator extends AFCSStrategy {

  public ProgramTerminator(NavComputer computer) {
    super(computer);
  }

  @Override
  public void run() {
    super.run();
    ComputerAbstractButton button = computer.getButton(ComputerButtonKeys.PROGRADE);
    button.setSelected(false);
    button.doClick();
    computer.getButton(ComputerButtonKeys.AUTOLAUNCH).setSelected(false);
    computer.setAnnunMsg("Program Ended");
    computer.setFlashAnnun(true);
    Utils.sleep(8000);
    computer.setAnnunMsg(null);
  }
}
