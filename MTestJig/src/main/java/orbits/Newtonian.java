package orbits;

import java.util.Date;

public interface Newtonian extends Massive {

  double[] getVelocity();

  void updatePosition(Date date, double[] newPosition, double[] currentPosition);

  double[] getPosition();

}
