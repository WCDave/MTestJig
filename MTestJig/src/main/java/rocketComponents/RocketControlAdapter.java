package rocketComponents;

import java.awt.Component;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.Rocket;
import orbits.Abstract3DModelObject;
import orbits.CoordSys;
import Foundation.Utils;
import enums.AxisType;
import enums.ControlInputSource;

public class RocketControlAdapter implements IControlAdapter {

  private Rocket rocket;
  private CoordSys rocketNav;
  private RocketEngine rocketEngine;
  private JSlider thrustSlider;
  private Map<Integer, Component> kbdCmdMap;
  private JComboBox planetSelectCb;

  private final static int RAMPFACTOR = 4;


  public RocketControlAdapter(Rocket rocket, JSlider thrustSlider, JComboBox planetSelectCb, Map<Integer, Component> cmdMap) {
    kbdCmdMap = cmdMap;
    this.thrustSlider = thrustSlider;
    this.rocket = rocket;
    rocketNav = rocket.getCoordSys();
    rocketEngine = rocket.getEngine();
    this.planetSelectCb = planetSelectCb;

    thrustSlider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent evt) {
        JSlider slider = (JSlider) evt.getSource();
        rocketEngine.getEcu().setThrottleSetting(slider.getValue());
      }
    });
  }

  public void rampThrust(int value, ControlInputSource source) {
    thrustSlider.setValue(value);
  }

  @Override
  public void rampAdjustThrust(int value, ControlInputSource source) {
    thrustSlider.setValue(thrustSlider.getValue() + value);
  }


  public void rampRollRight() {
    Runnable run = new Runnable() {
      public void run() {
        for (int i = 0; i < RAMPFACTOR; i++) {
          rocket.adjustRollRate(-0.01f);
          Utils.sleep(300);
        }
      }
    };
    new Thread(run).start();
  }

  public void rampRollLeft() {
    for (int i = 0; i < RAMPFACTOR; i++) {
      rocket.adjustRollRate(0.01f);
      Utils.sleep(300);
    }
  }

  public void rampPitchUp() {
    for (int i = 0; i < RAMPFACTOR; i++) {
      rocket.adjustPitchRate(0.01f);
      Utils.sleep(300);
    }
  }

  public void rampPitchDown() {
    for (int i = 0; i < RAMPFACTOR; i++) {
      rocket.adjustPitchRate(-0.01f);
      Utils.sleep(300);
    }
  }

  public int getThrottleSetting() {
    return (int) rocketEngine.getThrottleSetting();
  }

  public void setThrottle(int setting) {

    ChangeListener listener = thrustSlider.getChangeListeners()[0];
    thrustSlider.removeChangeListener(listener);
    thrustSlider.setValueIsAdjusting(true);
    thrustSlider.setValue(setting);
    rocket.setThrottleSetting(setting);
    thrustSlider.addChangeListener(listener);
  }

  public void resetThrottle() {
    thrustSlider.setValueIsAdjusting(false);
    thrustSlider.setEnabled(true);
  }

  @Override
  public void rcsThrustLeft() {
    rocket.setRcsThrustAxis(AxisType.X);
    rocket.setRcsThrustValue(-Rocket.RCS_THRUST);


  }

  @Override
  public void rcsThrustRight() {
    rocket.setRcsThrustAxis(AxisType.X);
    rocket.setRcsThrustValue(Rocket.RCS_THRUST);

  }

  @Override
  public void rcsThrustUp() {
    rocket.setRcsThrustAxis(AxisType.Y);
    rocket.setRcsThrustValue(Rocket.RCS_THRUST);

  }

  @Override
  public void rcsThrustDown() {
    rocket.setRcsThrustAxis(AxisType.Y);
    rocket.setRcsThrustValue(-Rocket.RCS_THRUST);

  }

  @Override
  public void rcsThrustForward() {
    rocket.setRcsThrustAxis(AxisType.Z);
    rocket.setRcsThrustValue(Rocket.RCS_THRUST);
  }

  @Override
  public void rcsThrustAft() {
    rocket.setRcsThrustAxis(AxisType.Z);
    rocket.setRcsThrustValue(-Rocket.RCS_THRUST);

  }

  @Override
  public void rcsThrustOff() {
    rocket.setRcsThrustValue(0);
  }

  @Override
  public void selectReferenceObject(Abstract3DModelObject object) {
    planetSelectCb.getModel().setSelectedItem(object.getName());

  }
}
