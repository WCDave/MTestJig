package autopilot;

import main.Command;
import orbits.NavComputer;

public class AlignRetrogradeComputerCommand implements Command {
  private NavComputer computer;
  private final String annunMsg = "Retrograde";
  private AFCSStrategy cmdThread;

  public AlignRetrogradeComputerCommand(NavComputer computer) {
    this.computer = computer;
  }

  public void execute() {
    computer.getCraft().setAutoPilotInhibit(true);
    computer.setAnnunMsg(annunMsg);
    cmdThread = new RetrogradeStrategy(computer);
    cmdThread.start();
  }

  public void cancel() {
    computer.getCraft().setAutoPilotInhibit(false);
    if (computer.getAnnunMsg() != null && computer.getAnnunMsg().equals(annunMsg))
      computer.setAnnunMsg(null);
    if (cmdThread != null) {
      //cmdThread.stop();
      cmdThread.setBlinkerThread(null);
      cmdThread = null;
    }
  }
}
