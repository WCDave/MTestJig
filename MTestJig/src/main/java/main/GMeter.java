package main;

import VMath.VMath;
import orbits.NavComputer;
import org.apache.commons.math3.util.FastMath;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class GMeter extends AbstractInstrument {

  public GMeter(CockPitView aView) {
    super(aView);
    setToolTipText("G Meter");
    clipShape =  new Ellipse2D.Float(5, 5, 70, 70);
    font1 = new Font("Gs", Font.BOLD, 9);
    font2 = new Font("GFont", Font.PLAIN, 10);
    stroke1 = new BasicStroke(2);
  }

  @Override
  public void updateInstrument() {
    Graphics2D g2 = instCanvas.getG2();
    drawMeter(g2);
    super.updateInstrument();
  }

  private void drawMeter(Graphics2D g2) {

    double gs = 0;
    double theta;
    int x, y;
    int wdt = instCanvas.getWidth();
    int hgt = instCanvas.getHeight();

    g2.setFont(font1);
    g2.drawString("Gs", 1, 9);
    g2.setClip(clipShape);
    g2.setColor(Color.BLACK);
    g2.fillRect(0, hgt / 2, wdt, hgt);
    g2.setColor(Color.WHITE);
    g2.fillRect(0, 0, wdt, hgt / 2);
    g2.setColor(Color.RED);
    g2.drawArc(10, 10, 60, 60, 0, 180);

    Craft aBody = this.viewOwnedBy.getTheRocket();

    double[] totForce = new double[3];
    for (int i = 0; i < 3; i++) {
      totForce[i] += aBody.getThrust()[i] + aBody.getDrag()[i] + aBody.getNorm()[i];
    }

    gs = VMath.mag(totForce) / (NavComputer.EarthG * aBody.getMass());
    //if(aBody instanceof Rocket)
    //	gs = ((aBody.getExperiencedDrag()+VMath.mag(((Rocket)aBody).computeThrust(-1))+aBody.getExperiencedNorm())/aBody.getMass())/EarthG;
    //else
    //	gs = ((aBody.getExperiencedDrag()+aBody.getExperiencedNorm())/aBody.getMass())/EarthG;
    String s = String.format("%10.4f", gs);
    g2.setFont(font2);
    g2.setColor(Color.YELLOW);
    g2.drawString(s, wdt / 2 - 28, hgt - 20);

    g2.setColor(Color.BLACK);
    theta = (float) FastMath.PI * (1 - gs / 10);
    x = (int) (35 * FastMath.cos(theta));
    y = (int) (35 * FastMath.sin(theta));
    g2.setStroke(stroke1);
    g2.drawLine(wdt / 2, hgt / 2, wdt / 2 + x, hgt / 2 - y);
    g2.setStroke(stroke0);
  }
}
