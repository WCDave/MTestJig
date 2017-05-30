package main;

import Foundation.ThreadMonitorAndFactory;
import Foundation.Utils;
import VMath.VMath;
import enums.ViewTypes;
import mapUtils.MapHelper;
import mapUtils.SphereHelper;
import orbitService.IOrbitService;
import orbits.Abstract3DModelObject;
import orbits.CoordSys;
import orbits.Drawable3D;
import orbits.Facet;
import orbits.GenericSatellite;
import orbits.Object3D.impl.DefaultGroundStructure;
import orbits.Planet;
import orbits.Point3D;
import orbits.ScreenPoint;
import orbits.SkyDecorator;
import orbits.Sphere;
import orbits.StarBackground;
import orbits.World3DContainer;
import orbits.atmospherics.EarthAtmosFactor2;
import orbits.atmospherics.SunAtmosphereDraw;
import orbits.movement.MoonOrbitMovement;
import orbits.movement.Moveable;
import orbits.movement.Movement;
import org.apache.commons.math3.util.FastMath;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.spi.NamingManager;
import javax.sql.DataSource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainApp extends JFrame {

  public static Dimension APP_SIZE_DIM = new Dimension(1000, 750);

  private JMenuItem helpMenuItem;
  private JMenu helpMenu;
  private JMenuItem deleteMenuItem;
  private JSeparator jSeparator1;
  private JMenuItem pasteMenuItem;
  private JMenuItem copyMenuItem;
  private JMenuItem cutMenuItem;
  private JMenu editMenu;
  private JMenuItem exitMenuItem;
  private JMenu fileMenu;
  private JMenuBar menuBar;
  private JRadioButtonMenuItem slewView, rocketView, geoSynchView;
  private ViewTypes viewType = ViewTypes.SLEW;
  private Rocket theRocket;

  private IOrbitService orbitService;
  double llat = 28.5;
  //double llon = -80.6 + (Utils.getDecimalTimeOfDay() * 15) - 9.4;
  double llon = -80.6 + (Utils.getDecimalTimeOfDay() * 15) - 9.4;

  CockPitView cView;
  ExternalView eView;

  private JMenuItem toggleMenuItem;
  private JMenu viewMenu;
  private Logger log = Logger.getLogger(MainApp.class);

  private CountDownLatch countDownLatch;

  public String reverse(String str) {

    if ((null == str) || (str.length() <= 1)) {
      System.out.println(str);
      return str;
    }
    System.out.println("* " + str.substring(1));
    return reverse(str.substring(1)) + str.charAt(0);
  }

  public static void main(String[] args) {

    MainApp inst = new MainApp();

//		Map<String, int[]> map = new HashMap<String,int[]>();
//		for(int i=0;i<603885;i++)
//		{
//			map.put(new Integer(i).toString(), new int[3]);
//		}


    Integer.valueOf("#a5bee7".substring(1, 3), 16);
    System.out.println("--- " + inst.reverse("Hello"));
    Hashtable<String, String> env = new Hashtable<String, String>();
    env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
    //env.put("java.naming.factory.url.pkgs", "org.jboss.naming");
    env.put(Context.PROVIDER_URL, "jnp://127.0.0.1");

    NamingEnumeration<NameClassPair> enumer = null;
    NamingEnumeration<NameClassPair> enumer1 = null;

    try {
      Context context = NamingManager.getInitialContext(env);
      enumer = context.list("");
      Object jk = context.lookup("XAOracleDS");
      DataSource kk = (DataSource) context.lookup("XAOracleDS");
      Connection c = kk.getConnection();
      Object pp = c.getClientInfo();
      int jp = 8;
      while (enumer.hasMoreElements()) {
        NameClassPair o = enumer.next();
        if ("org.jnp.interfaces.NamingContext".equals(o.getClassName())) {
          enumer1 = context.list(o.getName());
        }
      }
      Object o1 = context.lookupLink("java/XAOracleDS");
      String x = context.getNameInNamespace();
      int jj = 0;
    } catch (Exception e2) {
      int gg = 9;
    }

    // 02027G34KT
    Pattern tdp = Pattern.compile(" \\d{5}(G?\\d{2})?(KT)?(MPS)? ");
    Matcher m = tdp
            .matcher("UUEE 270130Z 22007MPS CAVOK 03/M02 Q1017 NOSIG RMK 21008G10MPS 75150157 25150157");
    // Matcher m = tdp.matcher("18/07");
    boolean x = m.find();
    String s = m.group().trim();
    if (s.contains("MPS"))
      ;

//		Binding b = new Binding();
//		GroovyShell shell = new GroovyShell(b);
//		Object o = null;
//		try {
//			o = shell.evaluate(new File("resources\\test.groovy"));
//		} catch (CompilationFailedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

    try {
      Enumeration<URL> xx = inst.getClass().getClassLoader().getResources("*");
      int oo = 0;
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    inst.setTitle("RocketSim");
    Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK,
            true);

    inst.setVisible(true);
    inst.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		String fileName = "RocketErrs.txt";
//		FileOutputStream fos = null;
//		try {
//			fos = new FileOutputStream(fileName);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		PrintStream ps = new PrintStream(fos);
//		System.setErr(ps);// System.setOut(ps);
    inst.initModel();
  }

  MainApp() {
    super();
    countDownLatch = new CountDownLatch(2);
    setVisible(true);
    log.debug("Building Rocket");
    theRocket = new Rocket(16000, 300000, (float) 8 * 20000);
    theRocket.setName("mud");
    GenericSatellite s = new GenericSatellite();
    s.setMass(50000);
    s.setName("Test1");
    Craft testCraft = new Craft() {
      @Override
      public Vector<Point3D> getDataList() {
        return null;
      }

      @Override
      public void draw(AbstractView view) {
        Graphics2D g2 = view.getViewCanvas().getG2();
        Dimension d = view.getViewCanvas().getScreenCenter();
        ScreenPoint p = view.getCoordSys().getScrnCoords(sSys.getPositionVec(), d.width, d.height);
        if (p.isInView()) {
          g2.setColor(Color.red);
          double mag = Math.sqrt(VMath.mag(vectorFromView));
          int divisor = 1;
          if (mag < 1e4) {
            divisor = (int) (10000 / mag);
          }
          g2.fillOval(p.x - divisor / 2, p.y - divisor / 2, divisor, divisor);
          g2.drawLine(p.x, p.y, p.x, p.y);
        }
      }

      @Override
      public void set3DObjectForDraw(AbstractView aView) {

        CoordSys cs = aView.getCoordSys();
        vectorFromView = VMath.vecSubtract(cs.getPositionVec(), sSys.getPositionVec());
        if (aView.getDrawableList() != null) {
          aView.getDrawableList().add(this);
        }
      }

      @Override
      public double getDragFactor() {
        return 1;
      }
    };
    testCraft.setMass(theRocket.getMass() / 20);
    testCraft.setName("Test");
    theRocket.loadBay(testCraft);
    theRocket.loadBay(s);
    cView = new CockPitView(countDownLatch);
    cView.setTheRocket(theRocket);
    cView.initGUI();
    eView = new ExternalView(
            new AbstractInstrument[]{cView.getComputer()}, theRocket);

    cView.setVisible(true);
    eView.setVisible(false);

    getContentPane().add(cView);
    getContentPane().add(eView);
    cView.getViewCanvas().initCanvas();
    eView.getViewCanvas().initCanvas();
    for (AbstractInstrument i : cView.getInstruments()) {
      i.getInstCanvas().initCanvas();
    }
    Runnable doInitGui = new Runnable() {
      public void run() {
        initGUI();
      }
    };
    SwingUtilities.invokeLater(doInitGui);
  }

  private void initModel() {
    log.debug("Initializing Model");
    World3DContainer container = World3DContainer.getInstance();
    IDrawingVisitor v = new DrawingVisitor();

    Abstract3DModelObject[] anArray = new Abstract3DModelObject[5];

    BufferedImage earthImage = null;
    BufferedImage marsImage = null;
    BufferedImage moonImage = null;
    try {
      //earthImage = ImageIO.read(new File("flat_earth_by_EnforcedCrowd.jpg"));
      earthImage = ImageIO.read(new File(ConfigurationManager.getInstance().getProperty("EARTH_IMG_FILE")));
      marsImage = ImageIO.read(new File(ConfigurationManager.getInstance().getProperty("MARS_IMG_FILE")));
      //earthImage = ImageIO.read(new File("color_etopo1_ice_low.jpg"));
      moonImage = ImageIO.read(new File(ConfigurationManager.getInstance().getProperty("MOON_IMG_FILE")));

      // earthImage =
      // ImageIO.read(getClass().getResource("../earth_map.jpg"));
    } catch (Exception e) {
      e.printStackTrace();
    }

    // anArray[0]=new Planet((float)1.991e30, (float)695950e3, Color.yellow,
    // new float[]{0,0,0,0}, false);
    anArray[0] = new Planet(0, (float) 695950e3, Color.yellow, new double[]{0, 0, 0, 0}, false, null, null);
    anArray[0].setDrawFaceOutlines(true);

    anArray[2] = new Planet((float) 5.99e24, (float) 6.37e6, new Color(135,206,235), new double[]{0, 0, 4.1780746480349296325186049083789e-6, 0},true, (Planet) anArray[0], earthImage);
    anArray[2].setDrawFaceOutlines(false);
    //((Planet) anArray[2]).getCoordSys().zRotate((Utils.getDecimalTimeOfDay() * 15) - 9.4);
    ((Planet) anArray[2]).setObliquity(0f);

    anArray[4] = new Planet(0, (float) 3.3962e6, Color.red, new double[]{0, 0, 0, 0},  true, (Planet) anArray[0], marsImage);
    anArray[4].setDrawFaceOutlines(false);

    anArray[1] = new Planet((float) 7.36e22, (float) (0.27 * 6.37e6), Color.white, new double[]{0, 0, 0, 0}, false, (Planet) anArray[2], moonImage);
    anArray[1].setDrawFaceOutlines(false);

    anArray[0].setName("Sun");
    anArray[1].setName("Moon");
    anArray[2].setName("Earth");
    anArray[4].setName("Mars");

    anArray[0].getCoordSys().setPositionAsVec(new double[]{0, (float) 1.49e11, 0}); // Set Sun's position
    // in space
    anArray[2].getCoordSys().setPositionAsVec(new double[]{0, 0, 0}); // Set
    // Earth's
    // positon
    // in
    // space
    anArray[1].getCoordSys().setPositionAsVec(new double[]{(float) 3.84403e8, 0, 0}); // Set Moon's
    //anArray[0] = new SunAtmosphereDraw((Planet) anArray[0]) ;

    // position in space

    ((Planet) anArray[1]).setShadingVisitor(v);
    ((Planet) anArray[2]).setShadingVisitor(v);
    ((Planet) anArray[4]).setShadingVisitor(v);
    ((Planet) anArray[0]).setAtmosDraw(new SunAtmosphereDraw((Planet) anArray[0]));
   //((Planet) anArray[2]).setAtmosDraw(new EarthAtmosphereDraw((Planet) anArray[2]));
    ((Planet) anArray[2]).setAtmosFactor(new EarthAtmosFactor2((Planet) anArray[2]));

    anArray[1].setDetailingFactor(.32 * (5e5));
    anArray[2].setDetailingFactor(5e5);
    anArray[4].setDetailingFactor(5e5);

    double[] tmpvec1 = new double[4];
    tmpvec1 = MapHelper.convertGeographicalDegreesToCartesianCoords(((Planet) anArray[2]), 40, llon);
//    tmpvec1[0] = (float) Math.cos(Math.toRadians(llon))
//            * (float) Math.cos(Math.toRadians(llat))
//            * ((Planet) anArray[2]).getRadius();
//    tmpvec1[1] = (float) Math.sin(Math.toRadians(llon))
//            * (float) Math.cos(Math.toRadians(llat))
//            * ((Planet) anArray[2]).getRadius();
//    tmpvec1[2] = (float) Math.sin(Math.toRadians(llat))
//            * ((Planet) anArray[2]).getRadius();
    tmpvec1 = VMath.vecAdd(anArray[2].getCoordSys().getPositionVec(),tmpvec1);
    theRocket.setPositionVec(tmpvec1);
    World3DContainer.getInstance().setLaunchPoint(VMath.vecMultByScalar(tmpvec1, 1));
    aimRocketUp();
    anArray[3] = theRocket;

    Drawable3D d = new SkyDecorator(null, anArray[2]);
    // createXMLForPlanets(anArray);
    try {
      System.out.println("loading surf data and star data");
      //loadSurfaceDataFor((Planet) anArray[2]);
      World3DContainer.getInstance().setBackground(new StarBackground(d, anArray[2]));
    } catch (Exception e1) {
      System.err.println("Problem occurred when attempting to load star_catalog.txt :" + e1.getMessage());
    }

    container.addItemsAsArray(anArray);
    container.setLiteEmitter(anArray[0]);

//    for (Facet f : ((Sphere) anArray[1]).getFaceList()) {
//      DefaultFaceDetailer.saveSerializedDetails(f);
//    }
//
//    for (Facet f : ((Sphere) anArray[2]).getFaceList()) {
//      DefaultFaceDetailer.saveSerializedDetails(f);
//    }

    cView.setObjectArray(anArray);
    // CoordSys aSys = new CoordSys(0,0,(float)9.15e7);
    // CoordSys bSys = (CoordSys)aSys.clone();
    // aSys.xRotate(180);
    // aSys.zRotate(180);
    // eView.setViewingCoordSys(aSys);

    // theRocket.getNavObject().yRotate(-llat);

    // theRocket.getNavObject().xRotate(90-llon);
    // theRocket.getNavObject().zRotate(270-llon);
    // theRocket.getNavObject().zRotate(180);
    cView.setTheRocket(theRocket);
    cView.getComputer().setReferenceObject(anArray[2]);

    adjustEViewPosition();
    List<Moveable> aList = new ArrayList<Moveable>(3);
    aList.add((Moveable) anArray[1]);
    aList.add((Moveable) anArray[2]);
    ((Moveable) anArray[1]).setMovements(new Movement[]{new MoonOrbitMovement((Planet) anArray[1])});

    //Movement lpMovement = new LaunchPointMovement((Planet)anArray[2]);
    //lpMovement.execute(-3000);
    double[] structureLocation = SphereHelper.movePoint(World3DContainer.getInstance().getLaunchPoint(), 90, 200, (Planet)anArray[2]);

    DefaultGroundStructure struct = new DefaultGroundStructure(25, 150, 350, structureLocation, new Color(108,123,139), anArray[2]);
    //struct.setPosition(structureLocation);
    struct.getBuilder().build();

    LaunchPointCraft lpCraft = new LaunchPointCraft(SphereHelper.movePoint(structureLocation, 200, 800, (Sphere) World3DContainer.getInstance().getItem("Earth")), eView, anArray[2]);
    lpCraft.setName("LP");
   // Movement groundStructureMovement = new GroundStructureMovement(new AbstractGroundStructure[]{struct}, (Planet)anArray[2]);
    //groundStructureMovement.execute(500);

    //((Moveable) anArray[2]).setMovements(new Movement[]{ new LaunchPointMovement((Planet)anArray[2])});

    container.addItemsAsArray(new Abstract3DModelObject[]{struct, lpCraft});

    List<Craft> craftList = new ArrayList<Craft>();
    craftList.add(theRocket);
    craftList.add(struct);
    craftList.add(lpCraft);
    container.getCraftList().addAll(craftList);

    ThreadMonitorAndFactory threadMonitorAndFactory = ThreadMonitorAndFactory.getInstance();
    Thread physicsThread = threadMonitorAndFactory.newThread(new PhysicsRunnable(anArray, aList));
    physicsThread.setName("Physics");
    physicsThread.start();

    Thread graphicsThread = threadMonitorAndFactory.newThread(new GraphicsRunnable(new AbstractView[]{cView, eView}));
    graphicsThread.setName("Graphics");
    graphicsThread.setPriority(Thread.MAX_PRIORITY);
    graphicsThread.start();

    // Thread trackingThread = new Thread(new UtilityRunnable(theRocket,
    // aList, cView.getComputer()),"Data");
    Thread trackingThread = threadMonitorAndFactory.newThread(new UtilityRunnable(theRocket, null, cView.getComputer(), countDownLatch));
    trackingThread.setName("Data");
    trackingThread.setPriority(Thread.MIN_PRIORITY);
    trackingThread.start();
//    new TestThread(cView.getComputer()).start();
  }

  private void adjustEViewPosition() {
    //CoordSys aSys = (CoordSys) theRocket.getCoordSys().clone();
    double aVec[] = theRocket.getCoordSys().getPositionVec();
    CoordSys eViewSys = new CoordSys();
    eViewSys.setZAxis(VMath.normalize(aVec));

    aVec = VMath.vecMultByScalar(aVec, 2.5);
    eViewSys.setPositionAsVec(aVec);

    double[] zAxis = VMath.normalize(eViewSys.zAxis().getVectorForm());
    double[] yAxis = VMath.normalize(theRocket.getCoordSys().yAxis().getVectorForm());
    double[] xAxis = VMath.normalize(VMath.crossprd(yAxis, zAxis));
    yAxis = VMath.crossprd(zAxis, xAxis);
    eViewSys.setXAxis(xAxis);
    eViewSys.setYAxis(yAxis);
    eViewSys.setZAxis(zAxis);

    eViewSys.xRotate(180);
    eViewSys.zRotate(-90);
    eView.setViewingCoordSys(eViewSys);
  }

  private void aimRocketUp() {
    CoordSys cs = theRocket.getCoordSys();
    double[] rBar = VMath.normalize(cs.getPositionVec());
    double[] vBar = VMath.normalize(new double[]{
            -FastMath.sin(FastMath.toRadians((double) llon)),
            FastMath.cos(FastMath.toRadians((double) llon)), 0, 0});
    double[] xAxis = VMath.normalize(VMath.crossprd(vBar, rBar));
     //System.out.println("((( "+VMath.mag(VMath.crossprd(rBar, cs.getPositionVec())));
    cs.setZAxis(rBar);
    cs.setYAxis(vBar);
    cs.setXAxis(xAxis);
  }

  private void initGUI() {
    try {
      this.setPreferredSize(APP_SIZE_DIM);
      this.setSize(APP_SIZE_DIM.width, APP_SIZE_DIM.height);
      {
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.setOpaque(false);
        {
          fileMenu = new JMenu();
          menuBar.add(fileMenu);
          fileMenu.setText("File");

          {
            exitMenuItem = new JMenuItem();
            fileMenu.add(exitMenuItem);
            exitMenuItem.setText("Exit");
            exitMenuItem.setAccelerator(KeyStroke
                    .getKeyStroke("alt X"));
            exitMenuItem.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                System.exit(0);
              }
            });
          }
        }
        {
          editMenu = new JMenu();
          menuBar.add(editMenu);
          editMenu.setText("Edit");
          {
            cutMenuItem = new JMenuItem();
            editMenu.add(cutMenuItem);
            cutMenuItem.setText("Cut");
            cutMenuItem.setBounds(0, 0, 7, 7);
          }
          {
            copyMenuItem = new JMenuItem();
            editMenu.add(copyMenuItem);
            copyMenuItem.setText("Copy");
          }
          {
            pasteMenuItem = new JMenuItem();
            editMenu.add(pasteMenuItem);
            pasteMenuItem.setText("Paste");
          }
          {
            jSeparator1 = new JSeparator();
            editMenu.add(jSeparator1);
          }
          {
            deleteMenuItem = new JMenuItem();
            editMenu.add(deleteMenuItem);
            deleteMenuItem.setText("Delete");
          }
        }
        {
          helpMenu = new JMenu();
          menuBar.add(helpMenu);
          helpMenu.setText("Help");
          {
            helpMenuItem = new JMenuItem();
            helpMenu.add(helpMenuItem);
            helpMenuItem.setText("Help");
            helpMenuItem.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                displayHelp();
              }
            });

          }
        }
        {
          viewMenu = new JMenu();

          menuBar.add(viewMenu);
          viewMenu.setText("View");
          viewMenu.setPreferredSize(new Dimension(35, 21));
          {
            toggleMenuItem = new JMenuItem();
            viewMenu.add(toggleMenuItem);
            toggleMenuItem.setText("View Toggle");
            toggleMenuItem
                    .setMnemonic(KeyEvent.VK_V);
            toggleMenuItem.setAccelerator(KeyStroke
                    .getKeyStroke("alt V"));
            toggleMenuItem.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                cView.setVisible(!cView.isVisible());
                eView.setVisible(!eView.isVisible());
                World3DContainer.getInstance().setVisibleView(cView.isVisible() ? cView : eView);
              }
            });
            World3DContainer.getInstance().setVisibleView(cView);
            viewMenu.add(new JSeparator());
            slewView = new JRadioButtonMenuItem(ViewTypes.SLEW.getDescription());
            rocketView = new JRadioButtonMenuItem(ViewTypes.ROCKET.getDescription());
            rocketView.setAccelerator(KeyStroke.getKeyStroke("alt R"));
            geoSynchView = new JRadioButtonMenuItem(ViewTypes.GEOSYNCH.getDescription());
            geoSynchView.setAccelerator(KeyStroke.getKeyStroke("alt G"));
            ButtonGroup bGroup = new ButtonGroup();
            bGroup.add(slewView);
            bGroup.add(rocketView);
            bGroup.add(geoSynchView);
            slewView.setSelected(true);
            viewMenu.add(slewView);
            viewMenu.add(rocketView);
            viewMenu.add(geoSynchView);
            viewMenu.add(new JSeparator());
            ActionListener act = new ViewChangeActionListener();
            slewView.addActionListener(act);
            rocketView.addActionListener(act);
            geoSynchView.addActionListener(act);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void displayHelp() {
    Process p = null;
    try {
      Runtime rt = Runtime.getRuntime();
      String fileName = ConfigurationManager.getInstance().getProperty(
              "HELP_FILE");
      String cmdArg[] = {"cmd.exe", "/C", fileName};
      p = rt.exec(cmdArg);
      BufferedReader br = new BufferedReader(new InputStreamReader(p
              .getInputStream()));
      while (true) {
        String output = br.readLine();
        if (output == null)
          break;
        // System.out.println(output);
      }
    } catch (IOException e2) {
      System.out
              .println("Problem occurred when attempting to load RocketDocument.doc :"
                      + e2.getMessage());
    }
  }

//	private void loadSurfaceDataFor(Planet aPlanet) throws IOException {
//		String fileName = ConfigurationManager.getInstance().getProperty(
//				"EARTH_COAST_FILE");
//		File inputFile = new File(fileName);
//		HashMap<String, Facet> faceMap = hashFaceList(aPlanet.getFaceList());
//		BufferedReader br = null;
//		try {
//			br = new BufferedReader(new FileReader(inputFile));
//		} catch (FileNotFoundException e) {
//			System.err
//					.println("No surface data found for " + aPlanet.getName());
//		}
//		Facet prevFace = null;
//		while (true) {
//			float lat = -999;
//			float lon = -999;
//			String input = br.readLine();
//			if (input == null)
//				break;
//			StringTokenizer st = new StringTokenizer(input, new String(
//					new char[] { '\u0009' }));
//			String s1 = st.nextToken();
//			if (s1.contains("#")) {
//				if (prevFace != null)
//					prevFace.getSurfaceFeatureData().add(null);
//			} else {
//				lon = (Float.parseFloat(s1) + 360) % 360;
//				lat = (270 - Float.parseFloat(st.nextToken())) % 180;
//				int iLat = ((int) (lat / orbits.Sphere.DPHI) * orbits.Sphere.DPHI);
//				int iLon;
//
//				if (lon > 0)
//					iLon = ((int) (lon / orbits.Sphere.DTHETA) * orbits.Sphere.DTHETA);
//				else
//					iLon = ((int) (lon / orbits.Sphere.DTHETA) * orbits.Sphere.DTHETA);
//
//				String key = iLat + "|" + iLon;
//				// System.out.println(key+" "+lat+" "+lon);
//				Facet aFace = faceMap.get(key);
//				List<double[]> aList = aFace.getSurfaceFeatureData();
//				aList.add(Sphere.getVectorForLatLon(lat, lon, aPlanet
//						.getCoordSys(), Sphere.obRad(lat, aPlanet.getRadius(),
//						aPlanet.getOblateness())));
//				prevFace = aFace;
//			}
//		}
//		/*
//		 * Iterator it = aPlanet.getFaceList().iterator(); while(it.hasNext()){
//		 * Facet face= (Facet)it.next();
//		 * System.err.println(face.name()+" "+face.
//		 * getSurfaceFeatureData().size()+" data points"); }
//		 */
//		faceMap = null;
//	}

  private HashMap<String, Facet> hashFaceList(List<? extends Facet> faceList) {
    HashMap<String, Facet> result = new HashMap<String, Facet>();

    for (Facet face : faceList) {
      String name = face.getName();
      result.put(name, face);
    }
    return result;
  }

  private class ViewChangeActionListener implements ActionListener {

    public void actionPerformed(ActionEvent arg0) {
      viewType.getViewStrategy().reset();
      if (arg0.getSource().equals(slewView)) {
        viewType = ViewTypes.SLEW;
      } else if (arg0.getSource().equals(rocketView)) {
        viewType = ViewTypes.ROCKET;
        eView.setViewLock(true);
      } else {
        viewType = ViewTypes.GEOSYNCH;
        eView.setViewLock(true);
      }
      eView.setViewType(viewType);
     // eView.getViewType().getViewStrategy().applyToView(eView);
      MainApp.this.adjustEViewPosition();
      // eView.setViewingCoordSys(aSys);
    }
  }

  /**
   * @param <T>
   * @return
   */
  public <T> T getit() {
    return (T) new Object();

  }

	/*
   * private void createXMLForPlanets(Abstract3DModelObject[] anArray) {
	 * Document d = new Document(new Element("root")); XMLOutputter x = new
	 * XMLOutputter(Format.getPrettyFormat()); File f = new File("xmldata.xml");
	 * BufferedWriter bw=null; try { bw = new BufferedWriter(new FileWriter(f));
	 * 
	 * } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } for(int i=0;i<anArray.length;i++){ Planet p =
	 * (Planet)anArray[i]; Element e = new Element("PlanetName");
	 * e.setText(p.name());
	 * 
	 * for(int j=0;j<Planet.class.getMethods().length;j++){ Method m =
	 * Planet.class.getMethods()[j]; if(m.getName().contains("get")) break;
	 * Element data = new Element(m.getName()); Method m String dataString =
	 * m.invoke() data.setText() } d.getRootElement().addContent(e); } try {
	 * x.output(d,bw); } catch (IOException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); }
	 * 
	 * 
	 * }
	 */
}
