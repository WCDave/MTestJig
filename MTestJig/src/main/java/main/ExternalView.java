package main;

import VMath.VMath;
import enums.ViewTypes;
import main.externalView.keyResponder.ExternalSlewKeyResponder;
import main.externalView.keyResponder.IKeyResponder;
import main.externalView.mouse.ExternalViewMouseInputListener;
import main.externalView.positioning.ExternalViewPositioner;
import orbits.CoordSys;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;


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

  private Map<ViewTypes, IKeyResponder<KeyEvent>> keyResponderMap;

  private Dimension EXT_VIEW_SIZE_DIM = new Dimension(MainApp.APP_SIZE_DIM.width-30, MainApp.APP_SIZE_DIM.height-70);

  private CoordSys initialCoordSys;

  public ExternalView(AbstractInstrument[] instruments, Craft mainCraft) {
    super();
    this.instruments = instruments;
    if (this.instruments.length != 0) {
      setCraft(mainCraft);
    }
    initGUI();
    keyResponderMap = new HashMap<ViewTypes, IKeyResponder<KeyEvent>>(){{
      put(ViewTypes.SLEW, new ExternalSlewKeyResponder(ExternalView.this));
      put(ViewTypes.ROCKET, new ExternalSlewKeyResponder(ExternalView.this));
      put(ViewTypes.GEOSYNCH, new ExternalSlewKeyResponder(ExternalView.this));
    }};
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

  public CoordSys getInitialCoordSys() {
    return initialCoordSys;
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
    IKeyResponder<KeyEvent> responder = keyResponderMap.get(viewType);
    responder.respond(evt);
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
