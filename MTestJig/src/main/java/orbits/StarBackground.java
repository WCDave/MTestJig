package orbits;

import jaxb.StarList;
import jaxb.StarType;
import main.AbstractView;
import main.ConfigurationManager;
import stars.StarListDocument;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.awt.*;
import java.io.File;

public class StarBackground extends Drawable3DDecorator {

  private float[][] starDat;
  private Planet obj;

  public StarBackground(Drawable3D drawable3D, Abstract3DModelObject obj) {
    super(drawable3D);
    this.obj = (Planet) obj;
    try {
      this.starDat = getStarDatJaxb();
    } catch (Exception e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

  @Override
  public void draw(AbstractView view) {
    super.draw(view);
    ScreenPoint starVec;
    Dimension d = view.getViewCanvas().getScreenCenter();
    CoordSys viewingCoordSys = view.getCoordSys();
    for (int j = 0; j < starDat.length; j++) {
      int colorValue = (int) starDat[j][0];
      //if(colorValue > obj.getAtmosFactor().getAtmosDensityValue(1)*150) {
      //starVec=viewingCoordSys.getScrnCoords(starDat[j][1],starDat[j][2],starDat[j][3], d.width, d.height);

      starVec = viewingCoordSys.getScrnCoords(starDat[j][1], starDat[j][2], starDat[j][3], d.width, d.height);

      if (starVec.isInView()) {
        Graphics2D g2 = view.getViewCanvas().getG2();
        g2.setColor(new Color(colorValue, colorValue, colorValue));
        g2.drawLine(starVec.x, starVec.y, starVec.x, starVec.y);
      }
      //}
    }
  }

  public static float[][] getStarDat() throws Exception {
    String fileName = ConfigurationManager.getInstance().getProperty("STAR_FILE");
    File inputFile = new File(fileName);
    StarListDocument al = StarListDocument.Factory.parse(inputFile);
    int starCount = 0;
    float[][] starDat = new float[9100][4];
    for (stars.StarType st : al.getStarList().getStarArray()) {
      starDat[starCount][0] = (10 - st.getBrightness()) * 25;
      starDat[starCount][2] = (float) (2e20 * st.getX());
      starDat[starCount][3] = (float) (2e20 * st.getY());
      starDat[starCount][1] = (float) (2e20 * st.getZ());
      starCount++;
    }

    return starDat;
  }

  public static float[][] getStarDatJaxb() throws Exception {

    SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    StreamSource ss = new StreamSource(StarBackground.class.getResourceAsStream("/xsds/stars.xsd"));
    Schema schema = sf.newSchema(ss);
    JAXBContext jaxbContext = JAXBContext.newInstance("jaxb");

    String starData = ConfigurationManager.getInstance().getProperty("STAR_FILE");
    File inputFile = new File(starData);

    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    unmarshaller.setSchema(schema);
    Object object = unmarshaller.unmarshal(inputFile);
    StarList starList = (StarList) object;
    int starCount = 0;
    float[][] starDat = new float[9100][4];
    for (StarType st : starList.getStar()) {
      starDat[starCount][0] = (10 - st.getBrightness()) * 25;
      starDat[starCount][2] = (float) (2e20 * st.getX());
      starDat[starCount][3] = (float) (2e20 * st.getY());
      starDat[starCount][1] = (float) (2e20 * st.getZ());
      starCount++;
    }

    return starDat;
  }
}
