package main;

import Foundation.Utils;


public class GraphicsRunnable implements Runnable {

  private final AbstractView[] viewArray;

  public GraphicsRunnable(AbstractView[] anArray) {
    viewArray = anArray;
  }

  public void run() {
    while (true) {
      for (int i = 0; i < viewArray.length; i++) {
        if (viewArray[i].isVisible()) {
          Utils.sleep(0, 30);
          viewArray[i].update();
        }
      }
    }
  }
}


