package orbits.movement;


public interface Moveable {
  void executeMovement(double dt);

  void initMoveable(double t);

  void setMovements(Movement[] moves);

  void setInitMovements(Movement[] moves);

}
