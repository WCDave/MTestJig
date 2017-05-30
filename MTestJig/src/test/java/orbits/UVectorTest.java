package orbits;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Dave
 * Date: 9/30/13
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class UVectorTest {

  @Test
  public void testDotProd1() {

    //0.9683296637314885
    double[] input1 = new double[]{1, 2, 3};
    input1 = VMath.VMath.normalize(input1);
    UVector iut = new UVector(input1[0], input1[1], input1[2], Color.black);

    double[] input2 = new double[]{5, 6, 7};
    input2 = VMath.VMath.normalize(input2);
    double result = -iut.dotprd(input2[0], input2[1], input2[2]);
    assertTrue(result == -0.9683296637314885);

  }

  @Test
  public void testDotProd2() {
    //0.9683296637314885
    double[] input1 = new double[]{1, 2, 3};
    input1 = VMath.VMath.normalize(input1);
    UVector iut = new UVector(input1[0], input1[1], input1[2], Color.black);

    double[] input2 = new double[]{5, 6, 7};
    input2 = VMath.VMath.normalize(input2);
    double result = -iut.dotprd(input2);
    assertTrue(result == -0.9683296637314885);
  }
}
