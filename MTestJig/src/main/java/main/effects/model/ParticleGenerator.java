package main.effects.model;


import Foundation.Utils;
import VMath.VMath;
import main.Craft;
import orbits.Abstract3DModelObject;
import orbits.Facet;
import orbits.Point3D;
import org.apache.commons.math3.util.FastMath;

import java.awt.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ParticleGenerator {


  public static Craft generateRandomParticle(double[] position, double[] velocity, Color color) {
    Craft result;
    if(System.currentTimeMillis() % 2 == 0) {
      result = generateFacetCraft(position, velocity, color, 1);
      //System.out.println("F");
    }
    else {
      result = generatePointCraft(position, velocity, color, 1);
      //System.out.println("P");
    }
   // result.setName("" + System.currentTimeMillis());
    return result;
  }

  public static Craft generateFacetCraft(double[] position, double[] velocity, Color color, double distance) {

    double distanceFactor = 250*distance/(400+distance)+20;
    //System.out.println(distanceFactor+" "+distance);
    List<double[]> vecList = Arrays.asList(VMath.vecAdd(position, new double[]{FastMath.max(FastMath.random()*distanceFactor, 10),0,0}),
            VMath.vecAdd(position, new double[]{0,Math.max(FastMath.random()*distanceFactor, 10),0}),
            VMath.vecAdd(position, new double[]{0,0,Math.max(FastMath.random()*distanceFactor, 10)}));

    Facet impl = new Facet(vecList, new Abstract3DModelObject.STUBA3O(), false, null, null) {
      @Override
      public Abstract3DModelObject getComposedObject() {
        return this;
      }
    };
    Facet.individuateFacet(impl);
    impl.setColor(color);
    impl.getCoordSys().setPositionAsVec(position);
    //impl.getCoordSys().setVelocityAsVec(velocity);
    //impl.setDrawFaceOutlines(true);
    impl.setPaintBothSides(true);
    Craft result = new Particle(FastMath.max((float)(5000 * FastMath.random()), .5f), impl, (long) (FastMath.random() * 4000));
    result.adjustPitchRate(.5f-(float) FastMath.random());
    result.adjustRollRate(.5f - (float) FastMath.random());
    result.adjustYawRate(.5f - (float) FastMath.random());
    result.setName("" + System.currentTimeMillis());
    result.setVelocityVec(velocity);
    result.setPositionVec(position);
    return result;
  }

  public static Craft generatePointCraft(double[] position, double[] velocity, Color color, double distance) {
    Abstract3DModelObject impl = new Point3D(position, color);;

    impl.getCoordSys().setPositionAsVec(position);
//    impl.getCoordSys().setVelocityAsVec(velocity);
    impl.setColor(color);
    Craft result = new Particle((float)(500 * Math.random()), impl, (long) (Math.random() * 4000));
    result.setColor(color);
    result.setVelocityVec(velocity);
    result.setPositionVec(position);
    result.setName("" + System.currentTimeMillis());
    return result;
  }

  final ReentrantLock rl = new ReentrantLock();

  public void test(){
    try {
      System.out.println(Thread.currentThread().getName()+"**");
      rl.lock();
      System.out.println(Thread.currentThread().getName() + "--");
      int o=0;
      System.out.println(Thread.currentThread().getName() + "$$");
    }
    finally {
      System.out.println(Thread.currentThread().getName() + "%%");
      rl.unlock();
      System.out.println(Thread.currentThread().getName() + "@@");
    }
  }

  public static void main(String[] args) {
    final ParticleGenerator p = new ParticleGenerator();

    Thread t1 = new Thread() {
      public void run() {
        while(true) {
          synchronized (p) {
            try {
              System.out.println(this.getName()+" Wait "+new Date());
              p.wait();
              System.out.println(this.getName()+" Done Wait "+new Date());
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            //Utils.sleep(2000);
          }
        }
      }
    };
    t1.setName("T1");

    Thread t2 = new Thread() {
      public void run() {
        while(true) {
          synchronized (p) {
            System.out.println(this.getName()+" Notif "+new Date());
            p.notify();
          }
          Utils.sleep((long)(10000*Math.random()));
        }
      }
    };
    t2.setName("T2");

    t1.start();
    t2.start();
  }
}
