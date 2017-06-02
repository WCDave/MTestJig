
package main;

import VMath.VMath;
import enums.AxisType;
import enums.DirectionTypes;
import gui.INamedControlContainer;
import orbits.Abstract3DModelObject;
import orbits.CoordSys;
import orbits.NavComputer;
import rocketComponents.RocketControlAdapter;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalSliderUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

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
public class CockPitView extends AbstractView implements Instrumented<AbstractInstrument>, KeyListener, MouseListener, INamedControlContainer {

  private RenderingCanvas pilotViewCanvas;
  private JPanel thrustPanel;
  private JPanel navPanel;

  private ButtonGroup zoomBg;
  private JRadioButton downViewRb;
  private JRadioButton upViewRb;
  private JRadioButton rightViewRb;
  private JRadioButton leftViewRb;
  private JRadioButton aftViewRb;
  private JRadioButton forwardViewRb;
  private ButtonGroup viewDirBg;
  private JCheckBox trackingSelectcb;
  private JCheckBox hudSelectcb;
  private JCheckBox rcsModecb;
  private JSlider rateAdjustSlider;
  private JButton yLeftButton;
  private JButton yRightButton;

  private JComboBox planetSelectcb;
  private AbstractInstrument ADIPanel;
  private AbstractInstrument gMeter;
  private AbstractInstrument fuelGauge;
  private JSlider thrustSlider;
  private boolean hudSelect;
  private boolean rcsLine;
  private NavComputer computer;
  private Rocket theRocket;
  private Abstract3DModelObject referencedPlanet;
  private Abstract3DModelObject[] objectArray;
  private float ratePercent = 1;
  private RocketControlAdapter controlAdapter;
  private IDrawingVisitor drawingVisitor;

  private Map<Integer, Component> kbdCommandMap = new HashMap<Integer, Component>();
  private Set<Component> allControls = new HashSet<>();
  private static AbstractInstrument[] instruments;

  private JButton rRightButton;
  private JButton rLeftButton;
  private JButton pDownButton;
  private JButton pUpButton;
  private JButton fwdThrustButton;
  private JButton aftThrustButton;
  private JButton resetButton;
  private JButton zoomOutButton;
  private JButton zoomInButton;
  private JButton nullRates;

  private CountDownLatch countDownLatch;

  private static Dimension COCKPIT_VIEW_SIZE_DIM = new Dimension(MainApp.APP_SIZE_DIM.width-25, MainApp.APP_SIZE_DIM.height-320);


  private static final float rateInc = 5f;

  public CockPitView(CountDownLatch countDownLatch) {
    super();
    drawingVisitor = new DrawingVisitor();
    this.countDownLatch = countDownLatch;
    //initGUI();
  }

  @Override
  public Component add(Component comp) {
    if(comp instanceof JPanel) {
      allControls.add((Component) comp);
    }

    return super.add(comp);
  }


  public void initGUI() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();
    double height = screenSize.getHeight();
    try {
      planetSelectcb = new JComboBox();
      setLayout(null);
      {
        pilotViewCanvas = new RenderingCanvas(COCKPIT_VIEW_SIZE_DIM, this);
        add(pilotViewCanvas);
        pilotViewCanvas.setBounds(7, 0, COCKPIT_VIEW_SIZE_DIM.width, COCKPIT_VIEW_SIZE_DIM.height);
        pilotViewCanvas.setPreferredSize(COCKPIT_VIEW_SIZE_DIM);
        this.setPreferredSize(MainApp.APP_SIZE_DIM);
        this.setVisible(true);
        pilotViewCanvas.setVisible(true);
      }
      {
        {
          viewDirBg = new ButtonGroup();
        }
        {
          zoomBg = new ButtonGroup();
        }
        thrustPanel = new JPanel();
        thrustPanel.setLayout(null);
        add(thrustPanel);
        thrustPanel.setBounds(7, 430, 175, 270);
        thrustPanel.setBackground(new Color(255, 0, 0));
        thrustPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        {
          thrustSlider = new JSlider();
          thrustSlider.setUI(new MetalSliderUI());
          //Hashtable aDict = new Hashtable();
          //for(int i=0;i<11;i++){
          //	aDict.put(new Integer(i),new Label(Integer.toString(i)));
          //}
          thrustSlider.setPaintLabels(true);
          thrustSlider.setLabelTable(thrustSlider.createStandardLabels(10));
          //thrustSlider.setLabelTable((Dictionary)aDict);
          //Dictionary x = (Dictionary)thrustSlider.getLabelTable();
          thrustPanel.add(thrustSlider);
          thrustSlider.setOrientation(1);
          thrustSlider.setBounds(14, 14, 77, 210);
          thrustSlider.setBorder(BorderFactory.createTitledBorder(""));
          thrustSlider.setValue(0);
          thrustSlider.setAutoscrolls(true);
          thrustSlider.setMaximum(110);
          thrustSlider.setPaintTicks(true);
          thrustSlider.setToolTipText("% Thrust");
//                    thrustSlider.addChangeListener(new ChangeListener() {
//                        public void stateChanged(ChangeEvent evt) {
//                            theRocket.setThrottleSetting(((JSlider)evt.getSource()).getValue());
//                        }
//                    });
          controlAdapter = new RocketControlAdapter(theRocket, thrustSlider, planetSelectcb, kbdCommandMap);
        }
      }
      {
        navPanel = new JPanel();
        add(navPanel);
        navPanel.setBounds(189, 430, 795, 270);
        navPanel.setBackground(new Color(64, 128, 128));
        navPanel.setLayout(null);
        navPanel.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
        {
          fwdThrustButton = new JButton();
          fwdThrustButton.setVisible(false);
          navPanel.add(fwdThrustButton);
          fwdThrustButton.setText("F");
          fwdThrustButton.setBounds(84, 21, 21, 21);
          fwdThrustButton.setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
          fwdThrustButton.setToolTipText("Thrust Fwd");
          fwdThrustButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
              if (rcsLine) {
                theRocket.setRcsThrustAxis(AxisType.Z);
                if (fwdThrustButton.getModel().isPressed()) {
                  theRocket.setRcsThrustValue(Rocket.RCS_THRUST);
                } else {
                  theRocket.setRcsThrustValue(0);
                }
              }
            }
          });

          aftThrustButton = new JButton();
          aftThrustButton.setVisible(false);
          navPanel.add(aftThrustButton);
          aftThrustButton.setText("A");
          aftThrustButton.setBounds(84, 117, 21, 21);
          aftThrustButton.setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
          aftThrustButton.setToolTipText("Thrust Aft");
          aftThrustButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
              if (rcsLine) {
                theRocket.setRcsThrustAxis(AxisType.Z);
                if (aftThrustButton.getModel().isPressed()) {
                  theRocket.setRcsThrustValue(-Rocket.RCS_THRUST);
                } else {
                  theRocket.setRcsThrustValue(0);
                }
              }
            }
          });

          pUpButton = new JButton();
          navPanel.add(pUpButton);
          pUpButton.setText("V");
          pUpButton.setBounds(126, 103, 21, 35);
          pUpButton.setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
          pUpButton.setToolTipText("Pitch Up");
          pUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              if (!rcsLine) {
                theRocket.adjustPitchRate((float) rateInc * ratePercent);
              }
            }
          });

          pUpButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
              if (rcsLine) {
                theRocket.setRcsThrustAxis(AxisType.Y);
                if (pUpButton.getModel().isPressed()) {
                  theRocket.setRcsThrustValue(Rocket.RCS_THRUST);
                } else {
                  theRocket.setRcsThrustValue(0);
                }
              }
            }
          });
        }
        {
          computer = new NavComputer(this, countDownLatch);
          navPanel.add(computer);
          computer.setLayout(null);
          computer.setBounds(280, 2, 515, 170);
          computer.setSize(515, 170);
          computer.setVisible(true);
        }
        {
          pDownButton = new JButton();
          navPanel.add(pDownButton);
          pDownButton.setText("^");
          pDownButton.setBounds(126, 21, 21, 35);
          pDownButton.setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
          pDownButton.setToolTipText("Pitch Down");
          pDownButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              if (!rcsLine) {
                theRocket.adjustPitchRate((float) -rateInc * ratePercent);
              }
            }
          });
          pDownButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
              if (rcsLine) {
                theRocket.setRcsThrustAxis(AxisType.Y);
                if (pDownButton.getModel().isPressed()) {
                  theRocket.setRcsThrustValue(-Rocket.RCS_THRUST);
                } else {
                  theRocket.setRcsThrustValue(0);
                }
              }
            }
          });
        }
        {
          rLeftButton = new JButton();
          navPanel.add(rLeftButton);
          rLeftButton.setText("<");
          rLeftButton.setBounds(84, 70, 35, 21);
          rLeftButton.setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
          rLeftButton.setToolTipText("Roll Left");
          rLeftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              if (!rcsLine) {
                theRocket.adjustRollRate((float) rateInc * ratePercent);
              }
            }
          });

          rLeftButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
              if (rcsLine) {
                theRocket.setRcsThrustAxis(AxisType.X);
                if (rLeftButton.getModel().isPressed()) {
                  theRocket.setRcsThrustValue(-Rocket.RCS_THRUST);
                } else {
                  theRocket.setRcsThrustValue(0);
                }
              }
            }
          });
        }
        {
          rRightButton = new JButton();
          navPanel.add(rRightButton);
          rRightButton.setText(">");
          rRightButton.setBounds(154, 70, 35, 21);
          rRightButton.setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
          rRightButton.setToolTipText("Roll Right");
          rRightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              if (!rcsLine) {
                theRocket.adjustRollRate((float) -rateInc * ratePercent);
              }
            }
          });
          rRightButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
              if (rcsLine) {
                theRocket.setRcsThrustAxis(AxisType.X);
                if (rRightButton.getModel().isPressed()) {
                  theRocket.setRcsThrustValue(Rocket.RCS_THRUST);
                } else {
                  theRocket.setRcsThrustValue(0);
                }
              }
            }
          });
        }
        {
          yRightButton = new JButton();
          navPanel.add(yRightButton);
          yRightButton.setText("--");
          yRightButton.setBounds(196, 168, 28, 14);
          yRightButton.setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
          yRightButton.setToolTipText("Yaw Right");
          yRightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              theRocket.adjustYawRate((float) rateInc * ratePercent);
            }
          });
        }
        {
          yLeftButton = new JButton();
          navPanel.add(yLeftButton);
          yLeftButton.setText("--");
          yLeftButton.setBounds(49, 168, 28, 14);
          yLeftButton.setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
          yLeftButton.setToolTipText("Yaw Left");
          yLeftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              theRocket.adjustYawRate((float) -rateInc * ratePercent);
            }
          });
        }

        {
          resetButton = new JButton();
          navPanel.add(resetButton);
          resetButton.setText(".");
          resetButton.setFont(new Font("Microsoft Sans Serif", 0, 9));
          resetButton.setBounds(11, 136, 42, 14);
          zoomBg.add(resetButton);
          resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              theRocket.getCoordSys().setD(2400);
            }
          });
        }
        {
          downViewRb = new JRadioButton();
          navPanel.add(downViewRb);
          downViewRb.setText("Down");
          downViewRb.setFont(new Font("Microsoft Sans Serif", 0, 9));
          downViewRb.setBounds(7, 82, 63, 14);
          viewDirBg.add(downViewRb);
          downViewRb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              if (downViewRb.isSelected())
                theRocket.getCoordSys().setViewDirection(
                        DirectionTypes.DOWN);
            }
          });
        }
        {
          leftViewRb = new JRadioButton();
          navPanel.add(leftViewRb);
          leftViewRb.setText("Left");
          leftViewRb.setBounds(7, 37, 63, 14);
          viewDirBg.add(leftViewRb);
          leftViewRb.setFont(new Font("Microsoft Sans Serif", 0, 9));
          leftViewRb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              if (leftViewRb.isSelected())
                theRocket.getCoordSys().setViewDirection(DirectionTypes.LEFT);
            }
          });
        }
        {
          rightViewRb = new JRadioButton();
          navPanel.add(rightViewRb);
          rightViewRb.setText("Right");
          rightViewRb.setBounds(7, 52, 63, 14);
          viewDirBg.add(rightViewRb);
          rightViewRb.setFont(new Font("Microsoft Sans Serif", 0, 9));
          rightViewRb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              if (rightViewRb.isSelected())
                theRocket.getCoordSys().setViewDirection(DirectionTypes.RIGHT);
            }
          });
        }
        {
          forwardViewRb = new JRadioButton();
          navPanel.add(forwardViewRb);
          forwardViewRb.setText("Forward");
          forwardViewRb.setBounds(7, 7, 63, 14);
          viewDirBg.add(forwardViewRb);
          forwardViewRb.setSelected(true);
          forwardViewRb.setFont(new Font("Microsoft Sans Serif", 0, 9));
          forwardViewRb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              if (forwardViewRb.isSelected())
                theRocket.getCoordSys().setViewDirection(DirectionTypes.FORWARD);
            }
          });
        }
        {
          aftViewRb = new JRadioButton();
          navPanel.add(aftViewRb);
          aftViewRb.setText("Aft");
          aftViewRb.setBounds(7, 22, 63, 14);
          viewDirBg.add(aftViewRb);
          aftViewRb.setFont(new Font("Microsoft Sans Serif", 0, 9));
          aftViewRb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              if (aftViewRb.isSelected())
                theRocket.getCoordSys().setViewDirection(DirectionTypes.AFT);
            }
          });
        }
        {
          rateAdjustSlider = new JSlider();
          rateAdjustSlider.setBounds(217, 35, 21, 84);
          rateAdjustSlider.setOrientation(1);
          rateAdjustSlider.setToolTipText("Fine rate adjustment");
          rateAdjustSlider.setValue(200);
          navPanel.add(rateAdjustSlider);
          rateAdjustSlider.setMaximum(200);
          rateAdjustSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
              ratePercent = (float) ((JSlider) evt.getSource()).getValue() / 200;
            }
          });
        }

        {
          nullRates = new JButton();
          navPanel.add(nullRates);
          nullRates.setText("00");
          nullRates.setBounds(129, 74, 14, 14);
          nullRates.setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
          nullRates.setToolTipText("Null Rates");
          nullRates.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              theRocket.nullRates();
            }
          });
        }

        {
          ComboBoxModel planetSelectcbModel = new DefaultComboBoxModel(new String[]{
                  "Sun", "Moon", "Earth"});

          navPanel.add(planetSelectcb);
          planetSelectcb.setModel(planetSelectcbModel);
          planetSelectcb.setBounds(140, 206, 84, 21);
          planetSelectcb.setSelectedIndex(2);
          planetSelectcb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              JComboBox aCb = (JComboBox) evt.getSource();
              String aString = (String) aCb.getSelectedItem();
              int i = 0;
              for (i = 0; i < objectArray.length; i++)
                if (objectArray[i].getName().equals(aString))
                  break;
              CockPitView.this.computer.setReferenceObject(objectArray[i]);
            }
          });
        }

        {
          hudSelectcb = new JCheckBox();
          navPanel.add(hudSelectcb);
          hudSelectcb.setText("HUD");
          hudSelectcb.setBounds(14, 217, 49, 14);
          hudSelectcb.setFont(new Font("Microsoft Sans Serif", 0, 9));
          hudSelectcb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
              hudSelect = evt.getStateChange() == ItemEvent.SELECTED;
            }
          });
        }
        {
          trackingSelectcb = new JCheckBox();
          navPanel.add(trackingSelectcb);
          trackingSelectcb.setText("Track");
          trackingSelectcb.setFont(new Font("Microsoft Sans Serif", 0, 9));
          trackingSelectcb.setBounds(14, 203, 49, 14);
          trackingSelectcb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
              theRocket.setTracking(evt.getStateChange() == ItemEvent.SELECTED);
            }
          });
        }
        {
          upViewRb = new JRadioButton();
          navPanel.add(upViewRb);
          upViewRb.setText("Up");
          upViewRb.setFont(new Font("Microsoft Sans Serif", 0, 9));
          upViewRb.setBounds(7, 67, 63, 14);
          viewDirBg.add(upViewRb);
          upViewRb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              if (upViewRb.isSelected())
                theRocket.getCoordSys().setViewDirection(
                        DirectionTypes.UP);
            }
          });
        }
        {
          zoomInButton = new JButton();
          navPanel.add(zoomInButton);
          zoomInButton.setText("+");
          zoomInButton.setBounds(11, 122, 42, 14);
          zoomInButton.setFont(new Font("Microsoft Sans Serif", 0, 9));
          zoomBg.add(zoomInButton);
          zoomInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              theRocket.getCoordSys().setD(theRocket.getCoordSys().getD() * 1.1);
            }
          });
        }
        {
          zoomOutButton = new JButton();
          navPanel.add(zoomOutButton);
          zoomOutButton.setText("-");
          zoomOutButton.setFont(new Font("Microsoft Sans Serif", 0, 9));
          zoomOutButton.setBounds(11, 108, 42, 14);
          zoomBg.add(zoomOutButton);
          zoomOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              theRocket.getCoordSys().setD(theRocket.getCoordSys().getD() / 1.1);
            }
          });
        }
        {
          gMeter = new GMeter(this);
          navPanel.add(gMeter);
          gMeter.setLayout(null);
          gMeter.setBounds(382, 171, 80, 80);
          gMeter.setSize(88, 88);
          gMeter.setVisible(true);
          //gMeter.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
        {
          ADIPanel = new ADIInstrument(this);
          navPanel.add(ADIPanel);
          ADIPanel.setLayout(null);
          ADIPanel.setBounds(280, 171, 80, 80);
          ADIPanel.setSize(88, 88);
          ADIPanel.setVisible(true);
          //ADIPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
          //{
          //   ADICanvas = new RenderingCanvas(new Dimension(80,80));
          //   ADIPanel.add(ADICanvas);
          //   ADICanvas.setBounds(0, 0, 80, 80);
          //}
        }
        {
          fuelGauge = new FuelGauge(this);
          navPanel.add(fuelGauge);
          fuelGauge.setLayout(null);
          fuelGauge.setBounds(484, 171, 80, 80);
          fuelGauge.setSize(88, 88);
          fuelGauge.setVisible(true);
        }

        {
          rcsModecb = new JCheckBox();
          navPanel.add(rcsModecb);
          rcsModecb.setText("RCS-L");
          rcsModecb.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
          rcsModecb.setBounds(210, 14, 60, 15);
          rcsModecb.setFont(new Font("Microsoft Sans Serif", 0, 9));
          rcsModecb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
              rcsLine = evt.getStateChange() == ItemEvent.SELECTED;
              fwdThrustButton.setVisible(rcsLine);
              aftThrustButton.setVisible(rcsLine);
              navPanel.requestFocus();
              if (rcsLine) {
                kbdCommandMap.put(new Integer(KeyEvent.VK_NUMPAD1), fwdThrustButton);
              } else {
                kbdCommandMap.put(new Integer(KeyEvent.VK_NUMPAD1), yLeftButton);
              }
            }
          });
        }

      }
      //this.setBounds(25, 25, 865, 572);
      this.setSize(MainApp.APP_SIZE_DIM.width-15, MainApp.APP_SIZE_DIM.height-50);
      this.setPreferredSize(new Dimension(MainApp.APP_SIZE_DIM.width-15, MainApp.APP_SIZE_DIM.height-50));
      this.setFocusable(false);
      this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.validate();
    navPanel.setFocusable(true);
    navPanel.validate();
    thrustPanel.setFocusable(true);
    thrustPanel.validate();
    navPanel.addKeyListener(this);
    navPanel.addMouseListener(this);

    kbdCommandMap.put(new Integer(KeyEvent.VK_NUMPAD6), rRightButton);
    kbdCommandMap.put(new Integer(KeyEvent.VK_NUMPAD4), rLeftButton);
    kbdCommandMap.put(new Integer(KeyEvent.VK_NUMPAD8), pDownButton);
    kbdCommandMap.put(new Integer(KeyEvent.VK_NUMPAD2), pUpButton);
    kbdCommandMap.put(new Integer(KeyEvent.VK_NUMPAD5), nullRates);
    kbdCommandMap.put(new Integer(KeyEvent.VK_MINUS), zoomOutButton);
    kbdCommandMap.put(new Integer(KeyEvent.VK_EQUALS), zoomInButton);
    kbdCommandMap.put(new Integer(KeyEvent.VK_SEMICOLON), resetButton);
    kbdCommandMap.put(new Integer(KeyEvent.VK_NUMPAD3), yRightButton);
    kbdCommandMap.put(new Integer(KeyEvent.VK_NUMPAD1), yLeftButton);
    kbdCommandMap.put(new Integer(KeyEvent.VK_NUMPAD7), aftThrustButton);

    instruments = new AbstractInstrument[]{computer, ADIPanel, gMeter, fuelGauge};
    for(Component comp : this.allControls) {
      for(Component subComp : ((JPanel)comp).getComponents()) {
        if (subComp instanceof INamedControlContainer) {
          computer.getControlHash().putAll(((INamedControlContainer) subComp).getNamedComponents());
        }
      }
    }
    computer.getControlHash().putAll(this.getNamedComponents());
    for(String s : computer.getControlHash().keySet()) {
      System.out.println(s+" - "+computer.getControlHash().get(s));
    }
  }


  public RenderingCanvas getViewCanvas() {
    return pilotViewCanvas;
  }

  public void setTheRocket(Rocket theRocket) {
    this.theRocket = theRocket;
  }

  public Craft getTheRocket() {
    return theRocket;
  }


  public CoordSys getCoordSys() {
    return theRocket.getCoordSys();
  }

  @Override
  public int getDetailLevel() {
    return theRocket.getNorm()[0] == 0 ?11:13;
  }


  /**
   * This method should return an instance of this class which does
   * NOT initialize it's GUI elements. This method is ONLY required by
   * Jigloo if the superclass of this class is abstract or non-public. It
   * is not needed in any other situation.
   */
  public static Object getGUIBuilderInstance() {
    return new CockPitView(Boolean.FALSE);
  }

  /**
   * This constructor is used by the getGUIBuilderInstance method to
   * provide an instance of this class which has not had it's GUI elements
   * initialized (ie, initGUI is not called in this constructor).
   */
  public CockPitView(Boolean initGUI) {
    super();
  }

  public AbstractInstrument[] getInstruments() {
    return instruments;
  }

//  public void setReferencedPlanet(Abstract3DModelObject referencedPlanet) {
//    this.referencedPlanet = referencedPlanet;
//    computer.setReferenceObject(referencedPlanet);
//  }
//
//  public Abstract3DModelObject getReferencedPlanet() {
//    return referencedPlanet;
//  }

  public void drawHud() {
    if (hudSelect) {
      int h = pilotViewCanvas.getHeight();
      int w = pilotViewCanvas.getWidth();
      double xDot, yDot, zDot;
//            Planet p = (Planet)referencedPlanet;
//            double[] aVelVec= VMath.normalize(VMath.vecSubtract(theRocket.getVelocityVec(),p.getVelocity()));
      double[] aVelVec = VMath.normalize(computer.getRelativeRocketVelocity());
      CoordSys aSys = theRocket.getCoordSys();

      Graphics2D g2 = pilotViewCanvas.getG2();
      g2.setColor(Color.WHITE);
      g2.drawLine(w / 2 - 20, h / 2, w / 2 - 5, h / 2);
      g2.drawLine(w / 2 + 5, h / 2, w / 2 + 20, h / 2);
      g2.drawLine(w / 2, h / 2 - 20, w / 2, h / 2 - 5);
      g2.drawLine(w / 2, h / 2 + 5, w / 2, h / 2 + 20);

      zDot = VMath.dotprod(aSys.zAxis().getVectorForm(), aVelVec);
      if (zDot > 0)
        g2.setColor(Color.cyan);
      else
        g2.setColor(Color.red);
      xDot = -VMath.dotprod(aVelVec, aSys.xAxis().getVectorForm());
      yDot = VMath.dotprod(aVelVec, aSys.yAxis().getVectorForm());
      g2.drawOval((int) (w / 2 * (1 + xDot) - 6), (int) (h / 2 * (1 - yDot) - 6), 12, 12);
    }
  }

  public void keyTyped(KeyEvent e) {

  }

  public void keyPressed(KeyEvent evt) {
    JButton button = (JButton) kbdCommandMap.get(new Integer(evt.getKeyCode()));
    button.getModel().setPressed(true);
    if (!rcsLine)
      button.doClick();
  }

  public void keyReleased(KeyEvent e) {
    JButton button = (JButton) kbdCommandMap.get(new Integer(e.getKeyCode()));
    button.getModel().setPressed(false);
  }

  public void mouseClicked(MouseEvent e) {
    //  this.requestFocusInWindow();
  }

  public void mousePressed(MouseEvent e) {
    navPanel.requestFocus();
  }


  public void mouseReleased(MouseEvent e) {
  }

  public void mouseEntered(MouseEvent e) {
  }

  public void mouseExited(MouseEvent e) {
  }

  public void setObjectArray(Abstract3DModelObject[] planetArray) {
    this.objectArray = planetArray;
  }

  public Abstract3DModelObject[] getObjectArray() {
    return objectArray;
  }

  @Override
  public void update() {

    //computer.updateInstrument();
    //new Thread() {
    //public void run() {
    //  setPriority(Thread.MIN_PRIORITY);
    //AbstractInstrument[] instPanels = getInstruments();
    //if(instPanels!=null) {
    for (AbstractInstrument i : instruments) {
      i.updateInstrument();
    }
    //}
    //   }
    //}.start();


    super.update();
    drawHud();
  }

  public NavComputer getComputer() {
    return computer;
  }

  //@Override
  //public void accept(IDrawingVisitor v) {
  //	if(v != null)
  //	{
  //		v.visitView(this);
  //	}

  //}

  /**
   * @return the controlAdapter
   */
  public RocketControlAdapter getControlAdapter() {
    return controlAdapter;
  }

  @Override
  public Map<String, Component> getNamedComponents() {
    Map<String, Component> result = new HashMap<String, Component>();
    for(Component c : this.getComponents()) {
      if(c instanceof JPanel) {
         JPanel p = (JPanel)c;
         for(Component c2 : p.getComponents()) {
           if(c2 instanceof AbstractButton) {
             result.put(((AbstractButton)c2).getText(), c2);
           }
         }
      }
    }
    return result;
  }
}
