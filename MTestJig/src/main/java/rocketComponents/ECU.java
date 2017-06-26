package rocketComponents;

import Foundation.Utils;
import main.effects.model.ParticleGenerator;
import orbits.World3DContainer;
import VMath.*;
import org.apache.commons.math3.util.FastMath;

import java.awt.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ECU {

  private RocketEngine engine;
  private BlockingQueue<Integer> ecuQueue;
  private int delayInMilliseconds;
  private Runnable consumer;
  private Thread t;
  private ParticleThread particleThread = new ParticleThread();
  private int delayDueToNumberOfProcessors;

  public ECU(RocketEngine engine, int delayInMilliseconds) {
    this.engine = engine;
    this.delayInMilliseconds = delayInMilliseconds;
    delayDueToNumberOfProcessors = Runtime.getRuntime().availableProcessors() > 2 ? 4 : (4-Runtime.getRuntime().availableProcessors()) * 1000;
    //if() {
      particleThread.start();
    //}
  }

  public void setThrottleSetting(int newThrottleSetting) {
    ecuQueue = new ArrayBlockingQueue<Integer>(400);

    int delta = newThrottleSetting - (int) engine.getThrottleSetting();
    try {
      for (int i = 0; i < FastMath.abs(delta); i++) {
        ecuQueue.put(new Integer((int) FastMath.signum(delta)));
      }
      ecuQueue.put(new Integer(0));
    } catch (Exception e) {
    }
    if (consumer == null || t != null && !t.isAlive()) {
      consumer = new Runnable() {
        public void run() {
          int inc = 1;
          while (inc != 0) {
            try {
              inc = ecuQueue.take();
              engine.setThrottleSetting(engine.getThrottleSetting() + inc);
              particleThread.thrustChange();
              Utils.sleep(delayInMilliseconds);
            } catch (InterruptedException e) {
            }
          }
        }
      };
      t = new Thread(consumer, "ThrottleThread");
      t.start();
    }
  }

  public ParticleThread getParticleThread() {
    return this.particleThread;
  }

  class ParticleThread extends Thread {

    ParticleThread() { }

    public void thrustChange() {
      if (engine.getThrottleSetting() != 0) {
        //System.out.println("notif");
        synchronized (this) {
          notify();
        }
      }
    }

    @Override
    public void run() {
      while (true) {
        if(engine.getThrottleSetting() == 0){

          synchronized (this) {
            try {
              wait();
            } catch (InterruptedException e) {}
          }
        }

        while(engine.getThrottleSetting() != 0) {
          Utils.sleep(delayDueToNumberOfProcessors);
          int r = FastMath.min((int) (engine.getThrottleSetting() / 60 * 255), 255);
          int g = FastMath.min((int) (engine.getThrottleSetting() / 50 * 69), 255);
          //int b = 0;
          Color c = new Color(r, g, 0, 70);
          //double[] velVec = VMath.vecMultByScalar(VMath.vectorRandomizer(VMath.normalize(ECU.this.engine.getRocket().getRelativeWind()), 500f), 1);
          double[] velVec = VMath.vectorRandomizer(VMath.vecMultByScalar(engine.getRocket().getCoordSys().zAxis().getVectorForm(), -engine.getThrottleSetting()*40), .1f);

          double[] posVec = engine.getRocket().getPosition();
          World3DContainer.getInstance().addCraft(ParticleGenerator.generateFacetCraft(posVec, VMath.vecAdd(velVec,engine.getRocket().getCoordSys().getVelocityAsVec()), c, 50));
        }
      }
    }
  }

}
