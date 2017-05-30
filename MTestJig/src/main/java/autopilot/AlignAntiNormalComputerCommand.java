package autopilot;

import main.Command;
import orbits.NavComputer;

public class AlignAntiNormalComputerCommand implements Command {
  private NavComputer computer;
  private final String annunMsg = "AntiNormal";
  private AFCSStrategy cmdThread;

  public AlignAntiNormalComputerCommand(NavComputer computer) {
    this.computer = computer;
  }

  public void execute() {
    computer.getCraft().setAutoPilotInhibit(true);
    computer.setAnnunMsg(annunMsg);
    cmdThread = new AntiNormalStrategy(computer);
    cmdThread.start();
  }

  public void cancel() {
    computer.getCraft().setAutoPilotInhibit(false);
    if (computer.getAnnunMsg() != null && computer.getAnnunMsg().equals(annunMsg)) {
      computer.setAnnunMsg(null);
    }
    if (cmdThread != null) {
      //cmdThread.stop();
      cmdThread.setBlinkerThread(null);
      cmdThread = null;
    }
  }
}
