package orbits;


import VMath.VMath;
import com.google.common.collect.MapMaker;
import main.AbstractView;
import org.apache.commons.math3.util.FastMath;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class CachingFaceDetailerWithFuturesArray implements ICachingDetailer<Facet> {

  private Logger log = Logger.getLogger(CachingFaceDetailerWithFuturesArray.class);
  private int maxLevel;

  private ExecutorService executor = Executors.newFixedThreadPool(5);
  private int LIST_SIZE;

  Map<Facet, List<Facet>[]> facetLevelMap = new MapMaker().expiration(5, TimeUnit.MINUTES).makeMap();

  public CachingFaceDetailerWithFuturesArray(int maxLevel) {
    this.maxLevel = maxLevel;
    LIST_SIZE = 2 << (12 - 1);
  }

  @Override
  public void createDetails(Facet f, AbstractView view) {
    f.setVectorFromView(VMath.vecAdd(view.getObjectVectorFromView(), f.mv()));
    if (f.hasDetailFacets()) {

      final int level = FastMath.max(3, FastMath.min((int) (FastMath.exp(-FastMath.pow(VMath.mag(f.vectorFromView), .95) / (f.getComposedObject().getDetailingFactor())) * 20), view.getDetailLevel()));

      if (f.getLastDetailLevel() != level) {
        List<Facet>[] levelMap = facetLevelMap.get(f);
        List<Facet> cachedList = null;
        if (levelMap != null) {
          cachedList = levelMap[level];
        }

        if (cachedList == null) {
          facetLevelMap.put(f, (List<Facet>[]) new List<?>[15]);
          executor.submit(new FutureTask<>(new DetailCallable(f, level)));
        } else {
          f.setLastDetailLevel(level);
          //System.out.println("cache hit");
          f.setDetailList(cachedList);
        }
      }

//      for (Facet face : f.getDetailList()) {
//        face.setColor(Facet.determineFaceColor(face));
//        //face.set3DObjectForDraw(view);
//      }

     executor.submit((Runnable) () -> {
       for (Facet face : f.getDetailList()) {
         face.setColor(Facet.determineFaceColor(face));
         //face.set3DObjectForDraw(view);
       }
     });
    }
    else {
      f.setColor(Facet.determineFaceColor(f));
    }

  }

  @Override
  public void flushCache() {
    facetLevelMap.clear();
  }

  private class DetailCallable implements Callable<Void> {

    private final Facet facet;
    private int level;

    DetailCallable(Facet f, int level) {
      this.facet = f;
      this.level = level;
    }

    @Override
    public Void call() {
      createAuxFaces(facet, level);
      return null;
    }

    private void createAuxFaces(Facet f, int level) {

      List<Facet> resultList = new ArrayList<Facet>(LIST_SIZE);
      resultList.add(f);

      tesselateTriangles(resultList, level);
    }

    private void tesselateTriangles(List<Facet> faceList, int level) {
    /*
     * This code assumes that each face is composed of a triangle. (3 points)
		 * This triangle will be decomposed into two new triangles which will become faces
		 * and replace the parent triangle/face in the faceList.
		 */

      if (level > 0) {
        List<Facet> tempList = new ArrayList<Facet>(4000);
        Iterator<Facet> it = faceList.iterator();

        Facet f;
        while (it.hasNext()) {
          f = it.next();

          double[] p1 = f.getVectorList().get(0);
          double[] p3 = f.getVectorList().get(2);
          double[] a = VMath.vecAdd(p1, VMath.vecMultByScalar(VMath.vecSubtract(p3, p1), 0.5));

          it.remove();
          Facet f1 = new Facet(Arrays.asList(p1, a, f.getVectorList().get(1)), f.getComposedObject(), false, f.name, f);
          //f1.setColor(Facet.determineFaceColor(f1));
          tempList.add(f1);

          f1 = new Facet(Arrays.asList(p3, a, f.getVectorList().get(1)), f.getComposedObject(), false, f.name, f);
          //f1.setColor(Facet.determineFaceColor(f1));
          tempList.add(f1);
          //tempList.addAll(divideTriangle(f.getVectorList().get(0), f.getVectorList().get(1), f.getVectorList().get(2), f));
        }
        faceList.addAll(tempList);
        tesselateTriangles(faceList, --level);
        facetLevelMap.get(facet)[this.level - level] = tempList;
      }

    }


  }

  @Override
  public void finalize() {
    executor.shutdownNow();
  }
}
