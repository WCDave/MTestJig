package orbits.keplerian;

import VMath.VMath;
import orbits.Abstract3DModelObject;

import java.util.concurrent.Callable;


public class KeplerCalc implements Callable<IOrbit> {

  private Abstract3DModelObject a3o;
  private static double mu = 3.98574405096e14;
  private static double TWO_PI = Math.PI*2;
  private boolean displayData;

  public KeplerCalc(Abstract3DModelObject a3o) {
    this.a3o = a3o;
  }

  public KeplerCalc(Abstract3DModelObject a3o, boolean displayData) {
    this.a3o = a3o;
    this.displayData = displayData;
  }


  @Override
  public IOrbit call() throws Exception {
   double[] vVec = a3o.getCoordSys().getVelocityAsVec();
   double[] rVec = a3o.getCoordSys().getPositionVec();

   double[] angMomentum  = VMath.crossprd(rVec, vVec);
   double[] node = VMath.crossprd(new double[]{0,0,1}, angMomentum);
   double v = VMath.mag(vVec);
   double r = VMath.mag(rVec);
   double n = VMath.mag(node);

   double eVec[] = VMath.vecMultByScalar(VMath.vecSubtract(VMath.vecMultByScalar(rVec, (v*v-mu/r)), VMath.vecMultByScalar(vVec, VMath.dotprod(rVec, vVec))), 1/mu);
   double e = VMath.mag(eVec);

   double E = v*v/2 - mu/r;
   double a = -mu/(2*E);
   double i = Math.acos(angMomentum[2] / VMath.mag(angMomentum));
   double RAAN = Math.acos(node[0] / n);
   double argp =  Math.acos(VMath.dotprod(node, eVec)/(n * e));
   double TA = Math.acos(VMath.dotprod(eVec, rVec)/(e * r));

   if(node[1] < 0) {
     RAAN = TWO_PI - RAAN;
   }
   if(eVec[2] < 0) {
     argp = TWO_PI - argp;
   }
   if(VMath.dotprod(rVec, vVec) < 0) {
     TA = TWO_PI - TA;
   }
   KeplerianElements ke = new KeplerianElements(a, e, i, RAAN, argp, TA);
   IOrbit orbit = new Orbit(ke);
   if(displayData) {
     System.out.println(a3o.getName()+": a="+a+", e="+e+", i="+Math.toDegrees(i)+", RAAN="+Math.toDegrees(RAAN)+", argP="+Math.toDegrees(argp)+", TA="+Math.toDegrees(TA));
   }
   return orbit;
  }
}
