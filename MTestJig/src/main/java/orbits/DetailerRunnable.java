package orbits;

import java.util.Vector;

public class DetailerRunnable implements Runnable {

  private Facet face;
  private double level;


  public DetailerRunnable(Facet face, double level) {
    this.face = face;
    this.level = level;
  }

  @Override
  public void run() {
    //Facet.createAuxFaces(Math.min((int)level,10), face);
    ((Sphere) face.getComposedObject()).getFaceMap().put(face, face.getDetailList());
    //System.out.println("detailing: "+face.getName());

  }

}
