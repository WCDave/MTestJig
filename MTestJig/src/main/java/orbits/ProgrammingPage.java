package orbits;

import gui.GlassKeyPad;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import Foundation.ThreadMonitorAndFactory;
import gui.INamedControlContainer;

public class ProgrammingPage extends NavComputerDisplay implements INamedControlContainer {

  public static final String pageName = "Page2";

  private GlassKeyPad keyPad;
  private GlassKeyPad.KeyPadAdapter keyPadAdapter;
  private StringBuffer commandString;


  public ProgrammingPage(NavComputer computer) {
    super(computer);
    keyPad = new GlassKeyPad(new Point(20, 20), new Dimension(130, 145), computer);
    keyPad.setPaintBorder(true);
    keyPadAdapter = keyPad.getAdapter();
    listener = keyPad.getAdapter();
  }

  @Override
  public void drawPage(Graphics2D g2) {
    keyPad.paint(g2);
    if (keyPadAdapter.isDataAvailable()) {
      commandString = new StringBuffer(keyPadAdapter.getValue());
    }
    g2.setColor(Color.WHITE);
    ThreadMonitorAndFactory tmf = ThreadMonitorAndFactory.getInstance();
    g2.drawString("" + tmf.checkThreads(), 100, 100);
  }

  public String toString() {
    return pageName;
  }

  @Override
  public void initPage() {
    List<MouseListener> listeners = Arrays.asList(computer.getInstCanvas().getMouseListeners());
    if (!listeners.contains(listener)) {
      computer.getInstCanvas().addMouseListener(listener);
    }
  }

  public GlassKeyPad getKeyPad() {
    return keyPad;
  }

  @Override
  public Map<String, Component> getNamedComponents() {
    return keyPad.getNamedComponents();
  }
}
