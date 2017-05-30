package experimenting;

import main.ConfigurationManager;
import main.Craft;
import main.Rocket;
import orbits.*;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class A3OSerializerDeserializer implements IA3OSerializerDeserializer {

  private Logger log = Logger.getLogger(A3OSerializerDeserializer.class);
  @Override
  public void serializeToFile(String filePath) {
    List<Abstract3DModelObject> objects = new ArrayList<>(World3DContainer.getInstance().getItems());
    ObjectOutputStream oos = null;
    try {
      FileOutputStream result = new FileOutputStream(filePath);
      oos = new ObjectOutputStream(result);
      for (Abstract3DModelObject obj : objects) {
        if(obj instanceof Rocket || obj instanceof Planet || obj instanceof GenericSatellite) {
          log.info("Ser: "+obj.getName());
          oos.writeObject(obj.getName());
          oos.writeObject(obj.getCoordSys());
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      if(oos != null) {
        try {
          oos.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  @Override
  public void  deSerializeFromFile(String filePath) {
    FileInputStream fis = null;
    ObjectInputStream ois = null;
    try {
      fis = new FileInputStream(filePath);
      ois = new ObjectInputStream(fis);

      Object obj = null;
      String name = null;
      while(true) {
        obj = ois.readObject();
        if(obj == null) break;
        if(obj instanceof String) {
           name = (String) obj;
        }
        else {
          log.info("De: "+name);
          CoordSys sys = (CoordSys)obj;
          Abstract3DModelObject a3o = World3DContainer.getInstance().getItem(name);
          if(a3o != null) {
            a3o.getCoordSys().setPositionAsVec(sys.getPositionVec());
            a3o.getCoordSys().setVelocityAsVec(sys.getVelocityAsVec());
          }
        }
      }
    }
    catch(Exception e) {
      log.info(e.toString());

    }
    finally {
      if(ois != null){
        try {
          ois.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

  }


  public static void main(String[] args) {

    BufferedImage earthImage = null;
    try {
      earthImage = ImageIO.read(new File(ConfigurationManager.getInstance().getProperty("EARTH_IMG_FILE")));
    } catch (Exception e) {
      e.printStackTrace();
    }

    Abstract3DModelObject s = new Planet(0, (float) 695950e3, Color.yellow, new double[]{0, 0, 0, 0}, false, null, null);
    s.setName("sun");
    Abstract3DModelObject p = new Planet((float) 5.99e24, (float) 6.37e6, new Color(135,206,235), new double[]{0, 0, 4.1780746480349296325186049083789e-6, 0},true, (Planet) s, earthImage);
    p.setName("earth");

    World3DContainer.getInstance().addItemsAsArray(new Abstract3DModelObject[]{s,p});

    A3OSerializerDeserializer a3os = new A3OSerializerDeserializer();
    a3os.serializeToFile("dave.dat");

    a3os.deSerializeFromFile("dave.dat");
  }
}
