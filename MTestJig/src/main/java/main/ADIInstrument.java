package main;

import VMath.VMath;
import orbits.CoordSys;
import orbits.NavComputer;
import org.apache.commons.math3.util.FastMath;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class ADIInstrument extends AbstractInstrument {


  private static final Color groundColor = new Color(92, 86, 91);
  private  AffineTransform at = new AffineTransform();
  private static double PI_DIV_2 = FastMath.PI/2;
  private static String INST_NAME = "ADI";
  private NavComputer computer;

  public ADIInstrument(CockPitView aView) {
    super(aView);
    setToolTipText("Attitude Directional Indicator");
    clipShape =  new Ellipse2D.Float(5, 5, 70, 70);
    font1 = new Font(INST_NAME, Font.BOLD, 9);
    computer = aView.getComputer();
  }

  @Override
  public void updateInstrument() {
    Graphics2D g2 = instCanvas.getG2();
    g2.setFont(font1);
    g2.setColor(Color.BLACK);
    g2.drawString(INST_NAME, 1, 9);
    drawHorizon(g2);
    super.updateInstrument();
  }

  private void drawHorizon(Graphics2D g2) {
    double[] bodyToPlanetVec;
    double[] transformedVec;

    float theta, phi;
    double x, y, z;
    int wdt = instCanvas.getWidth();
    int hgt = instCanvas.getHeight();

    g2.setColor(Color.BLUE);
    g2.setClip(clipShape);
    g2.fillRect(0, 0, wdt, hgt);

    CoordSys cs = viewOwnedBy.getCoordSys();

    bodyToPlanetVec = VMath.vecSubtract(cs.getPositionVec(), computer.getReferenceObject().getCoordSys().getPositionVec());
    bodyToPlanetVec = VMath.normalize(bodyToPlanetVec);

    //double[][] matrix = VMath.transposeMatrix(cs.getCoordSysAsMatrix());
    transformedVec = VMath.transform(bodyToPlanetVec, VMath.transposeMatrix(cs.getCoordSysAsMatrix()));
    //transformedVec = cs.transformVec(bodyToPlanetVec);
    x = transformedVec[0];
    y = transformedVec[1];
    z = transformedVec[2];

    theta = (float) FastMath.atan2(y , x);
    phi = (float) (PI_DIV_2 - FastMath.atan2(FastMath.sqrt(x * x + y * y) , z));

    int thetaMultiplier = 1;
    if (phi > PI_DIV_2) {
      phi = (float) (phi - FastMath.PI);
      //thetaMultiplier = -1;
    }

    at.setToRotation(theta - PI_DIV_2, wdt / 2, hgt / 2);
    g2.setTransform(at);
    g2.setColor(groundColor);
    g2.fillRect(0, (int) (hgt / 2 * (1 + FastMath.sin(phi))), wdt, hgt);
  }
}


