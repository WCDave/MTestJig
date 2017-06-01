package orbitService;

import domain.SolarSystemObject;

import java.util.List;

public interface IOrbitService {

    void loadPlanetData();

    double[] get3DCoordsForSolarSystemObject(SolarSystemObject object);

    void computeTA(SolarSystemObject object, long aDeltaTime);

    List<SolarSystemObject> getSolarSystemObjects();

    void  setCartesianCoords(SolarSystemObject object);
}
