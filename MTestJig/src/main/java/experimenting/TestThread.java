package experimenting;

public class TestThread extends Thread {

  private boolean parentThread;


  public void run() {
    if (parentThread) {
      new ThreadStat();
      TestThread t2 = new TestThread();
      t2.setName("Child");
      t2.start();
    }


    while (true) {
      int xx = 5;
      ThreadStat stat = ThreadStat.getInstance();
      String sessionId = null;
      if (stat != null)
        sessionId = stat.getSessionId();

      System.out.println(getName() + "  " + sessionId);
      if (parentThread) {
        if (stat != null) {
          stat.setSessionId("AAA");
          sessionId = stat.getSessionId();
        }


        System.out.println(getName() + " cleaning in 5 seconds " + sessionId);
        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        if (stat != null)
          stat.clean();
      }

      stat = ThreadStat.getInstance();
      if (stat != null)
        sessionId = stat.getSessionId();

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
      }
      System.out.println(getName() + "  " + sessionId);
      int h = 0;
    }
  }


  public void setParentThread(boolean parentThread) {
    this.parentThread = parentThread;
  }


  public boolean isParentThread() {
    return parentThread;
  }

}
