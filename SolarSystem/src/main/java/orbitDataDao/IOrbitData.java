package orbitDataDao;

import domain.SolarSystemObject;

import java.util.List;

public interface IOrbitData {

    List<SolarSystemObject> loadPlanetData() throws Exception;
}
