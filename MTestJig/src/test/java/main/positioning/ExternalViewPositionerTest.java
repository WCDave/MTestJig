package main.positioning;


import domain.SolarSystemObject;
import orbits.Abstract3DModelObject;
import orbits.CoordSys;
import orbits.Planet;
import orbits.World3DContainer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import main.externalView.positioning.ExternalViewPositioner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;

@RunWith(JUnit4.class)
public class ExternalViewPositionerTest {

  private Mockery context = new Mockery() {
    {
      setImposteriser(ClassImposteriser.INSTANCE);
    }
  };


  Planet p = context.mock(Planet.class);
  SolarSystemObject solarSystemObject = context.mock(SolarSystemObject.class);

  CoordSys testSys = new CoordSys();

  ExternalViewPositioner iut = new ExternalViewPositioner(0, testSys);

  @Test
  public void testPostionViewingSys() {
   Constructor c = World3DContainer.class.getDeclaredConstructors()[0];
    c.setAccessible(true);
    World3DContainer<Abstract3DModelObject> wc;
    final Map<String, Abstract3DModelObject> map = new HashMap<String, Abstract3DModelObject>();
    final List<SolarSystemObject> list = new ArrayList<SolarSystemObject>();
    list.add(solarSystemObject);
    map.put("Earth", p);
    try {
      wc = (World3DContainer<Abstract3DModelObject>) c.newInstance();
      Field f = World3DContainer.class.getDeclaredField("instance");
      f.setAccessible(true);
      f.set(World3DContainer.class, wc);
      f = World3DContainer.class.getDeclaredField("itemList");
      f.setAccessible(true);
      f.set(wc, map);
      f = World3DContainer.class.getDeclaredField("solarSystemObjects");
      f.setAccessible(true);
      f.set(wc, list);
    } catch (Exception e) {
      e.printStackTrace();
    }


    context.checking(new Expectations(){{
      allowing(p).getCoordSys();will(returnValue(new CoordSys()));
      oneOf(solarSystemObject).getGeoPositionVec();will(returnValue(new double[]{5,0,0}));
    }});
    iut.postionViewingSys();
    assertArrayEquals(new double[]{5,0,10000}, testSys.getPositionVec(), .1);
    assertArrayEquals(new double[]{1,0,0}, testSys.xAxis().getVectorForm(), .1);
    assertArrayEquals(new double[]{0,-1,0}, testSys.yAxis().getVectorForm(), .1);
    assertArrayEquals(new double[]{0,0,-1}, testSys.zAxis().getVectorForm(), .1);
  }
}
