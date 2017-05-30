package orbits;

import java.awt.Graphics2D;

import Foundation.Utils;

import main.AbstractView;

public class FacetDetailDrawRunnable implements Runnable {
  private Facet face;
  private AbstractView view;
  private Graphics2D g2;

  public FacetDetailDrawRunnable(Facet f, Graphics2D g2, AbstractView view) {
    face = f;
    this.view = view;
    this.g2 = g2;
  }

  @Override
  public void run() {
    for (Facet f : face.getDetailList()) {
      f.draw(view);
      //Utils.sleep(5);
    }

  }

}
