package gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.DefaultButtonModel;

import main.Command;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class GlassButton extends ComputerAbstractButton implements MouseMotionListener {

  private BufferedImage buttonImage = new BufferedImage(12, 12, BufferedImage.TYPE_4BYTE_ABGR_PRE);
  private Color stringColor;
  private int xSize, ySize;
  private Color mouseOverColor = Color.gray;
  private Color paintedColor = Color.black;
  private Logger log = Logger.getLogger(GlassButton.class);
  private Rectangle boundingRectangle;

  public GlassButton(String s, int x, int y, int xSize, int ySize, Color aColor, Command cmd) {
    super(cmd, x, y);
    buttonImage = new BufferedImage(xSize, ySize, BufferedImage.TYPE_4BYTE_ABGR_PRE);
    setText(s);
    stringColor = aColor;
    this.xSize = xSize;
    this.ySize = ySize;
    Graphics2D g2 = buttonImage.createGraphics();
    g2.setColor(aColor);
    Font font = g2.getFont();
    g2.setFont(new Font(font.getName(), xSize - 3, ySize - 3));
    g2.drawString(s, 3, 9);
    g2.setFont(font);
    setBounds(xPos, yPos, xSize, ySize);
    setModel(new DefaultButtonModel());
    addMouseMotionListener(this);
    boundingRectangle = new Rectangle(xPos, yPos, xSize, ySize);
  }

  public void paint(Graphics g) {
    try {
      paintComponent(g);
    } catch (Exception e) {
    }
  }

  public void paintComponent(Graphics g) {
    if (g == null) return;
    Graphics2D g2 = (Graphics2D) g;
    Color c = isSelected() ? mouseOverColor : Color.black;
    
    g2.setColor(c);

    g2.fillRect(xPos, yPos, xSize, ySize);
    g2.setColor(Color.WHITE);
    g2.draw(boundingRectangle);

    g2.setColor(stringColor);
    g2.drawImage(buttonImage, xPos, yPos, null);

    super.paintComponent(g);
  }

  /**
   * @param mouseOverColor the mouseOverColor to set
   */
  public void setMouseOverColor(Color mouseOverColor) {
    this.mouseOverColor = mouseOverColor;
  }

  /**
   * @return the mouseOverColor
   */
  public Color getMouseOverColor() {
    return mouseOverColor;
  }

  /**
   * @param paintedColor the paintedColor to set
   */
  public void setPaintedColor(Color paintedColor) {
    this.paintedColor = paintedColor;
  }

  /**
   * @return the paintedColor
   */
  public Color getPaintedColor() {
    return paintedColor;
  }

  @Override
  public void mouseDragged(MouseEvent arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseMoved(MouseEvent arg0) {
    Point p = arg0.getPoint();
    //log.debug(p.x+" "+p.y);

  }

}
