package main;

import VMath.VMath;
import enums.ViewTypes;
import main.externalView.mouse.ExternalViewMouseInputListener;
import main.externalView.positioning.ExternalViewPositioner;
import orbits.CoordSys;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class ExternalView extends AbstractView implements KeyListener {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private RenderingCanvas eViewCanvas;
  private static final float rateInc = 0.2f;
  final float AUtoMeters = 149597870300.9203f;
  private float spd;
  private double[] velVec = new double[4];
  private ViewTypes viewType = ViewTypes.SLEW;
  private AbstractInstrument[] instruments;
  private Craft craft;
  private double[] tmpRelVec;
  private boolean viewLock = true;
  private int planetSelector = 0;

  private Dimension EXT_VIEW_SIZE_DIM = new Dimension(MainApp.APP_SIZE_DIM.width-30, MainApp.APP_SIZE_DIM.height-70);

  private CoordSys initialCoordSys;

  public ExternalView(AbstractInstrument[] instruments, Craft mainCraft) {
    super();
    this.instruments = instruments;
    if (this.instruments.length != 0) {
      setCraft(mainCraft);
    }
    initGUI();
  }

  private void initGUI() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();
    double height = screenSize.getHeight();
    this.setLayout(null);
    {
      eViewCanvas = new RenderingCanvas(EXT_VIEW_SIZE_DIM, this);
      //setViewCanvas(new RenderingCanvas(new Dimension(854, 572)));
      add(eViewCanvas);
      eViewCanvas.setBounds(7, 0, EXT_VIEW_SIZE_DIM.width, EXT_VIEW_SIZE_DIM.height);
      this.setVisible(true);
      this.setPreferredSize(EXT_VIEW_SIZE_DIM);
      this.addKeyListener(this);
      ExternalViewMouseInputListener externalViewMouseInputAdapter = new ExternalViewMouseInputListener(this);
      eViewCanvas.addMouseListener(externalViewMouseInputAdapter);
      eViewCanvas.addMouseMotionListener(externalViewMouseInputAdapter);
      eViewCanvas.addMouseWheelListener(externalViewMouseInputAdapter);
    }
  }

  //public void setViewCanvas(RenderingCanvas eViewCanvas) {
  //	this.eViewCanvas = eViewCanvas;
  //}

  public RenderingCanvas getViewCanvas() {
    return eViewCanvas;
  }

  public CoordSys getCoordSys() {
    return viewingCoordSys;
  }

  @Override
  public int getDetailLevel() {
    return this.viewType.equals(ViewTypes.ROCKET) ? 13 : 11;
  }

  public void setViewingCoordSys(CoordSys aSys) {
    super.setViewingCoordSys(aSys);
    initialCoordSys = (CoordSys) aSys.clone();
    //boolean x = initialCoordSys.equals(viewingCoordSys);
    assert initialCoordSys.equals(viewingCoordSys) : "oops";
  }

  public void setViewType(ViewTypes viewType) {
    this.viewType = viewType;
  }

  public ViewTypes getViewType() {
    return viewType;
  }

  /**
   * This method should return an instance of this class which does
   * NOT initialize it's GUI elements. This method is ONLY required by
   * Jigloo if the superclass of this class is abstract or non-public. It
   * is not needed in any other situation.
   */
  public static Object getGUIBuilderInstance() {
    return new ExternalView(Boolean.FALSE);
  }

  /**
   * This constructor is used by the getGUIBuilderInstance method to
   * provide an instance of this class which has not had it's GUI elements
   * initialized (ie, initGUI is not called in this constructor).
   */
  public ExternalView(Boolean initGUI) {
    super();
  }

  public void keyTyped(KeyEvent e) {
  }

  public void keyPressed(KeyEvent evt) {
    if ((viewType == ViewTypes.SLEW || viewType == ViewTypes.ROCKET || viewType == ViewTypes.GEOSYNCH)) {
      switch (evt.getKeyCode()) {
        case KeyEvent.VK_NUMPAD2:
          viewingCoordSys.xRotate((float) rateInc);
          break;
        case KeyEvent.VK_NUMPAD8:
          viewingCoordSys.xRotate((float) -rateInc);
          break;
        case KeyEvent.VK_NUMPAD4:
          viewingCoordSys.zRotate((float) rateInc);
          break;
        case KeyEvent.VK_NUMPAD6:
          viewingCoordSys.zRotate((float) -rateInc);
          break;
        case KeyEvent.VK_NUMPAD1:
          viewingCoordSys.yRotate((float) -rateInc);
          break;
        case KeyEvent.VK_NUMPAD3:
          viewingCoordSys.yRotate((float) rateInc);
          break;
        case KeyEvent.VK_EQUALS:
          spd = evt.isShiftDown() ? (float) 0.0000001 * AUtoMeters : (float) 0.000001 * AUtoMeters ;
          break;
        case KeyEvent.VK_MINUS:
          spd = evt.isShiftDown() ? (float) -0.0000001 * AUtoMeters : (float) -0.000001 * AUtoMeters ;
          break;
        case KeyEvent.VK_SPACE: {
          velVec[0] = 0;
          velVec[1] = 0;
          velVec[2] = 0;
          spd = 0;
        }
        ;
        break;
        case KeyEvent.VK_SEMICOLON:
          viewingCoordSys = (CoordSys) initialCoordSys.clone();
          break;
        case KeyEvent.VK_0:
          planetSelector = -1;
          new ExternalViewPositioner("Test1", viewingCoordSys, "mud").postionViewingSys();
          break;
        case KeyEvent.VK_1:
          planetSelector = 2;
          new ExternalViewPositioner(2, viewingCoordSys).postionViewingSys();
          break;
        case KeyEvent.VK_2:
          planetSelector = 3;
          new ExternalViewPositioner(3, viewingCoordSys).postionViewingSys();
          break;
        case KeyEvent.VK_3:
          planetSelector = 0;
          new ExternalViewPositioner(0, viewingCoordSys).postionViewingSys();
          break;
        case KeyEvent.VK_4:
          planetSelector = 4;
          new ExternalViewPositioner(4, viewingCoordSys).postionViewingSys();
          break;
        case KeyEvent.VK_5:
          planetSelector = 5;
          new ExternalViewPositioner(5, viewingCoordSys).postionViewingSys();
          break;
        case KeyEvent.VK_6:
          planetSelector = 6;
          new ExternalViewPositioner(6, viewingCoordSys).postionViewingSys();
          break;
        case KeyEvent.VK_7:
          planetSelector = 7;
          new ExternalViewPositioner(7, viewingCoordSys).postionViewingSys();
          break;
        case KeyEvent.VK_8:
          planetSelector = 8;
          new ExternalViewPositioner(8, viewingCoordSys).postionViewingSys();
          break;
        case KeyEvent.VK_9:
          planetSelector = 9;
          new ExternalViewPositioner(9, viewingCoordSys).postionViewingSys();
          break;
        case KeyEvent.VK_M:
          planetSelector = -1;
          new ExternalViewPositioner("Moon", viewingCoordSys, "Earth").postionViewingSys();
          break;

        case KeyEvent.VK_SLASH:
          viewLock = !viewLock;
          break;

      }
      if (spd != 0) {
        velVec = VMath.vecMultByScalar(viewingCoordSys.zAxis().getVectorForm(), spd);
        double[] tempVec = VMath.vecAdd(viewingCoordSys.getPositionVec(), velVec);
        viewingCoordSys.setPositionAsVec(tempVec);
        spd = 0;
      }
    }
  }


  public void keyReleased(KeyEvent e) {
  }


  @Override
  public void update() {
    for (int i = 0; i < instruments.length; i++) {
      instruments[i].updateInstrument();
    }

    viewType.getViewStrategy().applyToView(this);
    super.update();
  }

  public Craft getCraft() {
    return craft;
  }

  public void setCraft(Craft craft) {
    this.craft = craft;
  }

  public boolean isViewLock() {
    return viewLock;
  }

  public void setViewLock(boolean viewLock) {
    this.viewLock = viewLock;
  }


  public int getPlanetSelector() {
    return planetSelector;
  }


  @Override
  public void setVisible(boolean flag) {
    super.setVisible(flag);
//    if(!flag) {
//      viewType.getViewStrategy().shutdown();
//    }
  }


}
