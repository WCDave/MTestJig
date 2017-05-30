package orbits.Object3D.impl;

import orbits.CoordSys;
import orbits.Facet;
import orbits.Sphere;

import java.util.ArrayList;
import java.util.List;


public class DefaultSphereBuilder extends SphereBuilderBase {

  private final FacetElaborator elaborator;

  public DefaultSphereBuilder(Sphere s) {
    super(s);
    elaborator = new FacetElaborator(s);
  }


  @Override
  public void build() {
    Sphere sphere = s;
    double ul[] = {0, 0, 0};
    double ll[] = {0, 0, 0};
    double ur[] = {0, 0, 0};
    double lr[] = {0, 0, 0};
    sphere.getFaceList().clear();
    Facet aFace;
    int DPHI = sphere.getFaceDim().height;
    int DTHETA = sphere.getFaceDim().width;
    int thetaslice = 360 / DPHI;
    int phislice = 180 / DPHI;

    for (int i = 0; i < phislice; i++) {
      for (int j = 0; j < thetaslice; j++) {

        ul = getVectorForLatLon((float) i * DPHI, (float) j * DTHETA, sphere.getCoordSys(), obRad(i * DPHI, sphere.getRadius(), sphere.getOblateness()));

        ll = getVectorForLatLon((float) i * DPHI + DPHI, (float) j * DTHETA, sphere.getCoordSys(), obRad(i * DPHI + DPHI, sphere.getRadius(), sphere.getOblateness()));

        lr = getVectorForLatLon((float) i * DPHI + DPHI, (float) (j + 1) * DTHETA, sphere.getCoordSys(), obRad(i * DPHI + DPHI, sphere.getRadius(), sphere.getOblateness()));

        ur = getVectorForLatLon((float) i * DPHI, (float) (j + 1) * DTHETA, sphere.getCoordSys(), obRad(i * DPHI, sphere.getRadius(), sphere.getOblateness()));

        List<double[]> aList = new ArrayList<double[]>(4);
        aList.add(ul);
        aList.add(ll);
        aList.add(lr);
        aList.add(ur);

        aFace = new Facet(aList, sphere, true, Integer.toString(DPHI * i) + "|" + Integer.toString(DTHETA * j), null);
        aFace.setColor(sphere.getColor());
//				if(sphere.textureImage != null)
//				{
//					aFace.setTextureImage(sphere.textureImage.getSubimage(j*DPHI, i*DTHETA,  (sphere.textureImage.getWidth()/360)*sphere.getFaceDim().width, (sphere.textureImage.getHeight()/180)*sphere.getFaceDim().height));
//				}
        //aFace.setName(Integer.toString(dphi*i)+"|"+Integer.toString(dTheta*j));
        //System.out.println(aFace.name());
        //aFace.setComposedObject(this);
        //if(aFace.name().equals("64|36"))
        //		aFace.setColor(Color.yellow);
        sphere.getFaceList().add(aFace);
      }
    }

  }

  private float obRad(float lat, float sRadius, float oblateness) {
    return ((1 - oblateness) + 2 * oblateness * (float) Math.sin(Math.abs(lat))) * sRadius;
  }

  private double[] getVectorForLatLon(double lat, double lon, CoordSys sSys, float adjustedRadius) {
    double[] result = new double[3];
    result[0] = adjustedRadius * (float) (Math.cos(Math.toRadians(lon)) * Math.sin(Math.toRadians(lat)));
    result[1] = adjustedRadius * (float) (Math.sin(Math.toRadians(lon)) * Math.sin(Math.toRadians(lat)));
    result[2] = adjustedRadius * (float) Math.cos(Math.toRadians(lat));
    result = sSys.transformVec(result);
    return result;
  }
}
