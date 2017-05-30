package junits;

import gui.ComputerAbstractButton;
import gui.JButtonGroup;
import orbits.Planet;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.awt.*;


@RunWith(JUnit4.class)
public class RocketTester {

  private Mockery context = new Mockery() {
    {
      setImposteriser(ClassImposteriser.INSTANCE);
    }
  };

  JButtonGroup jbGroup = new JButtonGroup(false);
  ComputerAbstractButton buttonMock1;
  ComputerAbstractButton buttonMock2;

  @Before
  public void setUp() throws Exception {

    buttonMock1 = context.mock(ComputerAbstractButton.class, "b1");
    buttonMock2 = context.mock(ComputerAbstractButton.class, "b2");

  }


  @Test
  public void testJButtonGroup() {
    context.checking(new Expectations() {{
      oneOf(buttonMock1).isSelected();
      will(returnValue(true));
      oneOf(buttonMock2).setSelected(false);
      oneOf(buttonMock2).doClick();
      oneOf(buttonMock1).setGroup(jbGroup);
      oneOf(buttonMock2).setGroup(jbGroup);
    }});

    jbGroup.add(buttonMock1);
    jbGroup.add(buttonMock2);
    jbGroup.handleButtonClick(buttonMock1);
  }

  @Test
  public void testAngVel() {
    Planet p = new Planet((float) 5.99e24, (float) 6.37e6, Color.blue,
            new double[]{0, 0, 4.166666570216051615369175570158e-6, 0},
            true, null, null);
    double[] v = p.getVelocityAtLocation(new double[]{6.37e6, 0, 0});
    int uu = 0;
  }
}
