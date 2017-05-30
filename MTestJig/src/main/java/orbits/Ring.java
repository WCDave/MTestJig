package orbits;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import main.AbstractView;
import VMath.VMath;

public class Ring extends Abstract3DModelObject {
  private double innerRadius;
  private double outerRadius;
  private double ringWidth = 0;
  private Color ringColor;
  private ArrayList<Facet> faceList = new ArrayList<Facet>();
  private RingSystem ringSystem;

  public Ring() {

  }

  public void setInnerRadius(double innerRadius) {
    this.innerRadius = innerRadius;
    if (ringWidth == 0 && outerRadius != 0) {
      ringWidth = outerRadius - innerRadius;
    }
  }

  public double innerRadius() {
    return innerRadius;
  }

  public void setOuterRadius(double outerRadius) {
    this.outerRadius = outerRadius;
    if (ringWidth == 0 && innerRadius != 0) {
      ringWidth = outerRadius - innerRadius;
    }
  }

  public double outerRadius() {
    return outerRadius;
  }

  public void setRingColor(Color ringColor) {
    this.ringColor = ringColor;
  }

  public Color ringColor() {
    return ringColor;
  }

  public void setFaceList(ArrayList<Facet> faceList) {
    this.faceList = faceList;
  }

  public ArrayList<Facet> faceList() {
    return faceList;
  }

  public void buildRing(Planet p, int dTheta) {
    int thetaSlice = 360 / dTheta;
    double temp_vec[] = {0, 0, 0};
    double ul[] = {0, 0, 0};
    double ll[] = {0, 0, 0};
    double ur[] = {0, 0, 0};
    double lr[] = {0, 0, 0};
    Facet aFace;
    CoordSys cs = p.getCoordSys();
    temp_vec[2] = 0;
    for (int i = 0; i < thetaSlice; i++) {
      temp_vec[0] = innerRadius * (float) (Math.cos(Math.toRadians(i * dTheta)));
      temp_vec[1] = innerRadius * (float) (Math.sin(Math.toRadians(i * dTheta)));
      ul = cs.transformVec(temp_vec);

      temp_vec[0] = outerRadius * (float) (Math.cos(Math.toRadians(i * dTheta)));
      temp_vec[1] = outerRadius * (float) (Math.sin(Math.toRadians(i * dTheta)));
      ll = cs.transformVec(temp_vec);

      temp_vec[0] = outerRadius * (float) (Math.cos(Math.toRadians(i * dTheta + dTheta)));
      temp_vec[1] = outerRadius * (float) (Math.sin(Math.toRadians(i * dTheta + dTheta)));
      lr = cs.transformVec(temp_vec);

      temp_vec[0] = innerRadius * (float) (Math.cos(Math.toRadians(i * dTheta + dTheta)));
      temp_vec[1] = innerRadius * (float) (Math.sin(Math.toRadians(i * dTheta + dTheta)));
      ur = cs.transformVec(temp_vec);

      List<double[]> aList = new ArrayList<double[]>(4);
      aList.add(ul);
      aList.add(ll);
      aList.add(lr);
      aList.add(ur);
      aFace = new Facet(aList, this, false, "RING|" + Integer.toString(dTheta * i), null);
      aFace.setColor(ringColor);
      //aFace.setName("RING|"+Integer.toString(dTheta*i));
      faceList.add(aFace);
    }
    return;
  }

  public void setRingForDraw(AbstractView aView, ArrayList<Facet> altList) {
    int faceCount = faceList().size();
    Facet aFace;
    CoordSys cs = aView.getCoordSys();
    double t2[] = VMath.vecSubtract(cs.getPositionVec(), ringSystem.planet().getCoordSys().getPositionVec());
    double[] pv = ringSystem.planet().sSys.getPositionVec();
    double[] mvec = {0, 0, 0, 0};
    GeneralPath aGp;
    ScreenPoint gpvec = null;
    //Planet aPlanet = (Planet)Sol.getHash().get(this.ringSystem().planetName());
    Planet aPlanet = ringSystem.planet();
    double critAngle = (float) (Math.asin(aPlanet.obRad(0) / (this.innerRadius + this.ringWidth / 2)) + Math.toRadians(5));

    double a, b, c;
    double testAngle;
    for (int i = 0; i < faceCount; i++) {
      aFace = (Facet) faceList().get(i);
      aFace.setGeneralPath(null);
      mvec = aFace.mv();

      aGp = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 4);
      aGp.reset();

      for (double[] vec : aFace.getVectorList()) {
        gpvec = cs.getScrnCoords(VMath.vecAdd(vec, pv), 1100, 900);
        aGp.moveTo((float) gpvec.x, (float) gpvec.y);
      }
      aGp.closePath();
      aFace.setGeneralPath(aGp);
      aFace.setIsBlockedFromView(VMath.dotprod(VMath.normalize(t2), mvec) < 0.0);
      a = VMath.mag(pv);
      b = VMath.mag(mvec);
      c = VMath.mag(VMath.vecAdd(pv, mvec));
      testAngle = (float) Math.acos((a * a - (b * b + c * c)) / (-2 * b * c));
      aFace.setIsIlluminated(testAngle >= critAngle);

      if (aFace.isBlockedFromView())
        altList.add(aFace);
    }
    return;
  }

  public void ringDraw(Graphics2D g2) {
    Facet aFace;
    GeneralPath aGp;
    int faceCount = faceList().size();
    for (int i = 0; i < faceCount; i++) {
      aFace = (Facet) faceList().get(i);
      aGp = aFace.itsPath();
      if (aGp != null && !aFace.isBlockedFromView()) {
        if (aFace.isIlluminated())
          g2.setColor(aFace.getColor());
        else
          g2.setColor(new Color(50, 50, 50));
        g2.fill(aGp);
        //g2.setColor(Color.black);
        g2.draw(aGp);
      }
    }
    return;
  }

  public void setRingSystem(RingSystem ringSystem) {
    this.ringSystem = ringSystem;
  }

  public RingSystem ringSystem() {
    return ringSystem;
  }

  @Override
  public void set3DObjectForDraw(AbstractView view) {
    // TODO Auto-generated method stub

  }

  @Override
  public void draw(AbstractView view) {
    // TODO Auto-generated method stub

  }
}
