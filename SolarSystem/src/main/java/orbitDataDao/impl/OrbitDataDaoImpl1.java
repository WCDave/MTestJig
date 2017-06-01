package orbitDataDao.impl;

import domain.SolarSystemObject;
import orbitData.PlanetDocument;
import orbitData.SolarSystemDocument;
import orbitDataDao.IOrbitData;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Repository("orbitDataDaoImpl1")
public class OrbitDataDaoImpl1 implements IOrbitData {

    Logger log = Logger.getLogger(OrbitDataDaoImpl1.class);

    @Override
    public List<SolarSystemObject> loadPlanetData() throws Exception {

        XmlOptions validateOptions = new XmlOptions();
        validateOptions.setValidateOnSet();
        String orbitData = System.getProperty("orbitData","orbit_data.xml");
        File inputFile = new File(orbitData);
        log.info("File " + inputFile.getAbsolutePath() + " exists: " + inputFile.exists());
        SolarSystemDocument ssd = SolarSystemDocument.Factory.parse(inputFile, validateOptions);
        PlanetDocument.Planet[] pList = ssd.getSolarSystem().getPlanetArray();
        List<SolarSystemObject> result = new ArrayList<SolarSystemObject>(pList.length);
        for(PlanetDocument.Planet p : pList) {
            SolarSystemObject obj = new SolarSystemObject(p.getPlanetName(),  p.getColor(), p.getEccentricity(), p.getEccentricityDelta(), p.getTrueAnamoly(),
                    p.getAscendingNode(), p.getAscendingNodeDelta(), p.getInclination(), p.getInclinationDelta(), p.getMeanAnamoly(), p.getRotationalPeriod(), p.getRadius(), p.getOblateness(),
                    p.getObliquity(), p.getAtmo(), p.getSemiMajorAxis(), p.getSemiMajorAxisDelta(), p.getMass(), p.getArgOfPerhelion(),  p.getArgOfPerhelionDelta(), p.getDm(),
                    p.getPeriod(), p.getEpochLongitude(), p.getEpochLongitudeDelta(), p.getPerihelionAngle(), p.getMeanAnamoly0(), p.getEpochDateTime().getTime());
            result.add(obj);
        }
        return result;
    }
}
