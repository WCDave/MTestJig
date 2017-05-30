package main.externalView.mouse;

import main.ExternalView;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Date;


public class ExternalViewMouseInputListener extends MouseInputAdapter {

  private static KeyEvent ZOOM_IN, ZOOM_OUT;

  private ExternalView externalView;


  public ExternalViewMouseInputListener(ExternalView externalView) {
    this.externalView = externalView;
    ZOOM_IN = new KeyEvent(externalView, KeyEvent.KEY_PRESSED, new Date().getTime(), 0, KeyEvent.VK_EQUALS, '=');
    ZOOM_OUT = new KeyEvent(externalView, KeyEvent.KEY_PRESSED, new Date().getTime(), 0, KeyEvent.VK_MINUS, '-');
  }

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseDragged(MouseEvent e) {

  }

  @Override
  public void mouseEntered(MouseEvent e) {
    externalView.requestFocusInWindow();
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    externalView.dispatchEvent(e.getPreciseWheelRotation() < 0 ? ZOOM_IN : ZOOM_OUT);
  }

}
