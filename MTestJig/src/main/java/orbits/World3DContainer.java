package orbits;

import Foundation.Utils;
import VMath.VMath;
import domain.SolarSystemObject;
import main.AbstractView;
import main.Craft;
import orbits.Object3D.IListener;
import orbits.Object3D.impl.DefaultPositionListener;
import orbits.Object3D.impl.Listenable;
import org.apache.log4j.Logger;
import surfaces3D.IShadowProducer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class World3DContainer<T extends Abstract3DModelObject> {

  private HashMap<String, T> itemList;
  private List<Craft> craftList;
  private T liteEmitter;
  private static World3DContainer instance;
  private Drawable3D background;
  private double[] launchPoint;
  private double[] liteEmitterPositionUnitVector = new double[3];
  private List<IShadowProducer> shadowProducers = new ArrayList<IShadowProducer>(2);
  private List<Craft> removalList = new ArrayList<Craft>();
  private List<Craft> quickRemovalList = new ArrayList<Craft>();
  private List<SolarSystemObject> solarSystemObjects = Collections.EMPTY_LIST;


  private AbstractView visibleView;

  private List<Listenable<Abstract3DModelObject>> listeners = new ArrayList<Listenable<Abstract3DModelObject>>();

  private Logger log = Logger.getLogger(World3DContainer.class);

  private World3DContainer() {
    itemList = new HashMap<String, T>(20);
    craftList = new ArrayList<Craft>();
    listeners.add(new DefaultPositionListener());
  }

  public static <T extends Abstract3DModelObject> World3DContainer<T> getInstance() {
    if (instance == null) {
      instance = new World3DContainer();
    }
    return instance;
  }

  public T getItem(String key) {
    T res = itemList.get(key);
    return res;
  }

  public void registerWithListeners(T t) {
    for (Listenable listener : listeners) {
      listener.addListener(t);
    }
  }


  public List<Listenable<Abstract3DModelObject>> getListeners() {
    return listeners;
  }

  public void setLiteEmitter(T liteEmitter) {
    this.liteEmitter = liteEmitter;
    //liteEmitterPositionUnitVector = VMath.normalize(liteEmitter.getCoordSys().getPositionVec());
  }

//  public void recalculateLiteEmitterPositionUnitVector() {
//    liteEmitterPositionUnitVector = VMath.normalize(liteEmitter.getCoordSys().getPositionVec());
//  }

  public double[] getLiteEmitterPositionUnitVector(T t) {
    //liteEmitterPosition is from the earth's position/Origin...must return value with respect to t's position
    return VMath.normalize(VMath.vecSubtract(liteEmitter.getCoordSys().getPositionVec(), t.getCoordSys().getPositionVec()));
  }

  public T getLiteEmitter() {
    return liteEmitter;
  }

  public void addItemsAsArray(T[] o) {
    for (int i = 0; i < o.length; i++) {
      itemList.put(o[i].getName(), o[i]);
      if (o[i].getShadowCalculator() != null) {
        shadowProducers.add(o[i]);
      }
    }
  }

  public T[] getItemsAsArray() {
    T[] result = null;
    synchronized (itemList) {
      Collection<T> c = itemList.values();
      result = (T[]) c.toArray(new Abstract3DModelObject[itemList.size()]);
      itemList.notifyAll();
    }
    return result;
  }

  public boolean hasLiteEmitter() {
    return liteEmitter != null;
  }

  public void setBackground(Drawable3DDecorator background) {
    this.background = background;
  }

  public Drawable3D getBackground() {
    return background;
  }


  /**
   * @return the craftList
   */
  public List<Craft> getCraftList() {
    return craftList;
  }

  public void addToQuickRemovalList( Craft c) {
    synchronized (quickRemovalList) {
      quickRemovalList.add(c);
    }
  }

  public void createRemovalJobForQuickRemovalList() {
    if(quickRemovalList.isEmpty()) return;
    Thread t = new Thread() {
      public void run() {
        //System.out.println("removal job running0 " + new Date());
        //Utils.sleep(1000);
        //System.out.println("removal job running1 " + new Date());
        synchronized (quickRemovalList) {
          for (Craft c : quickRemovalList) {
            World3DContainer.getInstance().removeCraft(c);
          }
          quickRemovalList.clear();
        }
        //System.out.println("removal job done " + new Date());
      }
    };
    t.start();
  }

  public void addForEventualRemoval(Craft c) {
    System.out.println("addForEventualRemoval");
    addCraft(c);
    synchronized (removalList) {
      removalList.add(c);
    }
  }

  public void createRemovalJob(final long millisecondsBeforeRun) {
    //System.out.println("createRemovalJob");
    Thread t = new Thread() {
      public void run() {
        //System.out.println("removal job running0 " + new Date());
        Utils.sleep(millisecondsBeforeRun);
        //System.out.println("removal job running1 " + new Date());
        synchronized (removalList) {
          for (Craft c : removalList) {
            World3DContainer.getInstance().removeCraft(c);
          }
          removalList.clear();
        }
        //System.out.println("removal job done " + new Date());
      }
    };
    t.start();
  }

  public void addCraft(Craft c) {
    synchronized (craftList) {
        //if (!craftList.contains(c)) {
          craftList.add(c);
      craftList.notifyAll();
    }
        //}
    synchronized (itemList) {
      itemList.put(c.name, (T) c);
      itemList.notifyAll();
    }
  }

  public void removeCraft(Craft c) {
    synchronized (craftList) {
      //synchronized (c) {
        if (craftList.contains(c)) {
          craftList.remove(c);
        }
      //}
       craftList.notifyAll();
    }
    synchronized (itemList) {
      itemList.remove(c.getName());
      itemList.notifyAll();
    }
  }

  public double[] getLaunchPoint() {
    return launchPoint;
  }

  public void setLaunchPoint(double[] point) {
    launchPoint = point;
  }

  public Collection<T> getItems() {
    Collection<T> collection;
    synchronized (itemList) {
      collection = new ArrayList<T>(itemList.values());
      itemList.notifyAll();
    }
    return collection;
  }


  public List<IShadowProducer> getShadowProducers() {
    return shadowProducers;
  }

  public void setSolarSystemObjects(List<SolarSystemObject> solarSystemObjects) {
    this.solarSystemObjects = solarSystemObjects;
  }

  public List<SolarSystemObject> getSolarSystemObjects() {
    return solarSystemObjects;
  }

  public void aimCoordSysAtObject(CoordSys sys, String nameOfObjectToPointTo) {
    CoordSys objectSys = itemList.get(nameOfObjectToPointTo).getCoordSys();
    double[] sunPosition = objectSys.getPositionVec();
    double[] newZAxis = VMath.normalize(VMath.vecSubtract(sunPosition, sys.getPositionVec()));
    double[] newXAxis = VMath.normalize(VMath.crossprd(objectSys.xAxis().getVectorForm(), newZAxis));
    double[] newYAxis = VMath.crossprd(newZAxis, newXAxis);

    sys.setZAxis(newZAxis);
    sys.setYAxis(newYAxis);
    sys.setXAxis(newXAxis);
    sys.zRotate(180);
  }

  public void setVisibleView(AbstractView visibleView) {
    this.visibleView = visibleView;
  }

  public AbstractView getVisibleView() {
    return visibleView;
  }

}
