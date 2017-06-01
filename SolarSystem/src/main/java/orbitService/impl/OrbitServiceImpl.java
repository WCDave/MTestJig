package orbitService.impl;

import domain.SolarSystemObject;
import orbitDataDao.IOrbitData;
import orbitService.IOrbitCalc;
import orbitService.IOrbitService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrbitServiceImpl implements IOrbitService, InitializingBean {
    private IOrbitData orbitDataDao;
    private List<SolarSystemObject> solarSystemObjects;


    @Resource(name="impl2")
    private IOrbitCalc orbitCalc;


    @Autowired
    public OrbitServiceImpl(@Qualifier("orbitDataDaoImpl2") IOrbitData orbitDataDao) {
        this.orbitDataDao = orbitDataDao;
    }

    @Override
    public void loadPlanetData() {
        if(solarSystemObjects == null)
        {
            try {
                solarSystemObjects = orbitDataDao.loadPlanetData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public double[] get3DCoordsForSolarSystemObject(SolarSystemObject object) {
        return object.getHelioPositionVec();
    }

    private void computeMA(SolarSystemObject object, double aDeltaTime){
        orbitCalc.computeMA(object, aDeltaTime);
        //object.setMeanAnamoly(Math.toRadians((object.getMeanAnamoly0()+object.getDm()*aDeltaTime)%360));
        return;
    }

    @Override
    public void computeTA(SolarSystemObject object, long aDeltaTime){
        orbitCalc.computeTA(object, aDeltaTime);
        /** //M = E-eSIN(E);E = M + eSIN(M) 1st approx
        //ui+1 = ui + (M - ui + e sin ui) / (1 - e cos ui)
        computeMA(object,millisecondsToDays(aDeltaTime));
        double diff=5;
        double u0=object.getMeanAnamoly();
        double u1=0;
        double ecc = object.getEccentricity();
        double MA = u0;
        while (diff>errorSize){
            u1 = u0+(MA-u0+ecc*Math.sin(u0))/(1-ecc*Math.cos(u0));
            diff=Math.abs(u0-u1);
            u0 = u1;
        }
        object.setTrueAnamoly((float)Math.toDegrees(u1));   */
        //object.setHelioPositionVec(orbitCalc.getHelioCoordsForAngle(object.getTrueAnamoly(), object));
        return;
    }

    public void setCartesianCoords(SolarSystemObject object){
        object.setHelioPositionVec(orbitCalc.getHelioCoordsForAngle(object.getTrueAnamoly(), object));
        return;
    }


    @Override
    public List<SolarSystemObject> getSolarSystemObjects() {
        return solarSystemObjects;
    }

  @Override
  public void afterPropertiesSet() throws Exception {
    loadPlanetData();
  }
}
