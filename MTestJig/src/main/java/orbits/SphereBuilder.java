package orbits;

import VMath.VMath;
import mapUtils.MapHelper;
import orbits.Object3D.IObjectBuilder;
import orbits.Object3D.impl.RegularIcosahedronSphereBuilder;
import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class SphereBuilder {

  private static IObjectBuilder<Sphere> builder;


  public static void buildSphere(Sphere sphere) {
    builder = new RegularIcosahedronSphereBuilder(sphere);
    builder.build();
//        double ul[] = {0,0,0};
//        double ll[] = {0,0,0};
//        double ur[] = {0,0,0};
//        double lr[] = {0,0,0};
//        sphere.faceList.clear();
//        Facet aFace;
//        int DPHI = sphere.getFaceDim().height;
//        int DTHETA = sphere.getFaceDim().width;
//        int thetaslice = 360/DPHI;
//        int phislice = 180/DPHI;
//
//        for (int i=0;i<phislice;i++) {
//            for (int j=0;j<thetaslice;j++) {
//
//                ul=getVectorForLatLon((float)i*DPHI, (float)j*DTHETA, sphere.sSys, obRad(i*DPHI, sphere.getRadius(), sphere.getOblateness()));
//
//                ll=getVectorForLatLon((float)i*DPHI+DPHI,(float)j*DTHETA,sphere.sSys, obRad(i*DPHI+DPHI, sphere.getRadius(), sphere.getOblateness()));
//
//                lr=getVectorForLatLon((float)i*DPHI+DPHI,(float)(j+1)*DTHETA,sphere.sSys, obRad(i*DPHI+DPHI, sphere.getRadius(), sphere.getOblateness()));
//
//                ur=getVectorForLatLon((float)i*DPHI,(float)(j+1)*DTHETA,sphere.sSys, obRad(i*DPHI, sphere.getRadius(), sphere.getOblateness()));
//
//                List<double[]> aList = new ArrayList<double[]>(4);
//                aList.add(ul);aList.add(ll);aList.add(lr);aList.add(ur);
//
//                aFace = new Facet(aList, sphere, true, Integer.toString(DPHI*i)+"|"+Integer.toString(DTHETA*j), null);
//                aFace.setColor(sphere.color);
////				if(sphere.textureImage != null)
////				{
////					aFace.setTextureImage(sphere.textureImage.getSubimage(j*DPHI, i*DTHETA,  (sphere.textureImage.getWidth()/360)*sphere.getFaceDim().width, (sphere.textureImage.getHeight()/180)*sphere.getFaceDim().height));
////				}
//                //aFace.setName(Integer.toString(dphi*i)+"|"+Integer.toString(dTheta*j));
//                //System.out.println(aFace.name());
//                //aFace.setComposedObject(this);
//                //if(aFace.name().equals("64|36"))
//                //		aFace.setColor(Color.yellow);
//                sphere.faceList.add(aFace);
//            }
//        }
  }

  public static float obRad(float lat, float sRadius, float oblateness) {
    return ((1 - oblateness) + 2 * oblateness * (float) FastMath.sin(FastMath.abs(lat))) * sRadius;
  }

  public static double[] getVectorForLatLon(double lat, double lon, CoordSys sSys, float adjustedRadius) {
    double[] result = MapHelper.convertGeographicalDegreesToCartesianCoords(adjustedRadius, lat, lon);
    result = sSys.transformVec(result);
    return result;
  }

  public static void buildSphere2(Sphere sphere) {
    List<Facet> faceList = sphere.getFaceList();
    double[] v1 = sphere.sSys.transformVec(new double[]{0, 0, sphere.getRadius()});      //z
    double[] v2 = sphere.sSys.transformVec(new double[]{sphere.getRadius(), 0, 0});      //x
    double[] v3 = sphere.sSys.transformVec(new double[]{0, sphere.getRadius(), 0});    //y
    double[] v4 = sphere.sSys.transformVec(new double[]{-sphere.getRadius(), 0, 0});   //-x
    double[] v5 = sphere.sSys.transformVec(new double[]{0, -sphere.getRadius(), 0});    //-y
    double[] v6 = sphere.sSys.transformVec(new double[]{0, 0, -sphere.getRadius()});      //z

    List<double[]> f1List = new ArrayList<double[]>();
    f1List.add(v1);
    f1List.add(v2);
    f1List.add(v3);

    List<double[]> f2List = new ArrayList<double[]>();
    f2List.add(v1);
    f2List.add(v4);
    f2List.add(v5);

    List<double[]> f3List = new ArrayList<double[]>();
    f3List.add(v1);
    f3List.add(v4);
    f3List.add(v3);

    List<double[]> f4List = new ArrayList<double[]>();
    f4List.add(v1);
    f4List.add(v2);
    f4List.add(v5);

    List<double[]> f5List = new ArrayList<double[]>();
    f5List.add(v6);
    f5List.add(v2);
    f5List.add(v3);

    List<double[]> f6List = new ArrayList<double[]>();
    f6List.add(v6);
    f6List.add(v4);
    f6List.add(v5);

    List<double[]> f7List = new ArrayList<double[]>();
    f7List.add(v6);
    f7List.add(v4);
    f7List.add(v3);

    List<double[]> f8List = new ArrayList<double[]>();
    f8List.add(v6);
    f8List.add(v2);
    f8List.add(v5);

    Facet f1 = new Facet(f1List, sphere, true, "", null);
//    f1.setColor(sphere.getColor());
    Facet f2 = new Facet(f2List, sphere, true, "", null);
//    f2.setColor(sphere.getColor());
    Facet f3 = new Facet(f3List, sphere, true, "", null);
//    f3.setColor(sphere.getColor());
    Facet f4 = new Facet(f4List, sphere, true, "", null);
//    f4.setColor(sphere.getColor());
    Facet f5 = new Facet(f5List, sphere, true, "", null);
//    f5.setColor(sphere.getColor());
    Facet f6 = new Facet(f6List, sphere, true, "", null);
//    f6.setColor(sphere.getColor());
    Facet f7 = new Facet(f7List, sphere, true, "", null);
//    f7.setColor(sphere.getColor());
    Facet f8 = new Facet(f8List, sphere, true, "", null);
//    f8.setColor(sphere.getColor());

    faceList.add(f1);
    faceList.add(f2);
    faceList.add(f3);
    faceList.add(f4);
    faceList.add(f5);
    faceList.add(f6);
    faceList.add(f7);
    faceList.add(f8);

    elaborateFaces(faceList, sphere);
  }

  private static void elaborateFaces(List<Facet> faceList, Sphere s) {
    if (faceList.size() >= 200) return;
    List<Facet> tempList = new ArrayList<Facet>();
    for (Facet f : faceList) {
      tempList.addAll(elaborateFace(f, s));
    }
    faceList.clear();
    faceList.addAll(tempList);
    elaborateFaces(faceList, s);
  }

  private static Collection<? extends Facet> elaborateFace(Facet f, Sphere s) {
    List<Facet> newFaces = null;
    double[] centerVertex = VMath.vecMultByScalar(VMath.normalize(f.mv()), s.getRadius());
    newFaces = elab3(f.getVectorList(), s, centerVertex);

    return newFaces;
  }

  private static List<Facet> elab3(List<double[]> vectorList, Sphere s, double[] centerVertex) {
    final double[] v1 = vectorList.get(0);
    final double[] v2 = vectorList.get(1);
    final double[] v3 = vectorList.get(2);

    double[] cVertex = new double[]{centerVertex[0], centerVertex[1], centerVertex[2]};

    final List<double[]> f1List = new ArrayList<double[]>();
    f1List.add(v1);
    f1List.add(cVertex);
    f1List.add(v2);

    final List<double[]> f2List = new ArrayList<double[]>();
    f2List.add(v2);
    f2List.add(cVertex);
    f2List.add(v3);

    final List<double[]> f3List = new ArrayList<double[]>();
    f3List.add(v3);
    f3List.add(cVertex);
    f3List.add(v1);

    List<Facet> result = new ArrayList<Facet>();

    result.add(new Facet(f1List, s, true, "", null));
    result.add(new Facet(f2List, s, true, "", null));
    result.add(new Facet(f3List, s, true, "", null));

    return result;
  }
}
