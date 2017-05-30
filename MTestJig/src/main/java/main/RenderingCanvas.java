package main;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.List;

public class RenderingCanvas extends Canvas {

  private Container parentComponent;
  private BufferStrategy strategy;
  private Graphics2D g2;
  private Dimension screenCenter;

  public RenderingCanvas(Dimension d, Container parentComponent) {
    this(parentComponent);
    setSize(d);
    setPreferredSize(d);
    setVisible(true);
    screenCenter = new Dimension(d.width>>1, d.height>>1);
  }

  public RenderingCanvas(Container parentComponent) {
    setVisible(true);
    this.parentComponent = parentComponent;
  }

  public void initCanvas() {
    createBufferStrategy(2);
    strategy = getBufferStrategy();
    g2 = (Graphics2D) strategy.getDrawGraphics();
  }

  public void update(Graphics g) {

    if (parentComponent instanceof AbstractInstrument) {
      Component[] aList = parentComponent.getComponents();
      for (Component c : aList) {
        if (!c.getClass().equals(this.getClass()))
          c.paint(g2);
      }
    }
    g2 = (Graphics2D) strategy.getDrawGraphics();
    strategy.show();
  }


  public Graphics2D getG2() {
    return g2;
  }

  public Dimension getScreenCenter() {
    return screenCenter;
  }
}
