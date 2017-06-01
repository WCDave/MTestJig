package junits;

import main.AbstractView;
import main.RenderingCanvas;
import orbits.CoordSys;
import orbits.Facet;
import orbits.Planet;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.List;

public class FacetSerializeTest {


  ObjectOutputStream oos;
  ObjectInputStream ois;
  FileOutputStream bos;

  @Test
  @Ignore
  public void test() throws Exception {

    BufferedImage earthImage = null;
    try {
      //earthImage = ImageIO.read(new File("earth_map.jpg"));
      URL url = getClass().getResource("/earth_map.jpg");
      earthImage = ImageIO.read(url);
    } catch (Exception e) {
      e.printStackTrace();
    }

    Planet p = new Planet((float) 5.99e24, (float) 6.37e6, Color.blue,
            new double[]{0, 0, 4.166666570216051615369175570158e-6, 0},
            true, null, earthImage);

    List<Facet> fList = p.getFaceList();
    bos = new FileOutputStream("dave.dat");
    oos = new ObjectOutputStream(bos);

    Facet.detailDot = 0;

    final CoordSys s = new CoordSys(-1, -1, -1);
    s.setPositionAsVec(new double[]{1e6, 2e6, 3e6});
    AbstractView aView = new AbstractView() {

      @Override
      public RenderingCanvas getViewCanvas() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public CoordSys getCoordSys() {
        // TODO Auto-generated method stub
        return s;
      }

      @Override
      public int getDetailLevel() {
        return 0;
      }

    };
    Facet f1 = fList.get(0);
    f1.set3DObjectForDraw(aView);
    //f1.setDetailList(Facet.createAuxFaces(1, f1));
    int length = f1.getDetailList().size();
    oos.writeObject(f1.getDetailList());
    oos.close();
    bos.close();
    //f1.getDetailList().clear();
    f1 = null;
    System.gc();

    ois = new ObjectInputStream(new FileInputStream("dave.dat"));
    Facet ft = (Facet) ois.readObject();
    int kk = 0;
  }
}
