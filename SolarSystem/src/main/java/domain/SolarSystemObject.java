package domain;




import orbitData.PlanetDocument;

import java.awt.*;
import java.util.Date;

public class SolarSystemObject {

    private double eccentricity;
    private double eccDelta;
    private double trueAnamoly;
    private double ascendingNode;
    private double ascNodeDelta;
    private double inclination;
    private double incDelta;
    private double meanAnamoly;
    private double rotationalPeriod;
    private double radius;
    private double oblateness;
    private double obliquity;
    private boolean atmo;
    private double semiMajorAxis;
    private double smAxisDelta;
    private double mass;
    private double argOfPerhelion;
    private double aopDelta;
    private double dm;
    private double period;
    private double epochLongitude;
    private double epochLongitudeDelta;
    private String name;
    private Color color;
    private Date epochDateTime;

    private double[] helioPositionVec = new double[3];
    private double[] geoPositionVec = new double[3];

    public SolarSystemObject(String name, PlanetDocument.Planet.Color pColor, double eccentricity, double eccDelta, double trueAnamoly,
                             double ascendingNode, double ascNodeDelta, double inclination, double incDelta, double meanAnamoly, double rotationalPeriod, double radius,
                             double oblateness, double obliquity, boolean atmo, double semiMajorAxis, double smAxisDelta, double mass, double argOfPerhelion, double aopDelta,
                             double dm, double period, double epochLongitude, double epochLongitudeDelta, double perihelionAngle, double meanAnamoly0, Date epochDateTime) {
        this.eccentricity = eccentricity;
        this.eccDelta = eccDelta;
        this.trueAnamoly = trueAnamoly;
        this.ascendingNode = ascendingNode;
        this.ascNodeDelta = ascNodeDelta;
        this.inclination = inclination;
        this.incDelta = incDelta;
        this.meanAnamoly = meanAnamoly;
        this.rotationalPeriod = rotationalPeriod;
        this.radius = radius;
        this.oblateness = oblateness;
        this.obliquity = obliquity;
        this.atmo = atmo;
        this.semiMajorAxis = semiMajorAxis;
        this.smAxisDelta = smAxisDelta;
        this.mass = mass;
        this.argOfPerhelion = argOfPerhelion;
        this.aopDelta = aopDelta;
        this.dm = dm;
        this.period = period;
        this.epochLongitude = epochLongitude;
        this.epochLongitudeDelta = epochLongitudeDelta;
        this.perihelionAngle = perihelionAngle;
        this.meanAnamoly0 = meanAnamoly0;
        this.name = name;
        color = new Color(pColor.getRed(), pColor.getGreen(), pColor.getBlue());
        this.epochDateTime = epochDateTime;
    }

    private double perihelionAngle;
    private double meanAnamoly0;

    public SolarSystemObject(String planetName, jaxb.Planet.Color color, double eccentricity, double eccentricityDelta, double trueAnamoly, double ascendingNode,
                             double ascNodeDelta, double inclination, double inclinationDelta, double meanAnamoly, double rotationalPeriod,
                             double radius, double oblateness, double obliquity, boolean atmo, double semiMajorAxis, double semiMajorAxisDelta, double mass,
                             double argOfPerhelion, double argOfPerhelionDelta, double dm, double period, double epochLongitude, double epochLongitudeDelta, double perihelionAngle, double meanAnamoly0, Date time) {
        this.eccentricity = eccentricity;
        this.eccDelta = eccentricityDelta;
        this.trueAnamoly = trueAnamoly;
        this.ascendingNode = ascendingNode;
        this.ascNodeDelta = ascNodeDelta;
        this.inclination = inclination;
        this.incDelta = inclinationDelta;
        this.meanAnamoly = meanAnamoly;
        this.rotationalPeriod = rotationalPeriod;
        this.radius = radius;
        this.oblateness = oblateness;
        this.obliquity = obliquity;
        this.atmo = atmo;
        this.semiMajorAxis = semiMajorAxis;
        this.smAxisDelta = semiMajorAxisDelta;
        this.mass = mass;
        this.argOfPerhelion = argOfPerhelion;
        this.aopDelta = argOfPerhelionDelta;
        this.dm = dm;
        this.period = period;
        this.epochLongitude = epochLongitude;
        this.epochLongitudeDelta = epochLongitudeDelta;
        this.perihelionAngle = perihelionAngle;
        this.meanAnamoly0 = meanAnamoly0;
        this.name = planetName;
        this.color = new Color(color.getRed(), color.getGreen(), color.getBlue());
        this.epochDateTime = time;
    }




    public double getEccentricity() {
        return eccentricity;
    }

    public void setEccentricity(double eccentricity) {
        this.eccentricity = eccentricity;
    }

    public double getTrueAnamoly() {
        return trueAnamoly;
    }

    public void setTrueAnamoly(double trueAnamoly) {
        this.trueAnamoly = trueAnamoly;
    }

    public double getAscendingNode() {
        return ascendingNode;
    }

    public void setAscendingNode(double ascendingNode) {
        this.ascendingNode = ascendingNode;
    }

    public double getInclination() {
        return inclination;
    }

    public void setInclination(double inclination) {
        this.inclination = inclination;
    }

    public double getMeanAnamoly() {
        return meanAnamoly;
    }

    public void setMeanAnamoly(double meanAnamoly) {
        this.meanAnamoly = meanAnamoly;
    }

    public double getRotationalPeriod() {
        return rotationalPeriod;
    }

    public void setRotationalPeriod(double rotationalPeriod) {
        this.rotationalPeriod = rotationalPeriod;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getOblateness() {
        return oblateness;
    }

    public void setOblateness(double oblateness) {
        this.oblateness = oblateness;
    }

    public double getObliquity() {
        return obliquity;
    }

    public void setObliquity(double obliquity) {
        this.obliquity = obliquity;
    }

    public boolean hasAtmo() {
        return atmo;
    }

    public void setAtmo(boolean atmo) {
        this.atmo = atmo;
    }

    public double getSemiMajorAxis() {
        return semiMajorAxis;
    }

    public void setSemiMajorAxis(double semiMajorAxis) {
        this.semiMajorAxis = semiMajorAxis;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getArgOfPerhelion() {
        return argOfPerhelion;
    }

    public void setArgOfPerhelion(double argOfPerhelion) {
        this.argOfPerhelion = argOfPerhelion;
    }

    public double getDm() {
        return dm;
    }

    public void setDm(double dm) {
        this.dm = dm;
    }

    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
        this.period = period;
    }

    public double getEpochLongitude() {
        return epochLongitude;
    }

    public void setEpochLongitude(double epochLongitude) {
        this.epochLongitude = epochLongitude;
    }

    public double getPerihelionAngle() {
        return perihelionAngle;
    }

    public void setPerihelionAngle(double perihelionAngle) {
        this.perihelionAngle = perihelionAngle;
    }

    public double getMeanAnamoly0() {
        return meanAnamoly0;
    }

    public void setMeanAnamoly0(double meanAnamoly0) {
        this.meanAnamoly0 = meanAnamoly0;
    }

    public double[] getHelioPositionVec() {
        return helioPositionVec;
    }

    public void setHelioPositionVec(double[] helioPositionVec) {
        this.helioPositionVec = helioPositionVec;
    }

    public String getName() {
        return name;
    }

    public double[] getGeoPositionVec() {
        return geoPositionVec;
    }

    public void setGeoPositionVec(double[] geoPositionVec) {
        this.geoPositionVec = geoPositionVec;
    }

    public Color getColor() {
        return color;
    }

    public Date getEpochDateTime() {
        return epochDateTime;
    }

    public double getEccDelta() {
        return eccDelta;
    }

    public double getAscNodeDelta() {
        return ascNodeDelta;
    }

    public double getIncDelta() {
        return incDelta;
    }

    public double getSmAxisDelta() {
        return smAxisDelta;
    }

    public double getAopDelta() {
        return aopDelta;
    }

    public double getEpochLongitudeDelta() {
        return epochLongitudeDelta;
    }
}
