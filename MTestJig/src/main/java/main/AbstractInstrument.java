package main;

import gui.INamedControlContainer;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.Collections;
import java.util.Map;

public abstract class AbstractInstrument extends JPanel implements INamedControlContainer {

  protected int w;
  protected int h;
  protected CockPitView viewOwnedBy;
  protected RenderingCanvas instCanvas;
  //protected GFXFramework gfx;
  protected Shape clipShape;
  protected Font font1;
  protected Font font2;

  protected Stroke stroke0 = new BasicStroke(1);
  protected Stroke stroke1;

    
    
    
    /*------------------------------------------------*/

  public AbstractInstrument(CockPitView aView) {
//    	try {
//			gfx = new GFXFramework(this,true);
//		} catch (GFXException e) {
//		}
    viewOwnedBy = aView;
    setLayout(new FlowLayout());
    //setLayout(null);
    setInstCanvas(new RenderingCanvas(this));
    setBackground(new Color(212, 208, 200));
    setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
  }

  public void setInstCanvas(RenderingCanvas instCanvas) {
    this.instCanvas = instCanvas;
    add(instCanvas);
  }

  public RenderingCanvas getInstCanvas() {
    return instCanvas;
  }

  public void updateInstrument() {
    instCanvas.repaint();
  }

  @Override
  public void setSize(int w, int h) {
    this.w = w;
    this.h = h;
    instCanvas.setPreferredSize(new Dimension(w - 8, h - 8));
    instCanvas.setSize(w - 8, h - 8);
    instCanvas.setVisible(true);
    instCanvas.setBounds(4, 4, w - 8, h - 8);
    super.setSize(w, h);
  }

  @Override
  public Map<String, Component> getNamedComponents() {
    return Collections.EMPTY_MAP;
  }
}
