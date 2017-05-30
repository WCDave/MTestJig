package orbits;

import VMath.VMath;
import enums.OrbitElementKeys;
import main.Rocket;
import org.apache.commons.math3.util.FastMath;
import org.apache.log4j.Logger;

import java.awt.*;

public class DataPage_I extends NavComputerDisplay {

  public static final String pageName = "Page1";
  Logger log = Logger.getLogger(DataPage_I.class);

  public DataPage_I(NavComputer computer) {
    super(computer);
  }

  public void drawPage(Graphics2D g2) {
    double alt;
    double ts;
    double vs;
    double speed;
    double[] bodyToObjectVec;

    double[] ecc;
    double rPer, rAph, vPer, vAph;
    double aoa = 0;

    ecc = (double[]) orbitElements.get(OrbitElementKeys.ecc);
    rPer = ((Double) orbitElements.get(OrbitElementKeys.rPer)).doubleValue();
    rAph = ((Double) orbitElements.get(OrbitElementKeys.rAph)).doubleValue();
    vPer = ((Double) orbitElements.get(OrbitElementKeys.vPer)).doubleValue();
    vAph = ((Double) orbitElements.get(OrbitElementKeys.vAph)).doubleValue();

    g2.setColor(Color.BLACK);
    try {
      g2.fillRect(0, 0, computer.getWidth(), computer.getHeight());
    }catch (Exception e){}

    g2.setColor(Color.YELLOW);
    g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 11));
    FontMetrics fm = g2.getFontMetrics();
    int y = fm.getHeight() - 5;
    double pRadius = ((Planet) computer.getReferenceObject()).getRadius() / NavComputer.METERS_PER_MILE;
    bodyToObjectVec = VMath.vecSubtract(computer.getCraft().getCoordSys().getPositionVec(), computer.getReferenceObject().getCoordSys().getPositionVec());

    if (computer.getCraft() instanceof Rocket) {
      g2.drawString("Thr: " + String.format("%12d", (int) (computer.getControlAdapter().getThrottleSetting() * ((Rocket) computer.getCraft()).getEngPower() / 4450)), 3, y);
    }

    alt = VMath.mag(bodyToObjectVec) / NavComputer.METERS_PER_MILE - pRadius;

    g2.drawString("Alt: " + String.format("%12.4f", alt), 2, 2 * y);

    g2.drawString("Grv: " + String.format("%12.4f", FastMath.abs(VMath.mag(computer.getCraft().getGrav()) / 4450)), 2, 3 * y);
    g2.drawString("Drg: " + String.format("%12.4f", VMath.mag(computer.getCraft().getDrag()) / 4450), 2, 4 * y);

    double[] rocketVelocity = computer.getCraft().getCoordSys().getVelocityAsVec();
    double[] rocketRelVelocity = VMath.vecSubtract(rocketVelocity, ((Planet) (computer.getReferenceObject())).getVelocity());
    speed = VMath.mag(rocketRelVelocity);
    g2.drawString("Spd: " + String.format("%12.4f", speed * 2.236f), 2, 5 * y);

    vs = VMath.dotprod(rocketRelVelocity, VMath.normalize(bodyToObjectVec));
    ts = (float) FastMath.sqrt(speed * speed - vs * vs) * 2.236f;
    vs *= 2.236f;
    aoa = FastMath.abs(VMath.dotprod(computer.getCraft().getCoordSys().zAxis().getVectorForm(), VMath.normalize(computer.getCraft().getRelativeWind())));


    g2.drawString("TS:  " + String.format("%12.4f", ts), 2, 6 * y);
    g2.drawString("VS:  " + String.format("%12.4f", vs), 2, 7 * y);
    g2.drawString("Mach:" + String.format("%12.4f", computer.getCraft().getMach()), 2, 8 * y);
    g2.drawString("TAT: " + String.format("%12.4f", computer.getCraft().getTAT()), 2, 9 * y);


    y += 4;

    g2.drawString("rApo: " + String.format("%13.4f(%13.4f)", rAph, rAph - pRadius), 135, 2 * y);
    g2.drawString("rPer: " + String.format("%13.4f(%13.4f)", rPer, rPer - pRadius), 135, 3 * y);
    g2.drawString("vApo: " + String.format("%13.4f", vAph), 135, 4 * y);
    g2.drawString("vPer: " + String.format("%13.4f", vPer), 135, 5 * y);
    g2.drawString("ecc:  " + String.format("%13.4f", VMath.mag(ecc)), 135, 6 * y);
    g2.drawString("aoa:  " + String.format("%13.4f", aoa), 135, 7 * (y));
  }

  public String toString() {
    return pageName;
  }

  @Override
  public void initPage() {
//		List<MouseListener> listeners  = Arrays.asList(computer.getInstCanvas().getMouseListeners());
//		computer.getInstCanvas().r

  }
}
