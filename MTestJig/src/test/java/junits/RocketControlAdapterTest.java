package junits;

import enums.ControlInputSource;
import main.Rocket;
import orbits.CoordSys;
import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import rocketComponents.RocketControlAdapter;
import rocketComponents.RocketEngine;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.powermock.api.easymock.PowerMock.*;

@RunWith(PowerMockRunner.class)
//@PrepareForTest( { ChangeListener[].class })
public class RocketControlAdapterTest {


  @Test
  public void testSetThrottle() {
    Rocket rocket = createMock(Rocket.class);
    CoordSys cs = createMock(CoordSys.class);
    RocketEngine engine = createMock(RocketEngine.class);
    JSlider slider = createMock(JSlider.class);
    JComboBox cb = createMock(JComboBox.class);
    ChangeListener[] clAr = new ChangeListener[1];
    ChangeListener cl1 = createMock(ChangeListener.class);
    clAr[0] = cl1;

    EasyMock.expect(rocket.getCoordSys()).andReturn(cs).once();
    EasyMock.expect(rocket.getEngine()).andReturn(engine).once();
    EasyMock.expect(slider.getChangeListeners()).andReturn(clAr);

    ChangeListener cl = EasyMock.anyObject();
    slider.addChangeListener(cl);
    EasyMock.expectLastCall();

    slider.removeChangeListener(cl1);
    EasyMock.expectLastCall();
    slider.setValueIsAdjusting(true);
    EasyMock.expectLastCall();

    Integer i = EasyMock.anyInt();
    slider.setValue(i);
    EasyMock.expectLastCall();

    Double d = EasyMock.anyDouble();
    rocket.setThrottleSetting(d);

    slider.addChangeListener(cl1);
    EasyMock.expectLastCall();
    replayAll();

    Map<Integer, Component> map = new HashMap<Integer, Component>();
    RocketControlAdapter a = new RocketControlAdapter(rocket, slider, cb, map);
    a.setThrottle(i);
    verifyAll();
  }

  @Test
  public void testRampThrust() {
    Rocket rocket = createMock(Rocket.class);
    CoordSys cs = createMock(CoordSys.class);
    RocketEngine engine = createMock(RocketEngine.class);
    JSlider slider = createMock(JSlider.class);
    JComboBox cb = createMock(JComboBox.class);

    EasyMock.expect(rocket.getCoordSys()).andReturn(cs).once();
    EasyMock.expect(rocket.getEngine()).andReturn(engine).once();

    ChangeListener cl = EasyMock.anyObject();
    slider.addChangeListener(cl);
    EasyMock.expectLastCall();

    Integer i = EasyMock.anyInt();
    slider.setValue(i);
    EasyMock.expectLastCall();
    replayAll();

    Map<Integer, Component> map = new HashMap<Integer, Component>();
    ControlInputSource cis = ControlInputSource.COMPUTER;
    RocketControlAdapter adapter = new RocketControlAdapter(rocket, slider, cb, map);
    adapter.rampThrust(i, cis);
    verifyAll();
  }

  @Test
  @Ignore
  public void testGetThrottleSetting() {
    Rocket rocket = createMock(Rocket.class);
    CoordSys cs = createMock(CoordSys.class);
    RocketEngine engine = createPartialMock(RocketEngine.class, "getThrottleSetting");

    EasyMock.expect(rocket.getCoordSys()).andReturn(cs).once();
    EasyMock.expect(rocket.getEngine()).andReturn(engine).once();
    EasyMock.expect(engine.getThrottleSetting()).andReturn(70000d).once();
    replayAll();

    JSlider slider = new JSlider();
    JComboBox cb = createMock(JComboBox.class);
    Map<Integer, Component> map = new HashMap<Integer, Component>();
    RocketControlAdapter adapter = new RocketControlAdapter(rocket, slider, cb, map);

    int x = adapter.getThrottleSetting();
    assertTrue(x == 70000);
    verifyAll();
  }

  @Test
  public void testResetThrottle() {
    JSlider slider = createMock(JSlider.class);
    JComboBox cb = createMock(JComboBox.class);
    Rocket rocket = createMock(Rocket.class);
    CoordSys cs = createMock(CoordSys.class);
    RocketEngine engine = createMock(RocketEngine.class);

    EasyMock.expect(rocket.getCoordSys()).andReturn(cs).once();
    EasyMock.expect(rocket.getEngine()).andReturn(engine).once();

    slider.setValueIsAdjusting(false);
    EasyMock.expectLastCall();

    slider.setEnabled(true);
    EasyMock.expectLastCall();

    ChangeListener cl = EasyMock.anyObject();
    slider.addChangeListener(cl);
    EasyMock.expectLastCall();

    replayAll();
    Map<Integer, Component> map = new HashMap<Integer, Component>();
    RocketControlAdapter adapter = new RocketControlAdapter(rocket, slider, cb, map);

    adapter.resetThrottle();
    verifyAll();
  }

}
