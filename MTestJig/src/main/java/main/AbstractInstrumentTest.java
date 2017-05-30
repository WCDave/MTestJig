package main;

import gfxmain.GFXException;
import gfxmain.GFXFramework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class AbstractInstrumentTest extends JPanel {

  protected int w;
  protected int h;
  protected CockPitView viewOwnedBy;
  //protected RenderingCanvas instCanvas;
  protected GFXFramework gfx;
    
    
    
    /*------------------------------------------------*/

  public AbstractInstrumentTest(CockPitView aView) {
    try {
      gfx = new GFXFramework(this, true);
    } catch (GFXException e) {
    }
    gfx.init();
    viewOwnedBy = aView;
    setLayout(new FlowLayout());
    //setLayout(null);

    setBackground(new Color(212, 208, 200));
    setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
  }


  public void updateInstrument() {
    gfx.repaint();
  }

}
