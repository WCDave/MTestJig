/*
 * Created on Aug 13, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package orbits;

import VMath.VMath;
import enums.AxisType;
import main.AbstractView;
import main.ColorModel;
import main.IDrawingVisitor;
import main.PhysicsRunnable;
import main.PlanetColorModel;
import main.Shadeable;
import orbits.Object3D.IStructureContainer;
import orbits.Object3D.impl.AbstractGroundStructure;
import orbits.atmospherics.IAtmosFactor;
import orbits.atmospherics.IAtmosphereDraw;
import orbits.movement.Moveable;
import orbits.movement.Movement;
import org.apache.commons.math3.util.FastMath;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

//import DDate.*;
public class Planet extends Sphere implements Moveable, Newtonian, IStructureContainer {

  private float semiMajorAxis;
  private float eccentricity;
  private float inclination;
  private float ascendingNode;
  private float perihelionAngle;
  private float argOfPerhelion;
  private float epochLongitude;
  private float mass;
  private boolean pAtmo;
  private float trueAnamoly;
  private double period;
  private double meanAnamoly;
  private float meanAnamoly0;
  private float obliquity;
  private double rotationalPeriod; //ms/360 degree
  private float DM;
  private double[] angVel;      // deg/ms
  private double[] radianAngVel; //radians / sec
  transient private RingSystem ringSys;
  private double mu;
  private Planet centralBody;

  transient private IDrawingVisitor shadingVisitor;
  private Logger logger = Logger.getLogger(Planet.class);
  // private long deltaTimeMs;
  private Date d1;
  private double[] positionDelta;
  private Movement[] movements = {};
  private Movement[] initMovements = {};

  transient private IAtmosphereDraw atmosDraw;
  transient private IAtmosFactor atmosFactor;

  List<AbstractGroundStructure> structures = new ArrayList<AbstractGroundStructure>();

  public Planet(BufferedImage image) {
    super(image, new Dimension(4, 4));
  }

  public Planet(float mass, float radius, Color color, double[] angVel, boolean atmo, Planet plan, BufferedImage image) {
    this(image);
    this.mass = mass;
    mu = PhysicsRunnable.G * mass;
    super.setRadius(radius);
    super.color = color;
    pAtmo = atmo;
    this.angVel = angVel;
    radianAngVel = VMath.vecMultByScalar(angVel, 1000 * Math.PI / 180);
    if (VMath.mag(angVel) > 0)
      rotationalPeriod = (1 / VMath.mag(angVel)) * 360;
    setOblateness(0);
    centralBody = plan;
    colorModel = new PlanetColorModel(new Color(50, 50, 50), color, image);
    //v2 = new double[]{0,0,0,0};

  }

  public Planet getCentralBody() {
    return centralBody;
  }

  public void setObliquity(float anAngle) {
    obliquity = anAngle;
    radianAngVel = sSys.rotatePointAroundAxis(AxisType.X, radianAngVel, anAngle);
    sSys.xRotate(anAngle);
    sSys.setInverseMatrix(VMath.invert(sSys.getCoordSysAsMatrix()));
    //sSys.xRotate(-23.45);
    return;
  }

  public float getObliquity() {
    return obliquity;
  }

  public double getRotationalPeriod() {
    return rotationalPeriod;
  }

  public double[] getAngVel() {
    return angVel;
  }

  public double[] getRadianAngVel() {
    return radianAngVel;
  }

  public float getMeanAnamoly0() {
    return meanAnamoly0;
  }

  public double getMeanAnamoly() {
    return meanAnamoly;
  }

  public void setMeanAnamoly(double anAngle) {
    meanAnamoly = anAngle;
    return;
  }

  public void setMeanAnamoly0(float anAngle) {
    meanAnamoly0 = anAngle;
    return;
  }

  public float getDM() {
    return DM;
  }

  public void setDM(float anAngle) {
    DM = anAngle;
    return;
  }

  public float getArgOfPerhelion() {
    return argOfPerhelion;
  }

  public void setAtmo(boolean aFlag) {
    pAtmo = aFlag;
    return;
  }

  public boolean hasAtmo() {
    return pAtmo;
  }

  public float getSemiMajorAxis() {
    return semiMajorAxis;
  }

  public void setSemiMajorAxis(float anAxis) {
    semiMajorAxis = anAxis;
    period = FastMath.pow(semiMajorAxis, 1.5);
    return;
  }

  public double getPeriod() {
    return period;
  }

  public float getEccentricity() {
    return eccentricity;
  }

  public void setEccentricity(float anEccentricity) {
    eccentricity = anEccentricity;
    return;
  }

  public float getInclination() {
    return inclination;
  }

  public void setInclination(float anInclination) {
    inclination = anInclination;
    return;
  }

  public float getAscendingNode() {
    return ascendingNode;
  }

  public void setAscendingNode(float anAngle) {
    ascendingNode = anAngle;
    return;
  }

  public float getPerihelionAngle() {
    return perihelionAngle;
  }

  public void setPerihelionAngle(float anAngle) {
    perihelionAngle = anAngle;
    argOfPerhelion = (float) VMath.modulo(perihelionAngle - ascendingNode + 360, 360);
    return;
  }

  public float getEpochLongitude() {
    return epochLongitude;
  }

  public void setEpochLongitude(float anAngle) {
    epochLongitude = anAngle;
    return;
  }

  public float getMass() {
    return mass;
  }

  public void setMass(float aMass) {
    mass = aMass;
    mu = PhysicsRunnable.G * mass;
    ;
    return;
  }

  public static void ValidateData(Planet p) {
    System.out.println(p.getSemiMajorAxis() + " " + p.getEccentricity() + " " + p.getInclination() + " " + p.getAscendingNode() + " " + p.getPerihelionAngle() + " " + p.getArgOfPerhelion() + " " + p.getEpochLongitude() + " " + p.getMass() + " " + p.getRadius() + " " + p.hasAtmo() + " " + p.getColor().toString() + " " + p.getName());
    return;
  }

  public float getTrueAnamoly() {
    return trueAnamoly;
  }

  public synchronized void setTrueAnamoly(float anAngle) {
    trueAnamoly = anAngle;
    return;
  }

  public void setRingSys(RingSystem ringSys) {
    this.ringSys = ringSys;
  }

  public RingSystem getRingSys() {
    return ringSys;
  }

  public void draw(AbstractView aView) {
    if (atmosDraw != null) {
        atmosDraw.draw(aView);
    }
  }


  public void setMu(double gravConstant) {
    mu = gravConstant;
  }

  public double getMu() {
    return mu;
  }

  @Override
  public void setMovements(Movement[] moves) {
    movements = moves;

  }

  @Override
  public void setInitMovements(Movement[] moves) {
    initMovements = moves;
  }

  @Override
  public void executeMovement(double dt) {
    for (Movement move : movements) {
      move.execute(dt);
    }
  }

  @Override
  public void initMoveable(double t) {
    for(Movement m : initMovements) {
      m.execute(t);
    }
  }

  @Override
  public ColorModel getColorModel() {
    return colorModel;
  }

  @Override
  public IDrawingVisitor getShadingVisitor() {
    return shadingVisitor;
  }

  @Override
  public void setShadingVisitor(IDrawingVisitor v) {
    shadingVisitor = v;

  }

  @Override
  public double[] getVelocity() {
    return getCoordSys().getVelocityAsVec();
  }

  @Override
  public void updatePosition(Date d, double[] p2, double[] delta) {
    long deltaTimeMs = 0;
    if (d1 != null) {
      deltaTimeMs = d.getTime() - d1.getTime();
    }
    d1 = d;

    if (deltaTimeMs != 0d) {
      double seconds = (double) deltaTimeMs / 1000d;
      sSys.setVelocityAsVec(VMath.vecMultByScalar(delta, 1.0d / seconds));
    }
    sSys.setPositionAsVec(p2);
  }

  @Override
  public double[] getPosition() {
    return getCoordSys().getPositionVec();
  }

  @Override
  public String toString() {
    return this.name;
  }

  @Override
  public void set3DObjectForDraw(AbstractView abstractView) {
    abstractView.getDrawableList().add(this);
    super.set3DObjectForDraw(abstractView);
  }

  public IAtmosphereDraw getAtmosDraw() {
    return atmosDraw;
  }

  public void setAtmosDraw(IAtmosphereDraw atmosDraw) {
    this.atmosDraw = atmosDraw;
  }

  public double[] getSurfaceVelcVec(double[] locationOnSurface) {
    return VMath.crossprd(getAngVel(), locationOnSurface);
  }

  public double[] getVelocityAtLocation(double[] location) {
    return VMath.crossprd(this.getRadianAngVel(), location);
  }

  public IAtmosFactor getAtmosFactor() {
    if (atmosFactor != null) {
      return atmosFactor;
    }
    return new IAtmosFactor() {
      @Override
      public double getAtmosDensityValue(double distance) {
        return 0;
      }

      @Override
      public double getAtmosSunScatteringValue() {
        return 0;
      }

      @Override
      public float getMachNumber(double speed) {
        return 0;
      }

      @Override
      public float getTAT(double speed) {
        return 0;
      }
    };
  }

  public void setAtmosFactor(IAtmosFactor atmosFactor) {
    this.atmosFactor = atmosFactor;
  }


  @Override
  public List<AbstractGroundStructure> getStructures() {
    return structures;
  }
}
