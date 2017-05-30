package Foundation;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadFactory;

public class ThreadMonitorAndFactory implements ThreadFactory {

  private List<Thread> activeThreads;
  private static ThreadMonitorAndFactory instance;

  public static void main(String[] args) {
    Thread t1 = new Thread() {
      public void run() {
        ThreadMonitorAndFactory.getInstance().addThread(this);
      }
    };
    t1.setName("t1");
    t1.start();

    Thread t2 = new Thread() {
      public void run() {
        ThreadMonitorAndFactory.getInstance().addThread(this);
      }
    };
    t2.setName("t2");
    t2.start();

  }

  private ThreadMonitorAndFactory() {
    activeThreads = new ArrayList<Thread>();
  }

  public static ThreadMonitorAndFactory getInstance() {
    if (instance == null) {
      instance = new ThreadMonitorAndFactory();
      //Thread.setDefaultUncaughtExceptionHandler(new DefaultThreadExceptionHandler());
    }
    return instance;
  }

  @Override
  public Thread newThread(Runnable arg0) {
    SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

    Thread result = new Thread(arg0, format.format(new Date()));
    activeThreads.add(result);
    return result;
  }

  public int checkThreads() {
    int result = 0;
    Iterator<Thread> it = activeThreads.iterator();
    while (it.hasNext()) {
      Thread t = it.next();
      if (!t.isAlive() || t.isInterrupted()) {
        result++;
        //it.remove();
        t = null;
      }
    }
    return result;
  }

  public synchronized void addThread(Thread t) {
    activeThreads.add(t);
  }

  public ThreadMXBean getThreadMXBean() {
    return ManagementFactory.getThreadMXBean();
  }

  private static class DefaultThreadExceptionHandler implements UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
      System.out.println("DEF: Thread " + t.getName() + " has an uncaught exception: " + e.getMessage());
    }

  }

}
