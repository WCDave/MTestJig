package orbitService.impl;

import domain.SolarSystemObject;
import orbitService.IOrbitCalc;
import org.apache.commons.math3.util.FastMath;

/**
 * Created with IntelliJ IDEA.
 * User: Dave
 * Date: 10/22/13
 * Time: 8:02 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseOrbitCalc implements IOrbitCalc {

    protected double millisecondsToDays(long aTime){
        double aValue = aTime/(double)24.00;
        aValue = aValue/(double)3600000.00;
        //aValue = (float)(aValue/365.256898326);
        return aValue;
    }

    protected double[] getHelioCoordsForAngle(double ta, double inc, double an, double ap, double smAxis, double ecc){
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

        double vpo = ta + ap - an;
        double r  = smAxis * (1 - ecc*ecc) / (1 + ecc * Math.cos(ta));
        x = r * (FastMath.cos(an)*FastMath.cos(vpo) - FastMath.sin(an)*FastMath.sin(vpo)*FastMath.cos(inc));
        y = r * (FastMath.sin(an) * FastMath.cos(vpo) + FastMath.cos(an) * FastMath.sin(vpo) * FastMath.cos(inc));
        z = r * (FastMath.sin(vpo) * FastMath.sin(inc));
        return new double[]{x,y,z};
    }
}
