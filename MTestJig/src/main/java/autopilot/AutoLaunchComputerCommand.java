package autopilot;

import enums.ComputerButtonKeys;
import gui.ComputerAbstractButton;

import java.util.List;

import main.Command;
import orbits.NavComputer;

public class AutoLaunchComputerCommand implements Command {
  private NavComputer computer;

  public AutoLaunchComputerCommand(NavComputer comp) {
    this.computer = comp;
  }

  public void execute() {
    computer.executeAutoLaunchSequence();
  }

  public void cancel() {
    List<AFCSStrategy> aList = computer.getProgramSequence();
    for (int i = aList.size() - 1; i >= 0; i--) {
      AFCSStrategy ars = aList.get(i);
      ars.stop();
      System.out.println(">>Stopping " + ars.getClass().getCanonicalName());
    }
    ComputerAbstractButton b = computer.getButton(ComputerButtonKeys.PROGRADE);
    b.setSelected(false);
    b.doClick();
    computer.setAnnunMsg(null);
  }

}
