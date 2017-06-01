package junits;

import mapUtils.MapHelper;
import orbits.Facet;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MapHelper.class})
public class MapHelperTest {

  BufferedImage moonImage;
  BufferedImage earthImage;

  @Before
  public void setUp() throws Exception {

//	    try {
//		 moonImage =
//		 ImageIO.read(new File("moon_Moon_4K_1__Hermes.jpg"));
//		 
//		 earthImage = ImageIO.read(new File("earth_map.jpg"));
//		 } catch (Exception e) {
//		 e.printStackTrace();
//		 }
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  @Ignore
  public void testGetScreenCoordsFor() {
    Facet f = new Facet(Arrays.asList(new double[]{1,1,1}, new double[]{1,1,1}, new double[]{1,1,1}), null);
    PowerMock.mockStaticPartial(MapHelper.class, "getDecimalTimeOfDay");
    double i = 0;
    EasyMock.expect(MapHelper.getDecimalTimeOfDay()).andReturn(i++).anyTimes();
    PowerMock.replayAll();
    double[] result = MapHelper.getFlatMapCoordsFor(f, new Dimension(500, 200));

    PowerMock.mockStaticPartial(MapHelper.class, "getDecimalTimeOfDay");
    EasyMock.expect(MapHelper.getDecimalTimeOfDay()).andReturn(i++).anyTimes();
    PowerMock.replayAll();
    result = MapHelper.getFlatMapCoordsFor(f, new Dimension(500, 200));

    PowerMock.mockStaticPartial(MapHelper.class, "getDecimalTimeOfDay");
    EasyMock.expect(MapHelper.getDecimalTimeOfDay()).andReturn(i++).anyTimes();
    PowerMock.replayAll();
    result = MapHelper.getFlatMapCoordsFor(f, new Dimension(500, 200));

    PowerMock.mockStaticPartial(MapHelper.class, "getDecimalTimeOfDay");
    EasyMock.expect(MapHelper.getDecimalTimeOfDay()).andReturn(i++).anyTimes();
    PowerMock.replayAll();
    result = MapHelper.getFlatMapCoordsFor(f, new Dimension(500, 200));

    PowerMock.mockStaticPartial(MapHelper.class, "getDecimalTimeOfDay");
    EasyMock.expect(MapHelper.getDecimalTimeOfDay()).andReturn(i++).anyTimes();
    PowerMock.replayAll();
    result = MapHelper.getFlatMapCoordsFor(f, new Dimension(500, 200));
  }


//	//@Test
//	public void testGetDecimalTimeOfDay() {
//		double x = MapHelper.getDecimalTimeOfDay();
//		x = Math.PI/12 * x;
//		x = 0;
//	}
//
//	@Test
//	public void testGetColorFromMapForVector() {
//		Color c = null;
//		for(int i = 0;i<50;i++)
//		{
//			double[] vec  = new double[]{10+i,10+i,i+200};
//			c = MapHelper.getColorFromMapForFace(moonImage, vec, 200000);
//			System.out.println(c);
//		}
//		
//		assertTrue(c != null);
//	}

}
