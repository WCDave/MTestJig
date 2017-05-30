package orbits;

import VMath.VMath;
import com.google.common.collect.MapMaker;
import main.AbstractView;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DefaultFaceDetailer implements IDetailer<Facet> {

  Logger log = Logger.getLogger(DefaultFaceDetailer.class);
  private int maxLevel;

  Map<Facet, Map<Integer, List<Facet>>> levelMap = new MapMaker().expiration(2, TimeUnit.MINUTES).makeMap();

  public DefaultFaceDetailer(int maxLevel) {
    this.maxLevel = maxLevel;
  }

  @Override
  public void createDetails(final Facet f, AbstractView view) {

    if (f.hasDetailFacets()) {
      f.setVectorFromView(VMath.vecAdd(view.getObjectVectorFromView(), f.mv()));
      final int level = Math.min((int) (Math.exp(-Math.pow((VMath.mag(f.vectorFromView)), .94) / (f.getComposedObject().getDetailingFactor())) * 20), maxLevel);

      if (level > 1) {
        if (f.getLastDetailLevel() != level) {
          //boolean increasingDetail = level > f.getLastDetailLevel();
          f.setLastDetailLevel(level);
          f.setDetailList(createAuxFaces(f));
        }

        for (Facet face : f.getDetailList()) {
          face.set3DObjectForDraw(view);
        }
      } else {
        //levelMap.get(f).clear();
        f.setLastDetailLevel(0);
        f.setDetailList(Collections.EMPTY_LIST);
      }
    }
  }

  private List<Facet> createAuxFaces(Facet f) {

    List<Facet> resultList = new ArrayList<Facet>(2000);
//        if (f.getVectorList().size() == 4) {
//
//
//            List<double[]> pointList = new ArrayList<double[]>(3);
//            pointList.add(f.getVectorList().get(0));
//            pointList.add(f.getVectorList().get(1));
//            pointList.add(f.getVectorList().get(2));
//            aFace = new Facet(pointList, f.getComposedObject(), false, f.name + "*1", f);
//
//            resultList.add(aFace);
//
//            pointList.clear();
//            pointList.add(f.getVectorList().get(2));
//            pointList.add(f.getVectorList().get(3));
//            pointList.add(f.getVectorList().get(0));
//            aFace = new Facet(pointList, f.getComposedObject(), false, f.name + "*2", f);
//
//            resultList.add(aFace);
//        } else {
    resultList.add(f);
//        }

    tesselateTriangles(resultList, f.getLastDetailLevel());
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
        //double s1, s2, s3, max;
        p1 = f.getVectorList().get(0);
        p2 = f.getVectorList().get(1);
        p3 = f.getVectorList().get(2);
//                s1 = VMath.mag(VMath.vecSubtract(p2, p1));
//                s2 = VMath.mag(VMath.vecSubtract(p3, p2));
//                s3 = VMath.mag(VMath.vecSubtract(p1, p3));
//                max = Math.max(Math.max(s1, s2), s3);
//                List<Facet> resultList;
////                if (max == s1) {
////                    resultList = divideTriangle(p1, p3, p2, f);
////                } else if (max == s2) {
////                    resultList = divideTriangle(p2, p1, p3, f);
////                } else {
//                    resultList = ;
//                }
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
    //List<double[]> pointList = new ArrayList<double[]>(3);
    Facet aFace = null;

//        pointList.add(p1);
//        pointList.add(a);
//        pointList.add(p2);
    aFace = new Facet(Arrays.asList(p1, a, p2), parentFace.getComposedObject(), false, parentFace.name, parentFace);

    faceList.add(aFace);

//        pointList = new ArrayList<double[]>(3);
//        pointList.add(p3);
//        pointList.add(a);
//        pointList.add(p2);
    aFace = new Facet(Arrays.asList(p3, a, p2), parentFace.getComposedObject(), false, parentFace.name, parentFace);

    faceList.add(aFace);
    return faceList;
  }

  private List<Facet> divideTriangle2(double[] p1, double[] p2, double[] p3, Facet parentFace) {
    List<Facet> result = new ArrayList<Facet>();
//    double[] p1 = f.getVectorList().get(0);
//    double[] p2 = f.getVectorList().get(1);
//    double[] p3 = f.getVectorList().get(2);
    Sphere s = (Sphere) parentFace.getComposedObject();

    double[] newVert1 = VMath.vecMultByScalar(VMath.normalize(VMath.vecAdd(p1, VMath.vecMultByScalar(VMath.vecSubtract(p2, p1), 0.5))), s.getRadius());
    double[] newVert2 = VMath.vecMultByScalar(VMath.normalize(VMath.vecAdd(p2, VMath.vecMultByScalar(VMath.vecSubtract(p3, p2), 0.5))), s.getRadius());
    double[] newVert3 = VMath.vecMultByScalar(VMath.normalize(VMath.vecAdd(p3, VMath.vecMultByScalar(VMath.vecSubtract(p1, p3), 0.5))), s.getRadius());

    Facet newFace = new Facet(Arrays.asList(newVert1, newVert2, newVert3), s, true, null, parentFace);
    result.add(newFace);

    newFace = new Facet(Arrays.asList(p1, newVert1, newVert3), s, true, null, parentFace);
    result.add(newFace);

    newFace = new Facet(Arrays.asList(newVert1, p2, newVert2), s, true, null, parentFace);
    result.add(newFace);

    newFace = new Facet(Arrays.asList(newVert2, p3, newVert3), s, true, null, parentFace);
    result.add(newFace);
    return result;
  }

  public static void saveSerializedDetails(final Facet f) {
//    Map<Facet, Map<Integer, List<Facet>>> levelMap = new HashMap<Facet, Map<Integer, List<Facet>>>();
//    BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(20);
//    ThreadPoolExecutor tpe = new ThreadPoolExecutor(20,20,10, TimeUnit.MILLISECONDS, queue);
//    for (int i = 1; i < 12; i++) {
//      final int level = i;
//
//       List<Facet> faceList1 = Facet.createAuxFaces(level, f);
//       List<Facet> faceList2 = Facet.createAuxFaces(level-1, f);
//      final List<Facet> faceList = new ArrayList<Facet>(faceList1.subList(faceList2.size(), faceList1.size()));
//       Runnable r =  new Runnable() {
//          public void run() {
//            try {
//              ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("c:\\playdata\\"+f.getComposedObject().getName() + "_" + f.getName().replace("|", "_") + "_" + level + ".dat"));
//              oos.writeObject(faceList);
//              oos.flush();
//              oos.close();
//            } catch (Exception e) {
//              e.printStackTrace();
//            }
//
//            }};
//
//      tpe.execute(r);
//
//  }
//    tpe.shutdown();
//    try {
//      tpe.awaitTermination(10, TimeUnit.MINUTES);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }

  }
}
