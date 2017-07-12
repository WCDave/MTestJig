package orbits;

import Foundation.Utils;
import VMath.VMath;
import autopilot.*;
import enums.AxisType;
import enums.ComputerButtonKeys;
import enums.OrbitElementKeys;
import gui.*;
import main.*;
import org.apache.commons.math3.util.FastMath;
import org.apache.log4j.Logger;
import rocketComponents.IControlAdapter;
import rocketComponents.RocketControlAdapter;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NavComputer extends AbstractInstrument {

  /**
   *
   */
  private static final long serialVersionUID = 6612654233210919745L;
  public static final double METERS_PER_MILE = 1609.344;
  public static final float G = (float) -6.672e-11;
  public static final float EarthG = -G * (float) 5.99e24 / (float) Math.pow(6.37e6, 2);
  private Craft craft;
  private Graphics2D g2;


  private JButtonGroup apButtonGrp = new JButtonGroup(true);
  private JButtonGroup computerPageGrp = new JButtonGroup(false);
  private String annunMsg;
  private boolean flashAnnun;
  private int annunCounter;
  private Color annunColor = Color.RED;
  private Abstract3DModelObject referenceObject;
  private List<AFCSStrategy> programSequence;
  private Map<ComputerButtonKeys, GlassButton> buttonHash = new HashMap<ComputerButtonKeys, GlassButton>();
  private Map<OrbitElementKeys, Object> rocketOrbitElements;
  private Map<String, Map<OrbitElementKeys, Object>> orbitElementMap;
  private Map<String, AbstractComputerDisplayPage> pageHash = new HashMap<String, AbstractComputerDisplayPage>();
  private AbstractComputerDisplayPage activePage;
  private IControlAdapter controlAdapter;
  private float targetAltitude = 105;

  private long launchTime;

  Map<Pattern, ICommandHandler> targetingStrategyMap;
  private static final Pattern apogeePattern = Pattern.compile("[AM]\\d{1,5}.?\\d*");
  private static final Pattern planeChangePattern = Pattern.compile("[PM]\\d{1,5}.?\\d*");

  private Map<String, Component> controlHash = new HashMap<String, Component>();

  private Logger log = Logger.getLogger(NavComputer.class);

  private CountDownLatch startUpLatch;
  private boolean isLaunched;

  public NavComputer(CockPitView aView, CountDownLatch countDownLatch) {
    super(aView);
    this.startUpLatch = countDownLatch;

    stroke1 = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[]{5}, 0);
    World3DContainer container = World3DContainer.getInstance();
    rocketOrbitElements = new HashMap<OrbitElementKeys, Object>();
    orbitElementMap = new HashMap<String, Map<OrbitElementKeys, Object>>();
    orbitElementMap.put("Rocket", rocketOrbitElements);

    for (Abstract3DModelObject object : container.getItemsAsArray()) {
      Map<OrbitElementKeys, Object> map = new HashMap<OrbitElementKeys, Object>();
      orbitElementMap.put(object.getName(), map);
    }

    targetingStrategyMap = new HashMap<Pattern, ICommandHandler>(){{
      put(Pattern.compile("[A]\\d{1,5}.?\\d*"), new ApogeeCommandHandler(NavComputer.this));
      put(Pattern.compile("[I]-?\\d{1,5}.?\\d*"), new PlaneChangeCommandHandler(NavComputer.this));
      put(Pattern.compile("[C]-?"), new CircularizeCommandHandler(NavComputer.this));
      put(Pattern.compile("[M]"), new MoonCommandHandler(NavComputer.this));
    }};

    controlAdapter = aView.getControlAdapter();
    setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), BorderFactory.createBevelBorder(BevelBorder.LOWERED)));
    GlassButton progradeBtn = new GlassButton(ComputerButtonKeys.PROGRADE.toString(), 190, 1, 13, 13, Color.GREEN.brighter(), new AlignProgradeComputerCommand(this));
    GlassButton retroGradeBtn = new GlassButton(ComputerButtonKeys.RETROGRADE.toString(), 210, 1, 13, 13, Color.GREEN.brighter(), new AlignRetrogradeComputerCommand(this));
    GlassButton normalBtn = new GlassButton(ComputerButtonKeys.NORMAL.toString(), 230, 1, 13, 13, Color.GREEN.brighter(), new AlignNormalComputerCommand(this));
    GlassButton antiNormalBtn = new GlassButton(ComputerButtonKeys.ANTINORMAL.toString(), 250, 1, 13, 13, Color.GREEN.brighter(), new AlignAntiNormalComputerCommand(this));
    GlassButton apEnableBtn = new GlassButton(ComputerButtonKeys.AUTOLAUNCH.toString(), 133, 1, 51, 13, Color.WHITE.brighter(), new AutoLaunchComputerCommand(this));

    add(progradeBtn);
    add(retroGradeBtn);
    add(normalBtn);
    add(antiNormalBtn);
    add(apEnableBtn);

    apButtonGrp.add(progradeBtn);
    apButtonGrp.add(retroGradeBtn);
    apButtonGrp.add(normalBtn);
    apButtonGrp.add(antiNormalBtn);

    instCanvas.addMouseListener(new ComputerMouseListener());
    //addMouseListener(new ComputerMouseListener());
    buttonHash.put(ComputerButtonKeys.PROGRADE, progradeBtn);
    buttonHash.put(ComputerButtonKeys.AUTOLAUNCH, apEnableBtn);
    buttonHash.put(ComputerButtonKeys.RETROGRADE, retroGradeBtn);
    buttonHash.put(ComputerButtonKeys.NORMAL, normalBtn);
    buttonHash.put(ComputerButtonKeys.ANTINORMAL, antiNormalBtn);


    activePage = new DataPage_I(this);
    pageHash.put(DataPage_I.pageName, activePage);
    pageHash.put(ProgrammingPage.pageName, new ProgrammingPage(this));
    for (int i = 0; i < pageHash.size(); i++) {
      final int j = i + 1;
      Command cmd = new Command() {
        public void execute() {
          activePage = pageHash.get("Page" + j);
          activePage.initPage();
        }

        public void cancel() {
        }
      };
      ComputerAbstractButton gb = new GlassButton("" + j, 125 + j * 15, 145, 13, 13, Color.white, cmd);
      add(gb);
      computerPageGrp.add(gb);

      if (i == 0) {
        gb.setSelected(true);
      }
    }

    Command cmd = new Command() {

      @Override
      public void cancel() {
      }

      @Override
      public void execute() {
        Rocket r = (Rocket) NavComputer.this.craft;

          Craft c = r.unloadBay("Test1");
        synchronized (c.getCoordSys()) {
          World3DContainer.getInstance().addCraft(c);
          Thread t = new Thread(new SatelliteSpinUp(c), "Spinup");
          c.getBehaviorThreads().add(t);
          t.start();
        }
      }
    };

    ComputerAbstractButton gb = new GlassButton("S", 125 + 3 * 15, 145, 13, 13, Color.white, cmd);
    add(gb);

    cmd = CommandBuilder.createCommand()
            .setExecutionObject(this)
            .setExecutionMethod("executeDemo")
            .buildCommand();

    gb = new GlassButton("DEMO", 125 + 4 * 15, 145, 37, 13, Color.white, cmd);
    add(gb);
  }

  public Orbit determineOrbitFor(CoordSys cs) {
    return null;
  }

  public ComputerAbstractButton getButton(ComputerButtonKeys key) {
    return buttonHash.get(key);
  }

  public void executeDemo() {
    final Map<String, Component> ctrls = this.controlHash;
    Thread t;
    IButtonExecutionHandler buttonExecutionHandler = new ButtonExecutionHandler();
    t = new Thread(() -> {
      Robot r = null;
      try {
        r = new Robot();
        buttonExecutionHandler.click((GlassButton) ctrls.get("2C"), .5f);

        buttonExecutionHandler.click((GlassButton) ctrls.get("AGP"), .5f);
        buttonExecutionHandler.click((GlassButton) ctrls.get("9GP"), .5f);
        buttonExecutionHandler.click((GlassButton) ctrls.get("0GP"), .5f);
        buttonExecutionHandler.click((GlassButton) ctrls.get(":GP"), .5f);
        buttonExecutionHandler.click((GlassButton) ctrls.get("CGP"), .5f);
        buttonExecutionHandler.click((GlassButton) ctrls.get("PGMGP"), .5f);
        buttonExecutionHandler.click((GlassButton) ctrls.get("XGP"), .5f);

        buttonExecutionHandler.click((GlassButton) ctrls.get("1C"), .5f);
        buttonExecutionHandler.click((JRadioButton) ctrls.get("Aft"), .5f);

        log.debug("T1 - Ext");
        r.keyPress(KeyEvent.VK_ALT);
        Utils.sleep(1000);
        r.keyPress((KeyEvent.VK_V));
        Utils.sleep(1000);
        r.keyRelease(KeyEvent.VK_ALT);
        Utils.sleep(1000);
        r.keyRelease((KeyEvent.VK_V));
        Utils.sleep(2000);

        log.debug("T2 - ExtLP");
        r.keyPress(KeyEvent.VK_ALT);
        Utils.sleep(1000);
        r.keyPress((KeyEvent.VK_R));
        Utils.sleep(1000);
        r.keyRelease(KeyEvent.VK_ALT);
        Utils.sleep(1000);
        r.keyRelease((KeyEvent.VK_R));
        Utils.sleep(1000);
        Utils.sleep(30000);

        log.debug("T3 - Int");
        r.keyPress(KeyEvent.VK_ALT);
        Utils.sleep(1000);
        r.keyPress((KeyEvent.VK_V));
        Utils.sleep(1000);
        r.keyRelease(KeyEvent.VK_ALT);
        Utils.sleep(1000);
        r.keyRelease((KeyEvent.VK_V));
        Utils.sleep(1000);
        Utils.sleep(40000);

        log.debug("T4 - Ext");
        r.keyPress(KeyEvent.VK_ALT);
        Utils.sleep(1000);
        r.keyPress((KeyEvent.VK_V));
        Utils.sleep(1000);
        r.keyRelease(KeyEvent.VK_ALT);
        Utils.sleep(1000);
        r.keyRelease((KeyEvent.VK_V));
        Utils.sleep(1000);
        Utils.sleep(160000);

        log.debug("T5 - Int");
        r.keyPress(KeyEvent.VK_ALT);
        Utils.sleep(1000);
        r.keyPress((KeyEvent.VK_V));
        Utils.sleep(1000);
        r.keyRelease(KeyEvent.VK_ALT);
        Utils.sleep(1000);
        r.keyRelease((KeyEvent.VK_V));
        Utils.sleep(1000);
        Utils.sleep(400000);

        buttonExecutionHandler.click((GlassButton) ctrls.get("PC"), 10f);
        buttonExecutionHandler.click((GlassButton) ctrls.get("SC"), 80f);

        log.debug("T6 - Ext");
        r.keyPress(KeyEvent.VK_ALT);
        Utils.sleep(1000);
        r.keyPress((KeyEvent.VK_V));
        Utils.sleep(1000);
        r.keyRelease(KeyEvent.VK_ALT);
        Utils.sleep(1000);
        r.keyRelease((KeyEvent.VK_V));
        Utils.sleep(1000);

        log.debug("T7 - ExtG");
        r.keyPress(KeyEvent.VK_ALT);
        Utils.sleep(1000);
        r.keyPress((KeyEvent.VK_G));
        Utils.sleep(1000);
        r.keyRelease(KeyEvent.VK_ALT);
        Utils.sleep(1000);
        r.keyRelease((KeyEvent.VK_G));
        Utils.sleep(1000);
      } catch (AWTException e) {
        e.printStackTrace();
      }

      log.info("end demo");
    });
    t.start();
  }

  @Override
  public void updateInstrument() {

    craft = viewOwnedBy.getTheRocket();    

    //rocketOrbitElements.clear();
    new Thread(()->{
      //boolean isFirstTime = rocketOrbitElements.isEmpty();
       rocketOrbitElements.putAll(computeOrbitalElements(craft.getCoordSys(), referenceObject));
       //if(isFirstTime) {
         startUpLatch.countDown();
       //}
    }).start();

    try {
      startUpLatch.await();
    } catch (InterruptedException e) {}
    if (viewOwnedBy.isVisible()) {

      //planet = viewOwnedBy.getReferencedPlanet();

//            new Thread() {
//                public void run() {
      g2 = instCanvas.getG2();
      g2.clearRect(0, 0, w, h);
      annunCounter++;
      activePage.drawPage(g2);

      g2.setStroke(stroke1);
      g2.setColor(Color.gray);
      g2.drawLine(130, 0, 130, h);
      g2.drawLine(130, 20, w, 20);
      g2.setColor(Color.WHITE.darker());
      //g2.drawString("AP Mode",135,12);
      g2.setStroke(stroke0);

      if (annunMsg != null) {
        if (flashAnnun && annunCounter % 10 == 0) {
          annunColor = annunColor.equals(Color.RED) ? Color.BLACK : Color.RED;
        }

        g2.setColor(annunColor);
        g2.drawString(annunMsg, 270, 13);
      }
      NavComputer.super.updateInstrument();
//                }
//            }.start();


    }
  }


  public void setAnnunMsg(String annunMsg) {
    this.annunMsg = annunMsg;
  }

  public String getAnnunMsg() {
    return annunMsg;
  }


  public void setCraft(Craft craft) {
    this.craft = craft;
  }

  public Craft getCraft() {
    return craft;
  }


  public void setReferenceObject(Abstract3DModelObject refObject) {
    this.referenceObject = refObject;
  }

  public Abstract3DModelObject getReferenceObject() {
    return referenceObject;
  }

  public double[] getRelativeRocketVelocity() {
    CoordSys cs = craft.getCoordSys();
    double v[] = VMath.vecSubtract(cs.getVelocityAsVec(), ((Planet) referenceObject).getVelocity());
    return v;
  }

  private static Map<OrbitElementKeys, Object> computeOrbitalElements(CoordSys cs, Abstract3DModelObject refObject) {

    //CoordSys cs = craft.getNavObject();
    Map<OrbitElementKeys, Object> resultMap = new HashMap<OrbitElementKeys, Object>();

    double[] radiusVec, ecc;
    double a, vPer, rPer, vAph, rAph;

    double mu = -((Planet) refObject).getMu();
    //double v[]=cs.getVelocityAsVec();
    double v[] = VMath.vecSubtract(cs.getVelocityAsVec(), ((Planet) refObject).getVelocity());
    double magV = VMath.mag(v);
    radiusVec = VMath.vecSubtract(cs.getPositionVec(), ((Planet) refObject).getCoordSys().getPositionVec());
    double h[] = VMath.crossprd(radiusVec, v);
    ecc = VMath.vecSubtract(VMath.vecMultByScalar(VMath.crossprd(v, h), 1 / mu), VMath.vecMultByScalar(radiusVec, 1 / VMath.mag(radiusVec)));
    double e = VMath.mag(ecc);
    a = -mu / (2 * ((magV * magV) / 2 - mu / VMath.mag(radiusVec)));
    double trPer = (1 - e) * a;
    double trAph = (1 + e) * a;

    rPer = trPer / METERS_PER_MILE;
    rAph = trAph / METERS_PER_MILE;
    vPer = FastMath.sqrt(((1 + e) * mu) / trPer) * 2.236;
    vAph = FastMath.sqrt(((1 - e) * mu) / trAph) * 2.236;

    resultMap.put(OrbitElementKeys.rPer, new Double(rPer));
    resultMap.put(OrbitElementKeys.vPer, new Double(vPer));
    resultMap.put(OrbitElementKeys.rAph, new Double(rAph));
    resultMap.put(OrbitElementKeys.vAph, new Double(vAph));
    resultMap.put(OrbitElementKeys.ecc, ecc);
    resultMap.put(OrbitElementKeys.a, new Double(a));
    resultMap.put(OrbitElementKeys.radiusVec, radiusVec);

    double[] transformedVec = new double[]{refObject.getCoordSys().X(), refObject.getCoordSys().Y(), refObject.getCoordSys().Z()};
    transformedVec = cs.rotatePointAroundAxis(AxisType.X, transformedVec, ((Planet)World3DContainer.getInstance().getItem("Earth")).getObliquity()+28.5);

    resultMap.put(OrbitElementKeys.refX, new Double(transformedVec[0]));
    resultMap.put(OrbitElementKeys.refY, new Double(transformedVec[1]));
    resultMap.put(OrbitElementKeys.refZ, new Double(transformedVec[2]));
    resultMap.put(OrbitElementKeys.refName, refObject.name);

    transformedVec = new double[]{cs.X(), cs.Y(), cs.Z()};
    transformedVec = cs.rotatePointAroundAxis(AxisType.X, transformedVec, ((Planet)World3DContainer.getInstance().getItem("Earth")).getObliquity()+28.5);
    resultMap.put(OrbitElementKeys.rocketX, new Double(transformedVec[0]));
    resultMap.put(OrbitElementKeys.rocketY, new Double(transformedVec[1]));
    resultMap.put(OrbitElementKeys.rocketZ, new Double(transformedVec[2]));

    //resultMap.put(OrbitElementKeys.Launch_Time, new Double(launchTime));
    World3DContainer wc = World3DContainer.getInstance();
    Abstract3DModelObject[] array = wc.getItemsAsArray();
    Map<String, Object> locMap = new HashMap<String, Object>();

    Planet p = (Planet) wc.getItem("Moon");
    locMap.put(p.getName(), p.getCoordSys().getPositionVec());     

    resultMap.put(OrbitElementKeys.locMap, locMap);

    return resultMap;

  }

  public void executeAutoLaunchSequence() {

    try {
      startUpLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    programSequence = new ArrayList<AFCSStrategy>(8);
    programSequence.add(new APEngineStartAndLaunch(this));
    programSequence.add(new RollProgram(this));
    //programSequence.add(new PitchProgram(this));
    programSequence.add(new GravityTurn(this));
    //programSequence.add(new OrbitInsertion(this));
    programSequence.add(new CircularizeProgram(this, new  CircularizationRaisePeriApsis(this)));
    programSequence.add(new MoonOrbitPlaneAlignProgram(this));
    programSequence.add(new TLIProgram(this));
    programSequence.add(new CourseCorrectionManeuverProgram(this, (Planet) World3DContainer.getInstance().getItem("Moon"), 80d));
    programSequence.add(new CircularizeProgram(this, new CircularizationLowerApoApsis(this)));
    programSequence.add(new ProgramTerminator(this));

    runProgramSequence();
  }

  public void moonShot() {

    try {
      startUpLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    double moonTargetAlt = this.targetAltitude;
    targetAltitude = 105;
    programSequence = new ArrayList<AFCSStrategy>(8);
    programSequence.add(new APEngineStartAndLaunch(this));
    programSequence.add(new RollProgram(this));
    //programSequence.add(new PitchProgram(this));
    programSequence.add(new GravityTurn(this));
    //programSequence.add(new OrbitInsertion(this));
    programSequence.add(new CircularizeProgram(this, new  CircularizationRaisePeriApsis(this)));
    programSequence.add(new MoonOrbitPlaneAlignProgram(this));
    programSequence.add(new TLIProgram(this));
    programSequence.add(new CourseCorrectionManeuverProgram(this, (Planet) World3DContainer.getInstance().getItem("Moon"), moonTargetAlt));
    programSequence.add(new CircularizeProgram(this, new CircularizationLowerApoApsis(this)));
    programSequence.add(new ProgramTerminator(this));

    runProgramSequence();
  }

  public void executeAutoLaunchSequencePrg() {

    try {
      startUpLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    programSequence.add(new ProgramTerminator(this));

    runProgramSequence();
  }

  public void executeCommand(String[] commands) {
    try {
      startUpLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    programSequence = new ArrayList<AFCSStrategy>(7);
    if(VMath.mag(craft.getNorm()) > 0) {
      programSequence.add(new APEngineStartAndLaunch(this));
      programSequence.add(new RollProgram(this));
    }

    String dest = null;
    for (String string : commands) {
      for(Pattern p : targetingStrategyMap.keySet()) {
        Matcher m = p.matcher(string);
        if(m.find()) {
          programSequence.addAll(targetingStrategyMap.get(p).handle(m));
        }
      }
      //programSequence.add(new CircularizeProgram(this, new  CircularizationRaisePeriApsis(this)));
//      Matcher altMatch = apogeePattern.matcher(string);
//      Matcher planeMatch = planeChangePattern.matcher(string);
//
//      if (altMatch.find()) {
//        String resultCommand = altMatch.group().substring(1);
//        targetAltitude = Float.parseFloat(resultCommand);
//        dest = altMatch.group().substring(0, 1);
//      }
//      if(planeMatch.find()) {
//        String resultCommand = planeMatch.group().substring(1);
//        float targetPlaneAngle = Float.parseFloat(resultCommand);
//        log.debug("targetPlaneAnge="+targetPlaneAngle);
//        CoordSys cs = (CoordSys) this.getReferenceObject().getCoordSys().clone();
//        cs.xRotate(targetPlaneAngle);
//        PlanetPlaneChangePlaneAlignProgram planeChangeAlignProgram = new PlanetPlaneChangePlaneAlignProgram(this, cs.zAxis().getVectorForm());
//        planeChangeAlignProgram.start();
//        return;
//      }
    }
//    if (dest.equals("M")) {
//      moonShot();
//    } else {
      executeAutoLaunchSequencePrg();
    //}
  }

  private void runProgramSequence() {
    craft.setAutoPilotInhibit(false);
    Iterator<AFCSStrategy> it = programSequence.iterator();
    AFCSStrategy as = null;
    while (it.hasNext()) {
      AFCSStrategy ars = it.next();
      if (as != null) {
        ars.setJoinToThread(as);
      }
      as = ars;
      ars.start();
    }
    launchTime = new Date().getTime();
    //programSequence=null;
  }


  public synchronized void setFlashAnnun(boolean flashAnnun) {
    //System.out.println("setFlashAnnun "+flashAnnun+" by "+Thread.currentThread().getName());
    this.flashAnnun = flashAnnun;
    if (!flashAnnun)
      annunColor = Color.RED;
  }

  public boolean isFlashAnnun() {
    return flashAnnun;
  }

  public void setProgramSequence(List<AFCSStrategy> programSequence) {
    this.programSequence = programSequence;
  }

  public List<AFCSStrategy> getProgramSequence() {
    return programSequence;
  }

  public Map<OrbitElementKeys, Object> getOrbitElements() {
    return rocketOrbitElements;
  }

  public void setOrbitElements(Map<OrbitElementKeys, Object> orbitElements) {
    this.rocketOrbitElements = orbitElements;
  }


  /**
   * @param controlAdapter the controlAdapter to set
   */
  public void setControlAdapter(RocketControlAdapter controlAdapter) {
    this.controlAdapter = controlAdapter;
  }

  /**
   * @return the controlAdapter
   */
  public IControlAdapter getControlAdapter() {
    return controlAdapter;
  }


  public Map<OrbitElementKeys, Object> getRocketOrbitElements() {
    return rocketOrbitElements;
  }


  public float getTargetAltitude() {
    return targetAltitude;
  }

  public void setTargetAltitude(float altitude) {
    this.targetAltitude = altitude;
  }

  @Override
  public Map<String, Component> getNamedComponents() {
    Map<String, Component> result = new HashMap<>();
    for(ComputerButtonKeys key : this.buttonHash.keySet()) {
      result.put(key.toString()+"C", buttonHash.get(key));
    }

    for(Component c : this.getComponents()){
      if(c instanceof GlassButton) {
        GlassButton gb = (GlassButton)c;
        result.put(gb.getText()+"C", gb);
      }
    }

    result.putAll(((INamedControlContainer)pageHash.get(ProgrammingPage.pageName)).getNamedComponents());
    return result;
  }


  private class ComputerMouseListener implements MouseListener {
    /*
     *  SwingUtilities.invokeLater(new Runnable()
                        {public void run(){theTree.updateUI();}});
     */
    public void mouseClicked(MouseEvent arg0) {
      for (Component c : NavComputer.this.getComponents()) {
        if (c instanceof AbstractButton && c.getBounds().contains(arg0.getPoint())) {
          AbstractButton b = (AbstractButton) c;
          b.setSelected(!b.isSelected());
          b.doClick();
          break;
        }
      }
    }

    public void mousePressed(MouseEvent arg0) {
      //log.debug("MPr");
    }

    public void mouseReleased(MouseEvent arg0) {
      //log.debug("MRel");
    }

    public void mouseEntered(MouseEvent arg0) {
      //log.debug("MEnt");
    }

    public void mouseExited(MouseEvent arg0) {
      //log.debug("MEx");
    }
  }

  public Map<String, Component> getControlHash() {
    return controlHash;
  }
}
