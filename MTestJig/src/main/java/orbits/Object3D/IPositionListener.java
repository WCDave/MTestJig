package orbits.Object3D;


import main.AbstractView;
import orbits.Abstract3DModelObject;

public interface IPositionListener<T extends Abstract3DModelObject> {

  void register(T t);

  void checkDistance(AbstractView view);

  enum RebuildFactor {
    RADIIDEFAULT(1, 4000), RADII20(20, 1279), RADII40(40, 319), RADII60(60, 79), RADII100(100, 19);
    private int radii;
    private int factor;

    RebuildFactor(int radii, int factor) {
      this.radii = radii;
      this.factor = factor;
    }

    public static RebuildFactor getFactorFor(int initialRadiiMultiple) {
      if (initialRadiiMultiple > RADII20.radii && initialRadiiMultiple < RADII40.radii)
        return RADII20;
      if (initialRadiiMultiple > RADII40.radii && initialRadiiMultiple < RADII60.radii)
        return RADII40;
      if (initialRadiiMultiple > RADII60.radii && initialRadiiMultiple < RADII100.radii)
        return RADII60;
      if (initialRadiiMultiple > RADII100.radii)
        return RADII100;
      return RADIIDEFAULT;

    }

    public int getFactor() {
      return factor;
    }

    public static int firstLevelFactorThreshold() {
      return RADII20.getFactor() + 1;
    }

  }
}
