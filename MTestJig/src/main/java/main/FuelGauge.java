package main;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Collections;
import java.util.Map;

public class FuelGauge extends AbstractInstrument {

  public FuelGauge(CockPitView aView) {
    super(aView);
    setToolTipText("Fuel Gauge");
    clipShape = new RoundRectangle2D.Float(80 / 2 - 16, 80 - 60, 40, 55, 10, 10);
    font1 = new Font("FUEL", Font.BOLD, 9);
    font2 = new Font("FuelAmt", Font.PLAIN, 10);
  }

  @Override
  public void updateInstrument() {
    Graphics2D g2 = instCanvas.getG2();
    g2.setFont(font1);
    g2.setColor(Color.BLACK);
    g2.drawString("Fuel", 1, 9);
    g2.drawString("x100", 1, 20);
    drawGauge(g2);
    super.updateInstrument();
  }

  private void drawGauge(Graphics2D g2) {
    Craft aBody = viewOwnedBy.getTheRocket();
    if (aBody instanceof Rocket) {
      int wdt = instCanvas.getWidth();
      int hgt = instCanvas.getHeight();

      Rocket aRocket = (Rocket) aBody;
      double fuelRemaining = aRocket.getFuelRemaining();

      String s = String.format("%8d", (int) fuelRemaining / 100);
      g2.setColor(Color.BLACK);
      g2.setClip(null);

      g2.setFont(font2);
      g2.fillRoundRect(wdt / 2 - 13, 4, 32, 11, 5, 5);
      g2.setColor(Color.YELLOW);
      g2.drawString(s, 19, 13);

      g2.setColor(Color.BLACK);
      g2.setClip(clipShape);
      g2.fillRect(0, hgt - 60, wdt, 55);

      g2.setColor(Color.GREEN);
      int height = (int) (fuelRemaining / aRocket.getInitialFuel() * 55);
      g2.fillRect(0, hgt - 60 - (height - 55), wdt, 110 - height);
    }
  }

  @Override
  public Map<String, Component> getNamedComponents() {
    return Collections.EMPTY_MAP;
  }
}
