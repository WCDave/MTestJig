package autopilot;

import java.text.SimpleDateFormat;
import java.util.Date;

import Foundation.ThreadMonitorAndFactory;

import main.Command;
import orbits.NavComputer;

public abstract class AFCSStrategy extends Thread {
  protected NavComputer computer;
  protected double e1Tolerance = Command.alignError * Command.alignError;
  private Thread joinToThread;
  protected Thread blinkerThread;

  public AFCSStrategy(NavComputer computer) {
    this.computer = computer;
    this.setPriority(Thread.MIN_PRIORITY);
    this.setName(this.getClass().getName() + "/" + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(new Date()));
    blinkerThread = this;
    ThreadMonitorAndFactory.getInstance().addThread(this);
  }

  @Override
  public void run() {
    //System.out.println(this.getClass().getName()+" initialized");
    if (joinToThread != null) {
      try {
        joinToThread.join();
      } catch (InterruptedException e) {
      }
    }
    //System.out.println(this.getClass().getName()+" executing");
  }

  public void setJoinToThread(Thread joinToThread) {
    this.joinToThread = joinToThread;
  }


  public Thread getJoinToThread() {
    return joinToThread;
  }


  @Override
  public void finalize() {
    try {
      super.finalize();
    } catch (Throwable e) {
    }
    System.err.println(new Date() + " " + this.getClass().getCanonicalName() + " finalizing");
  }

  public void setBlinkerThread(Thread blinkerThread) {
    this.blinkerThread = blinkerThread;
  }

  public Thread getBlinkerThread() {
    return blinkerThread;
  }

}
