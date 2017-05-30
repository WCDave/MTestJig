package main;

import org.apache.commons.math3.util.FastMath;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class PlanetColorModel implements ColorModel {

  private Color minColor;
  private Color maxColor;
  private BufferedImage image;

  public PlanetColorModel(Color min, Color max, BufferedImage image) {
    this.maxColor = max;
    this.minColor = min;
    this.image = image;
  }

  @Override
  public Color getShadeForLightSourceFactor(Color color, float dotProduct) {
    float dp = 0;
    if (color == null) {
      return Color.BLACK;
    }
    if (dotProduct < 0) {
      dp = 1f - (float) Math.exp(7 * dotProduct);
    }
    float maxRed = color.getRed();
    float maxGreen = color.getGreen();
    float maxBlue = color.getBlue();
    int newRed = FastMath.max((int) (maxRed * dp), 35);
    int newGreen = FastMath.max((int) (maxGreen * dp), 35);
    int newBlue = Math.max((int) (maxBlue * dp), 35);
    return new Color(newRed, newGreen, newBlue);

  }

  @Override
  public BufferedImage getModelImage() {
    return image;
  }
}
