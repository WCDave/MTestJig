package main;

import Foundation.Utils;
import VMath.VMath;
import orbits.Abstract3DModelObject;
import orbits.Planet;
import orbits.World3DContainer;
import orbits.movement.Moveable;

import java.util.List;

public class PhysicsRunnable implements Runnable {

  final public static double G = (double) -6.67545e-11;
  final static double omegaCvt = 17.453292519943295769236907684886;

  Craft[] craft;
  //List<Craft> craftList;
  Abstract3DModelObject[] planetArray;
  List<Moveable> moveables;

//    double thrust[] = new double[]{0,0,0};
//    double grav[] = new double[]{0,0,0};
//    double drag[] = new double[]{0,0,0};
//    double norm[] = new double[]{0,0,0};
//    double windVec[] = new double[]{0,0,0};
//    double[] relativeWind = new double[]{0,0,0};

  public PhysicsRunnable(Abstract3DModelObject[] anArray, List<Moveable> moveables) {
    //craftList = b;
    planetArray = anArray;
    this.moveables = moveables;
    //System.out.println(moveables.size());

  }

  public void run() {
    long t1 = System.currentTimeMillis();
    long t2 = 0;
    double dt = 0;
    while (true) {
      t2 = System.currentTimeMillis();
      dt = (t2 - t1)/1000d;
      //dt=dt/1000L;
      t1 = t2;
      //do calcs

      synchronized (World3DContainer.getInstance().getCraftList()) {
        for (Craft c : World3DContainer.getInstance().getCraftList()) {
          c.applyAttitudeRates(dt);
          c.decrementTimeToLive();
          updateVelocityVector(c, dt);
        }
        //World3DContainer.getInstance().getCraftList().notifyAll();
      }
      updateMoveables(dt);
      Utils.sleep(0,100);
    }
  }

  private void updateMoveables(double dt) {
    if (moveables != null) {
      for (Moveable moveable : moveables) {
        moveable.executeMovement((long) dt);
      }
    }

  }

  private void updateVelocityVector(Craft craft, double dt) {
    double[] bodyVec = craft.getPosition();
    double[] planVec;
    double[] planToBodVec;
    double[] velVec;

    double thrust[] = new double[]{0, 0, 0};
    double grav[] = new double[]{0, 0, 0};
    double drag[] = new double[]{0, 0, 0};
    double norm[] = new double[]{0, 0, 0};
    double windVec[] = new double[]{0, 0, 0};
    double[] relativeWind = new double[]{0, 0, 0};

        /*VMath.zero3DVect(thrust);
        VMath.zero3DVect(grav);
        VMath.zero3DVect(drag);
        VMath.zero3DVect(norm);
        VMath.zero3DVect(windVec);
        VMath.zero3DVect(relativeWind);     */

    double[] launchVec;

    if (craft instanceof Rocket) {
      thrust = ((Rocket) craft).computeThrust(dt);
    }
    double dragCoeff, gravCoeff, planToBodDist;


    for (int i = 0; i < planetArray.length; i++) {
      Planet p;
      if (planetArray[i] instanceof Planet) {
        p = (Planet) planetArray[i];
      } else {
        continue;
      }

      planVec = p.getCoordSys().getPositionVec();
      planToBodVec = VMath.vecSubtract(bodyVec, planVec);
      planToBodDist = VMath.mag(planToBodVec);

      gravCoeff = G * craft.getMass() * p.getMass() / (planToBodDist * planToBodDist * planToBodDist);
      grav = VMath.vecAdd(grav, VMath.vecMultByScalar(planToBodVec, gravCoeff));

      craft.setGrav(grav);

      if ((p.hasAtmo()) && (planToBodDist <= 1.02 * (p.getRadius()))) {
        windVec = p.getVelocityAtLocation(planToBodVec);
        relativeWind = VMath.vecSubtract(windVec, craft.getVelocity());
        craft.setRelativeWind(relativeWind);
        double relWindMag = VMath.mag(relativeWind);
        dragCoeff = craft.getDragFactor() * p.getAtmosFactor().getAtmosDensityValue(planToBodDist - p.getRadius()) * relWindMag * relWindMag;
        drag = VMath.vecMultByScalar(VMath.normalize(relativeWind), dragCoeff);
        craft.setMach(p.getAtmosFactor().getMachNumber(relWindMag));
        craft.setTAT(p.getAtmosFactor().getTAT(relWindMag));
      }
      //http://en.wikipedia.org/wiki/Density_of_air

      craft.setDrag(drag);
      craft.setNorm(norm);
      if (planToBodDist <= p.getRadius()) {
        craft.groundBehavior(windVec, planToBodVec, p);
        norm = craft.getNorm();
      }
    }

    velVec = craft.getVelocity();
    for (int i = 0; i < 3; i++)
      velVec[i] += dt * (grav[i] + thrust[i] + drag[i] + norm[i]) / craft.getMass();

    synchronized (craft) {
      craft.setPositionVec(VMath.vecAdd(craft.getPosition(), VMath.vecMultByScalar(velVec, dt)));
    }
    //System.out.println(trackedBody.getPositionVec()[0]+" "+trackedBody.getPositionVec()[1]+" "+trackedBody.getPositionVec()[2]);
    //System.out.println(VMath.mag(aBody.getPositionVec()));
    //System.out.println("Thrust="+VMath.mag(thrust)+" Grav="+VMath.mag(grav)+" Vel="+VMath.mag(aBody.getVelocityVec())+" Drag="+VMath.mag(drag)+" Alt="+VMath.mag(aBody.getPositionVec())+" dt="+dt);
  }
}
