package orbits;


import VMath.VMath;
import com.google.common.collect.MapMaker;
import main.AbstractView;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.*;

public class CachingFaceDetailerWithFuturesArrayOptimized implements ICachingDetailer<Facet> {

  private Logger log = Logger.getLogger(CachingFaceDetailerWithFuturesArrayOptimized.class);
  private int maxLevel;

  private ExecutorService executor = Executors.newFixedThreadPool(5);
  private int LIST_SIZE;

  Map<Facet, List<Facet>[]> facetLevelMap = new MapMaker().expiration(2, TimeUnit.MINUTES).makeMap();

  public CachingFaceDetailerWithFuturesArrayOptimized(int maxLevel) {
    this.maxLevel = maxLevel;
    LIST_SIZE = 2<<(maxLevel);
  }

  @Override
  public void createDetails(Facet f, AbstractView view) {
    f.setVectorFromView(VMath.vecAdd(view.getObjectVectorFromView(), f.mv()));
    if (f.hasDetailFacets()) {

      final int level = Math.max(3, Math.min((int) (Math.exp(-Math.pow((VMath.mag(f.vectorFromView)), .94) / (f.getComposedObject().getDetailingFactor())) * 20), maxLevel));

      if (f.getLastDetailLevel() != level) {
        List<Facet>[] levelMap = facetLevelMap.get(f);
        List<Facet> cachedList = null;
        if (levelMap != null) {
          cachedList = levelMap[level];

        } else {
          levelMap = (List<Facet>[])new List<?>[maxLevel+2];
          facetLevelMap.put(f, levelMap);
        }

        f.setLastDetailLevel(level);
        if (cachedList == null) {
          executor.submit(new FutureTask<Void>(new DetailCallable(f, level)));
        } else {
          f.setDetailList(cachedList);
        }
      }

      synchronized (f.getDetailList()) {
        for (Facet face : f.getDetailList()) {
          face.set3DObjectForDraw(view);
        }
      }
    }

  }

  @Override
  public void flushCache() {
    facetLevelMap.clear();
  }

  private class DetailCallable implements Callable<Void> {

    private final Facet facet;
    private int level;

    public DetailCallable(Facet f, int level) {
      this.facet = f;
      this.level = level;
    }

    @Override
    public Void call() {
      List<Facet> result = CachingFaceDetailerWithFuturesArrayOptimized.this.createAuxFaces(facet, level);
      //facet.setDetailList(result);
      facetLevelMap.get(facet)[level] = result;

      return null;
    }
  }

  private boolean isArrayPopulated(Object[] array) {

    for(Object o : array) {
      if (o != null) return true;
    }
    return false;
  }

  private int getHighestIndexPopulated(Object[] array, int level) {
    int result = 0;
    for(int i = level-1; i > 0; i--) {
      if(array[i] != null) {
        return i;
      }
    }
    return result;
  }

  private List<Facet> createAuxFaces(Facet f, int level) {
    List<Facet> resultList;
    int inputLevel = level;
    if (!isArrayPopulated(facetLevelMap.get(f))) {
      resultList = new ArrayList<Facet>(LIST_SIZE);
      resultList.add(f);
      f.setDetailList(resultList);
    } else {
      int i = getHighestIndexPopulated(facetLevelMap.get(f), level);
      resultList = facetLevelMap.get(f)[i];
      inputLevel = level - i;
    }

    synchronized (resultList) {
      tesselateTriangles(resultList, inputLevel);
    }
    return resultList;

  }

  private void tesselateTriangles(List<Facet> faceList, int level) {
    /*
     * This code assumes that each face is composed of a triangle. (3 points)
		 * This triangle will be decomposed into two new triangles which will become faces
		 * and replace the parent triangle/face in the faceList.
		 */

    if (level > 0) {
      List<Facet> tempList = new ArrayList<Facet>(4000);
      level--;
      Iterator<Facet> it = faceList.iterator();

      while (it.hasNext()) {
        Facet f = it.next();

        double[] p1, p2, p3;

        p1 = f.getVectorList().get(0);
        p2 = f.getVectorList().get(1);
        p3 = f.getVectorList().get(2);

        it.remove();
        tempList.addAll(divideTriangle(p1, p2, p3, f));
      }
      faceList.addAll(tempList);
      tesselateTriangles(faceList, level);
    }

  }

  private List<Facet> divideTriangle(double[] p1, double[] p2, double[] p3, Facet parentFace) {
    List<Facet> faceList = new ArrayList<Facet>(2);
    double[] a = VMath.vecAdd(p1, VMath.vecMultByScalar(VMath.vecSubtract(p3, p1), 0.5));

    faceList.add(new Facet(Arrays.asList(p1, a, p2), parentFace.getComposedObject(), false, parentFace.name, parentFace));

    faceList.add(new Facet(Arrays.asList(p3, a, p2), parentFace.getComposedObject(), false, parentFace.name, parentFace));
    return faceList;
  }

  @Override
  public void finalize() {
    executor.shutdownNow();
  }
}
