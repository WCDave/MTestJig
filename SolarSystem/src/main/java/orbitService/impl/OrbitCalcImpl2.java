package orbitService.impl;

import domain.SolarSystemObject;
import orbitService.IOrbitCalc;
import org.apache.commons.math3.util.FastMath;
import org.springframework.stereotype.Component;

@Component("impl2")
public class OrbitCalcImpl2 extends BaseOrbitCalc {

    private static final double errorSize = 1e-06; //.000000000000001;
    private static final double DAYS_TO_CENTURIES = 36525.639;
    @Override
    public void computeMA(SolarSystemObject object, double aDeltaTime) {
        double result = 0;
        object.setMeanAnamoly(result);
    }

    @Override
    public void computeTA(SolarSystemObject object, long aDeltaTime) {
        /*
           E(i+1)=Ei+(M+esinEi-Ei)/(1-ecosEi).
         */
        double centuries =  millisecondsToDays(aDeltaTime)/DAYS_TO_CENTURIES;
        double a = object.getSemiMajorAxis() + object.getSmAxisDelta() * centuries;
        double e = object.getEccentricity() + object.getEccDelta() * centuries;
        double i = FastMath.toRadians(object.getInclination() + object.getIncDelta() * centuries);
        double o = FastMath.toRadians(object.getAscendingNode() + object.getAscNodeDelta() * centuries);
        double w = object.getArgOfPerhelion() + object.getAopDelta() * centuries;
        double l = object.getEpochLongitude() + object.getEpochLongitudeDelta() * centuries;

        //M = E-eSIN(E);E = M + eSIN(M) 1st approx
        //ui+1 = ui + (M - ui + e sin ui) / (1 - e cos ui)
        //computeMA(object,millisecondsToDays(aDeltaTime));
        double ma = l - w;
        //ma = ma % 360;
        //ma = ma > 180 ? ma-360 : ma;
        ma = FastMath.toRadians(ma);
        double diff=5;
        double u1=0;
        double u0 = ma;
        w = FastMath.toRadians(w);
        l = FastMath.toRadians(l);

        while (diff>errorSize){
            u1 = u0+(ma+e*FastMath.sin(u0)-u0)/(1-e*FastMath.cos(u0));
            diff=FastMath.abs(u0-u1);
            u0 = u1;
        }
        object.setTrueAnamoly(u1);
        object.setHelioPositionVec(getHelioCoordsForAngle(object.getTrueAnamoly(), i, o, w-o, a, e));
    }

    protected double[] getHelioCoordsForAngle(double ta, double inc, double an, double ap, double smAxis, double ecc){
        double xp, yp, zp;
        double xe, ye, ze;
        xp = smAxis * (FastMath.cos(ta) - ecc);
        yp = smAxis * (FastMath.sqrt(1 - ecc*ecc)) * FastMath.sin(ta);
        zp = 0;

        xe = (FastMath.cos(ap) * FastMath.cos(an) - FastMath.sin(ap) * FastMath.sin(an) * FastMath.cos(inc)) * xp + (-FastMath.sin(ap) * FastMath.cos(an) - FastMath.cos(ap)*FastMath.sin(an)*FastMath.cos(inc)) * yp;
        ye = (FastMath.cos(ap) * FastMath.sin(an) + FastMath.sin(ap) * FastMath.cos(an) * FastMath.cos(inc)) * xp + (-FastMath.sin(ap) * FastMath.sin(an) + FastMath.cos(ap)*FastMath.cos(an)*FastMath.cos(inc)) * yp;
        ze = FastMath.sin(ap) * FastMath.sin(inc) * xp + FastMath.cos(ap) * FastMath.sin(inc) * yp;
        return new double[]{xe,ye,ze};
    }

    @Override
    public double[] getHelioCoordsForAngle(double anAngle, SolarSystemObject object) {
        return new double[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
