package main;

import gfxmain.GFXException;
import gfxmain.GFXFramework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

public class FuelGauge2 extends AbstractInstrumentTest {

  private GFXFramework gfx;

  public FuelGauge2(CockPitView aView, Dimension d) {
    super(aView);
    this.setPreferredSize(d);
    try {
      gfx = new GFXFramework(this, true);
    } catch (GFXException e) {
    }
    setLayout(null);
    setBounds(484, 145, 80, 80);
    setVisible(true);
    gfx.init();
  }

  @Override
  public void updateInstrument() {
    Craft aBody = viewOwnedBy.getTheRocket();
    if (aBody instanceof Rocket) {
      Graphics2D g2 = gfx.getG2();
      int wdt = gfx.getWidth();
      int hgt = gfx.getHeight();

      Rocket aRocket = (Rocket) aBody;
      float fuelRemaining = aRocket.getFuelMass();

      String s = String.format("%8d", (int) fuelRemaining / 100);
      g2.setColor(Color.BLACK);
      g2.setClip(null);

      g2.setFont(new Font("FuelAmt", Font.PLAIN, 10));
      g2.fillRoundRect(wdt / 2 - 13, 4, 32, 11, 5, 5);
      g2.setColor(Color.YELLOW);
      g2.drawString(s, 19, 13);

      g2.setColor(Color.BLACK);
      g2.setClip(new RoundRectangle2D.Float(wdt / 2 - 16, hgt - 60, 40, 55, 10, 10));
      g2.fillRect(0, hgt - 60, wdt, 55);

      g2.setColor(Color.GREEN);
      int height = (int) (fuelRemaining / aRocket.getInitialFuel() * 55);
      g2.fillRect(0, hgt - 60 - (height - 55), wdt, 110 - height);
      gfx.update();
    }
  }

}


