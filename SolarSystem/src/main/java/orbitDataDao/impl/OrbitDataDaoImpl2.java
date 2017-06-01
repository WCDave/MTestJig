package orbitDataDao.impl;

import domain.SolarSystemObject;
import jaxb.Planet;
import jaxb.SolarSystem;
import orbitDataDao.IOrbitData;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Repository("orbitDataDaoImpl2")
public class OrbitDataDaoImpl2 implements IOrbitData {

    Logger log = Logger.getLogger(OrbitDataDaoImpl2.class);

    @Override
    public List<SolarSystemObject> loadPlanetData() throws Exception {
        String orbitData = System.getProperty("orbitData","Orbit_Data.xml");
        File inputFile = new File(orbitData);
        log.info("File " + inputFile.getAbsolutePath() + " exists: " + inputFile.exists());

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        StreamSource ss = new StreamSource(OrbitDataDaoImpl2.class.getResourceAsStream("/xsds/OrbitData.xsd"));

        Schema schema = sf.newSchema(ss);
        JAXBContext jaxbContext =   JAXBContext.newInstance("jaxb");
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setSchema(schema);
        Object object = unmarshaller.unmarshal(inputFile);
        List<Planet> pList =  ((SolarSystem)object).getPlanet();
        List<SolarSystemObject> result = new ArrayList<SolarSystemObject>(pList.size());
        for(Planet p : pList) {
            SolarSystemObject obj = new SolarSystemObject(p.getPlanetName(),p.getColor(), p.getEccentricity(), p.getEccentricityDelta(), p.getTrueAnamoly(),
                    p.getAscendingNode(), p.getAscendingNodeDelta(), p.getInclination(), p.getInclinationDelta(), p.getMeanAnamoly(), p.getRotationalPeriod(), p.getRadius(), p.getOblateness(),
                    p.getObliquity(), p.isAtmo(), p.getSemiMajorAxis(), p.getSemiMajorAxisDelta(), p.getMass(), p.getArgOfPerhelion(),  p.getArgOfPerhelionDelta(), p.getDm(),
                    p.getPeriod(), p.getEpochLongitude(), p.getEpochLongitudeDelta(), p.getPerihelionAngle(), p.getMeanAnamoly0(), p.getEpochDateTime().toGregorianCalendar().getTime());
            result.add(obj);
        }
        return result;
    }
}
