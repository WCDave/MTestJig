package orbits;

import orbits.movement.Movement;

public class EarthRotationMovement implements Movement {

  private Planet earth;


  public EarthRotationMovement(Planet p) {
    earth = p;
  }

  @Override
  public void execute(double dt) {
    double angle = -((double) dt) * earth.getAngVel()[2];
    performRotation(angle, earth);

  }

  public static void performRotation(double angle, Planet planet) {
//		CoordSys sSys =planet.sSys;
//		sSys.zRotate(angle);
//		for(Facet face:planet.faceList) {
//			List<double[]> resultList = new ArrayList<double[]>(face.getVectorList().size());
//
//			for(double[] u:face.getVectorList()) {
//				resultList.add(sSys.rotatePointAroundAxis(AxisType.Z,u,angle));
//			}
//			face.setVectorList(resultList);
//			face.setMv(sSys.rotatePointAroundAxis(AxisType.Z,face.mv(),angle));
//
//			List<double[]> aList = face.getSurfaceFeatureData();
//
//			for(int i=0;i<aList.size();i++){
//				double[] aPoint = aList.get(i);
//				if(aPoint!=null)
//				    aList.set(i,sSys.rotatePointAroundAxis(AxisType.Z,aPoint,angle));
//			}
//		}
  }

}
