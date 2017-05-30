package orbits;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public interface Composed3DObject<T> {
  List<T> getFaceList();

  IDetailer<T> getDetailer();
}
