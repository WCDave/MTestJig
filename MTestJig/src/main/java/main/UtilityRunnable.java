package main;

import Foundation.Utils;
import VMath.VMath;
import domain.SolarSystemObject;
import messaging.CommLink;
import orbitService.IOrbitService;
import orbits.Abstract3DModelObject;
import orbits.CoordSys;
import orbits.Drawable3D;
import orbits.Drawable3DDecorator;
import orbits.Object3D.impl.Listenable;
import orbits.movement.Moveable;
import orbits.NavComputer;
import orbits.Object3D.IViewPositionListener;
import orbits.Planet;
import orbits.PlanetBackgroundDecorator;
import orbits.Point3D;
import orbits.Sphere;
import orbits.World3DContainer;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.jms.JMSException;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class UtilityRunnable implements Runnable {

  private Rocket theRocket;
  private List<Moveable> moveables;
  private Date startTime;
  private CommLink commandListener;
  private int CACHE_FLUSH_COUNTER = CACHE_FLUSH_LIMIT;
  private static final int CACHE_FLUSH_LIMIT = 60;
  private ReentrantLock sphereLock = new ReentrantLock();

  private IOrbitService orbitServiceImpl;

  private Logger log = Logger.getLogger(UtilityRunnable.class);

  //private static final long BASE_DATE;   // for use in planet position calc

  private ApplicationContext appContext = new ClassPathXmlApplicationContext("classpath*:/spring/*-appContext.xml");

  private static final Planet sun = (Planet) World3DContainer.getInstance().getLiteEmitter();
  private SolarSystemObject earth;

  private List<SolarSystemObject> solarSystemObjects;
//    static {
//        Calendar c = Calendar.getInstance();
//        c.setTimeZone(TimeZone.getTimeZone("GMT"));
//        c.set(1999,Calendar.DECEMBER, 31, 0, 0);
//        BASE_DATE = c.getTime().getTime();
//    }


  public UtilityRunnable(Rocket aRocket, List<Moveable> moveables, NavComputer computer, CountDownLatch countDownLatch) {
    theRocket = aRocket;
    this.moveables = moveables;
    commandListener = new CommLink(computer);
    try {
      commandListener.run();
    } catch (JMSException e) {
      log.error("Unable to make JMS link");
    }
    orbitServiceImpl = appContext.getBean(IOrbitService.class);
    //orbitServiceImpl.loadPlanetData();
    solarSystemObjects = orbitServiceImpl.getSolarSystemObjects();
    World3DContainer.getInstance().setSolarSystemObjects(solarSystemObjects);
    earth = solarSystemObjects.get(0);
    Drawable3D dec = World3DContainer.getInstance().getBackground();
    Drawable3DDecorator dec2 = new PlanetBackgroundDecorator(solarSystemObjects, dec);
    World3DContainer.getInstance().setBackground(dec2);
    log.debug("U Latch");
    countDownLatch.countDown();
  }

  public void run() {
    startTime = new Date();
    long t1 = startTime.getTime();
    initMoveables(Utils.getDecimalTimeOfDay());

    while (true) {
      long t2 = new Date().getTime();
      moveMoveables(t2 - t1);
      t1 = t2;
      collectRocketData();
      if (CACHE_FLUSH_COUNTER-- == 0) {
        flushColorMaps();
      }
      Utils.sleep(5000);
      try {
        commandListener.sendData();
      } catch (Exception e) {}
      for (SolarSystemObject obj : solarSystemObjects) {
        if (!"Sun".equals(obj.getName()))
          orbitServiceImpl.computeTA(obj, t2 - obj.getEpochDateTime().getTime());
      }
      sun.getCoordSys().setPositionAsVec(VMath.vecMultByScalar(earth.getHelioPositionVec(), -1));

      for (SolarSystemObject obj : solarSystemObjects) {
        transformHelioToGeo(obj);
      }
      VMath.renormalizeCoordSys(theRocket.getCoordSys());
      processListeners();

      List<Craft> craftList = World3DContainer.getInstance().getCraftList();
      synchronized (craftList) {
        for(Craft c : craftList) {
          if(c.decrementTimeToLive()) {
            World3DContainer.getInstance().addToQuickRemovalList(c);
          }
        }
      }
      World3DContainer.getInstance().createRemovalJobForQuickRemovalList();
    }
  }

  private void processListeners() {
    for (Listenable listenable : World3DContainer.getInstance().getListeners()) {
      if(listenable instanceof IViewPositionListener)
        ((IViewPositionListener) listenable).checkDistance(World3DContainer.getInstance().getVisibleView());
    }
  }

  private void flushColorMaps() {
    Abstract3DModelObject[] objects = World3DContainer.getInstance().getItemsAsArray();
    for (Abstract3DModelObject object : objects) {
      if (object instanceof Sphere) {
        try {
          sphereLock.lock();
          ((Sphere) object).getFaceMap().clear();
          //log.info("flushing caches");
        } finally {
          sphereLock.unlock();
        }
      }
    }
    CACHE_FLUSH_COUNTER = CACHE_FLUSH_LIMIT;

  }

  private void initMoveables(double now) {
    if (moveables != null) {
      for (Moveable m : moveables) {
        m.initMoveable(now);
      }
    }
  }

  private void moveMoveables(long dt) {
    if (moveables != null) {
      for (Moveable m : moveables) {
        m.executeMovement(dt);
      }
    }
  }

  /**
   *
   */
  private void collectRocketData() {
    if (theRocket.isTracking()) {
      Vector<Point3D> aList = theRocket.getDataList();
      if (aList.size() > 3000) {
        aList.remove(0);
      }
      aList.add(new Point3D(theRocket.getCoordSys().getPositionVec(), Color.red));
    }
  }

  private void updateSolarSystemObjects(long deltaT) {
    for (SolarSystemObject obj : solarSystemObjects) {
      orbitServiceImpl.computeTA(obj, deltaT);
    }

  }

  public Date getStartTime() {
    return startTime;
  }

  void transformHelioToGeo(SolarSystemObject otherPlanet) {
    otherPlanet.setGeoPositionVec(VMath.vecAdd(sun.getPosition(), otherPlanet.getHelioPositionVec()));
    Abstract3DModelObject a3o = World3DContainer.getInstance().getItem(otherPlanet.getName());
    if(a3o != null) {
      a3o.getCoordSys().setPositionAsVec(otherPlanet.getGeoPositionVec());
    }
  }

  private void renormalizeCoordSys(CoordSys cs ) {
    //synchronized (cs) {
    double[] zAxis = VMath.normalize(cs.zAxis().getVectorForm());
    double[] yAxis = VMath.normalize(cs.yAxis().getVectorForm());
    double[] xAxis = VMath.crossprd(yAxis, zAxis);
    yAxis = VMath.crossprd(zAxis, xAxis);
    cs.setXAxis(xAxis);
    cs.setYAxis(yAxis);
    cs.setZAxis(zAxis);
    //}
  }


}
