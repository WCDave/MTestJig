package rocketComponents;

import orbits.Abstract3DModelObject;
import enums.ControlInputSource;

import javax.naming.ldap.Control;

public interface IControlAdapter {

  void rampThrust(int value, ControlInputSource source);

  void rampAdjustThrust(int value, ControlInputSource source);

  void rampRollRight();

  void rampRollLeft();

  void rampPitchUp();

  void rampPitchDown();

  int getThrottleSetting();

  void setThrottle(int setting);

  void resetThrottle();

  void rcsThrustLeft();

  void rcsThrustRight();

  void rcsThrustUp();

  void rcsThrustDown();

  void rcsThrustForward();

  void rcsThrustAft();

  void rcsThrustOff();

  void selectReferenceObject(Abstract3DModelObject object);
}
