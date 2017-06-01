package orbits.Object3D.impl;


import orbits.CoordSys;
import orbits.Sphere;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class RegularIcosahedronSphereBuilderTest {

  Mockery context = new Mockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  Sphere s = context.mock(Sphere.class);

  private RegularIcosahedronSphereBuilder iut;

  @Test
  public void testBuild() {
    context.checking(new Expectations() {{

      allowing(s).getRadius();
      will(returnValue(10f));
      allowing(s).getCoordSys();
      will(returnValue(new CoordSys()));
      allowing(s).getBuildFactor();will(returnValue(-1));
      allowing(s);
//      allowing(s).getFaceList();
//      will(returnValue(new ArrayList<Facet>()));
//      allowing(s).getCoordSys();
//      will(returnValue(new CoordSys()));
//      allowing(s).getColor();
//      will(returnValue(Color.RED));
    }});

    iut = new RegularIcosahedronSphereBuilder(s);
    iut.build();
  }
}
