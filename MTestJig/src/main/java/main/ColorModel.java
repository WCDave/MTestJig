package main;

import java.awt.Color;
import java.awt.image.BufferedImage;

public interface ColorModel {

  Color getShadeForLightSourceFactor(Color color, float lightSourceFactor);

  BufferedImage getModelImage();

  ColorModel DEFAULT_COLOR_MODEL = new ColorModel() {
    @Override
    public Color getShadeForLightSourceFactor(Color color, float lightSourceFactor) {
      return null;
    }

    @Override
    public BufferedImage getModelImage() {
      return null;
    }
  };
}
