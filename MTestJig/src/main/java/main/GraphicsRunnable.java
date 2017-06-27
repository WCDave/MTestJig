package main;

import Foundation.Utils;


public class GraphicsRunnable implements Runnable {

  private final AbstractView[] viewArray;
  private long waitTime;

  public GraphicsRunnable(AbstractView[] anArray) {
    viewArray = anArray;
    this.waitTime = (long)(100.0/(double)Runtime.getRuntime().availableProcessors());
  }

  public void run() {
    while (true) {
      for (int i = 0; i < viewArray.length; i++) {
        if (viewArray[i].isVisible()) {
          Utils.sleep(waitTime, 0);
          viewArray[i].update();
        }
      }
    }
  }
}


