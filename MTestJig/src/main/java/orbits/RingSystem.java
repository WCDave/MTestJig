package orbits;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.AbstractView;


public class RingSystem {
  private int ringCount;
  private ArrayList<Ring> ringList = new ArrayList<Ring>();
  private Planet planet;

  public void setRingCount(int ringCount) {
    this.ringCount = ringCount;
  }

  public int ringCount() {
    ringCount = ringList.size();
    return ringCount;
  }

  public void setRingList(ArrayList<Ring> ringList) {
    this.ringList = ringList;
  }

  public ArrayList<Ring> ringList() {
    return ringList;
  }

  public void addRing(Ring aRing) throws Exception {
    Ring aTestRing;
    boolean validRing = true;
    for (int i = 0; i < ringCount(); i++) {
      aTestRing = (Ring) ringList.get(i);
      validRing = (aRing.outerRadius() < aTestRing.innerRadius()
              || aRing.innerRadius() > aTestRing.outerRadius()) && validRing;

    }
    if (!validRing)
      throw new Exception("InvalidRing");
    else
      ringList.add(aRing);

  }

  @SuppressWarnings("unchecked")
  public void buildSaturnsRings(Planet aPlanet) {
    Ring aRing = new Ring();
    aRing.setRingSystem(this);
    aRing.setInnerRadius(aPlanet.getRadius() * 1.8f);
    aRing.setOuterRadius(aPlanet.getRadius() * 2.0f);
    aRing.setRingColor(aPlanet.getColor());
    aRing.buildRing(aPlanet, 5);
    try {
      this.addRing(aRing);
    } catch (Exception e) {
      e.printStackTrace();
    }
    aRing = new Ring();
    aRing.setRingSystem(this);
    aRing.setInnerRadius(aPlanet.getRadius() * 2.05f);
    aRing.setOuterRadius(aPlanet.getRadius() * 2.4f);
    aRing.setRingColor(aPlanet.getColor());
    aRing.buildRing(aPlanet, 5);
    try {
      this.addRing(aRing);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Collections.sort((List<Ring>) ringList);
    return;
  }

  //public DList setRingSystemForDraw(double[] t2, CoordSys cs, CoordSys sSys){
  public ArrayList<Facet> setRingSystemForDraw(AbstractView aView) {
    Ring aRing;
    //double t2[]= VMath.vecSubtract(aView.getCoordSys().getPositionVec(),planet.getCoordSys().getPositionVec());
    ArrayList<Facet> aResultList = new ArrayList<Facet>();
    for (int i = 0; i < ringCount(); i++) {
      aRing = (Ring) ringList.get(i);
      aRing.setRingForDraw(aView, aResultList);
    }
    return aResultList;
  }

  public void draw(Graphics2D g2) {
    Ring aRing;
    for (int i = 0; i < ringCount(); i++) {
      aRing = (Ring) ringList.get(i);
      aRing.ringDraw(g2);
    }
  }

  public void setPlanet(Planet planetName) {
    this.planet = planetName;
  }

  public Planet planet() {
    return planet;
  }
}
