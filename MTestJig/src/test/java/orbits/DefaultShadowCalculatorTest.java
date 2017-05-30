package orbits;


import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import surfaces3D.DefaultShadowCalculator;
import surfaces3D.IShadowCalculator;

public class DefaultShadowCalculatorTest {

  Mockery context = new Mockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  Sphere s = context.mock(Sphere.class);
  Abstract3DModelObject sun = context.mock(Abstract3DModelObject.class, "sun");
  Abstract3DModelObject earth = context.mock(Abstract3DModelObject.class, "earth");

  IShadowCalculator iut = new DefaultShadowCalculator(s);

  @Test
  public void testIsInShadow() {
    final CoordSys sunSys = new CoordSys(50, 50, 0);
    final CoordSys earthSys = new CoordSys(0, 0, 0);

    context.checking(new Expectations() {{
      allowing(sun).getCoordSys();
      will(returnValue(sunSys));
      allowing(s).getCoordSys();
      will(returnValue(earthSys));
      allowing(s).getRadius();
      will(returnValue(10f));
    }});

    World3DContainer.getInstance().setLiteEmitter(sun);

    float result = iut.isInShadow(new double[]{0, -20, 0});
    System.out.println(result);

    result = iut.isInShadow(new double[]{-11.1, -11, 0});
    System.out.println(result);

    result = iut.isInShadow(new double[]{0, -18, 0});
    System.out.println(result);

    result = iut.isInShadow(new double[]{0, 110, 0});
    System.out.println(result);

    result = iut.isInShadow(new double[]{0, -110, 0});
    System.out.println(result);
  }

}
