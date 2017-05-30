package orbits;


import VMath.VMath;
import com.google.common.collect.MapMaker;
import main.AbstractView;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.*;

public class CachingFaceDetailerWithFuturesTreeMapOptimized implements ICachingDetailer<Facet> {

  private Logger log = Logger.getLogger(CachingFaceDetailerWithFuturesTreeMapOptimized.class);
  private int maxLevel;

  private ExecutorService executor = Executors.newFixedThreadPool(5);
  private int LIST_SIZE;

  Map<Facet, TreeMap<Integer, List<Facet>>> facetLevelMap = new MapMaker().expiration(2, TimeUnit.MINUTES).makeMap();

  public CachingFaceDetailerWithFuturesTreeMapOptimized(int maxLevel) {
    this.maxLevel = maxLevel;
    LIST_SIZE = 2<<(maxLevel-1);
  }

  @Override
  public void createDetails(Facet f, AbstractView view) {
    f.setVectorFromView(VMath.vecAdd(view.getObjectVectorFromView(), f.mv()));
    if (f.hasDetailFacets()) {

      final int level = Math.max(3, Math.min((int) (Math.exp(-Math.pow((VMath.mag(f.vectorFromView)), .94) / (f.getComposedObject().getDetailingFactor())) * 20), maxLevel));

      if (f.getLastDetailLevel() != level) {
        TreeMap<Integer, List<Facet>> levelMap = facetLevelMap.get(f);
        List<Facet> cachedList = null;
        if (levelMap != null) {
          cachedList = levelMap.get(level);

        } else {
          levelMap = new TreeMap<Integer, List<Facet>>();
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
      List<Facet> result = CachingFaceDetailerWithFuturesTreeMapOptimized.this.createAuxFaces(facet, level);
      //facet.setDetailList(result);
      facetLevelMap.get(facet).put(level, result);

      return null;
    }
  }

  private List<Facet> createAuxFaces(Facet f, int level) {
    List<Facet> resultList;
    int inputLevel = level;
    if(facetLevelMap.get(f).size() == 0) {
      resultList = new ArrayList<Facet>(LIST_SIZE);
      resultList.add(f);
      f.setDetailList(resultList);
    }
    else {
      resultList = facetLevelMap.get(f).lowerEntry(level).getValue();
      inputLevel = level - facetLevelMap.get(f).lowerKey(level);
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
