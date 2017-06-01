package orbitService;

import domain.SolarSystemObject;

public interface IOrbitCalc {

    void computeMA(SolarSystemObject object, double aDeltaTime);

    void computeTA(SolarSystemObject object, long aDeltaTime);

    double[] getHelioCoordsForAngle(double anAngle, SolarSystemObject object);
}
