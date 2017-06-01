package orbitService.impl;

import domain.SolarSystemObject;
import orbitService.IOrbitCalc;
import org.springframework.stereotype.Component;

@Component("impl1")
public class OrbitCalcImpl1 extends BaseOrbitCalc  {

    private static final double errorSize = 1e-15; //.000000000000001;

    @Override
    public void computeMA(SolarSystemObject object, double aDeltaTime) {
        object.setMeanAnamoly(Math.toRadians((object.getMeanAnamoly0()+object.getDm()*aDeltaTime)%360));
    }

    @Override
    public void computeTA(SolarSystemObject object, long aDeltaTime) {
        //M = E-eSIN(E);E = M + eSIN(M) 1st approx
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
        object.setTrueAnamoly((float)Math.toDegrees(u1));
        object.setHelioPositionVec(getHelioCoordsForAngle(object.getTrueAnamoly(), object));
    }

    @Override
    public double[] getHelioCoordsForAngle(double anAngle, SolarSystemObject object){
        /*
        r = a * (1 - e^2) / [1 + e * cos(v)]

          a is the semi-major axis
          e is the eccentricity
          v is the true anomaly
          ---

        X = r * [cos(o) * cos(v + p - o) - sin(o) * sin(v + p - o) *  cos(i)]
        Y = r * [sin(o) * cos(v + p - o) + cos(o) * sin(v + p - o) * cos(i)]
        Z = r * [sin(v + p - o) * sin(i)]

        r is radius vector
        v is true anomaly
        o is longitude of ascending node
        p is longitude of perihelion
        i is inclination of plane of orbit

        the quantity v + o - p is the angle of the planet measured
        in the plane of the orbit from the ascending node
         */
        double x,y,z;
        double i = Math.toRadians(object.getInclination());
        double v = Math.toRadians((anAngle));
        double o = Math.toRadians(object.getAscendingNode());
        double p = Math.toRadians(object.getArgOfPerhelion());
        double a = object.getSemiMajorAxis();
        double vpo = v+p-o;
        double e = object.getEccentricity();
        double r  = a * (1 - e*e) / (1 + e * Math.cos(v));
        x = r * (Math.cos(o)*Math.cos(vpo) - Math.sin(o)*Math.sin(vpo)*Math.cos(i));
        y = r * (Math.sin(o) * Math.cos(vpo) + Math.cos(o) * Math.sin(vpo) * Math.cos(i));
        z = r * (Math.sin(vpo) * Math.sin(i));
        return new double[]{x,y,z};
    }
}
