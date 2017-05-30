package gui;

import Foundation.Utils;
import enums.ComputerButtonKeys;
import enums.DecimalSymbols;
import experimenting.A3OSerializerDeserializer;
import experimenting.IA3OSerializerDeserializer;
import main.Command;
import main.CommandBuilder;
import orbits.NavComputer;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class GlassKeyPad extends Container implements INamedControlContainer {

  private boolean paintBorder;
  private KeyPadAdapter adapter;
  private Logger log = Logger.getLogger(GlassKeyPad.class);
  private NavComputer computer;
  private StringBuffer compositeCommand;


  public static void main(String args[]) {
    @SuppressWarnings("unused")
    GlassKeyPad k = new GlassKeyPad(new Point(13, 13), new Dimension(150, 150), null);
  }

  public GlassKeyPad(Point upperLeft, Dimension size, NavComputer computer) {
    this(new Rectangle(upperLeft, size), computer);
    //this.computer = computer;
  }

  public GlassKeyPad(Rectangle boundRectangle, NavComputer computer) {
    setBounds(boundRectangle);
    setPreferredSize(new Dimension(boundRectangle.width, boundRectangle.height));
    this.computer = computer;
    adapter = new KeyPadAdapter();
    createKeys();     
    //this.addMouseListener(adapter);
  }

  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(Color.black);
    g2.fillRect(0, 0, getWidth(), getHeight());

    for (Component c : getComponents()) {
      c.paint(g);
    }

    if (paintBorder) {
      g2.setColor(Color.white);
      g2.draw(new Rectangle(0, 0, getWidth(), getHeight()));
    }

    g2.setColor(Color.white);
    StringBuffer value = adapter.getValue();
    if (value != null) {
      g2.drawString(value.toString(), 3, getHeight() - 18);
    }

    if (compositeCommand != null) {
      g2.setColor(Color.cyan);
      String[] strings = compositeCommand.toString().split(":");
      int i = 0;
      for (String string : strings) {
        g2.drawString(string, 60, 10 + i++ * 10);
      }
    }

  }


  private void createKeys() {
    int buttonWidth, buttonHeight, horizGap, vertGap;
    buttonWidth = getBounds().width / 10;
    buttonHeight = getBounds().height / 10;
    horizGap = getBounds().width / 20;
    vertGap = getBounds().height / 20;
    Enum[] keyPadEnums = new Enum[15];
    int i = 0;
    for(Enum e : ComputerButtonKeys.values()) {
      if(e.name().contains("KP")) {
        keyPadEnums[i++] = e;
      }
    }

    i=0;
    for (final DecimalSymbols digit : DecimalSymbols.values()) {
      int x = digit.ordinal() % 3;
      int y = digit.ordinal() / 3;
      int posX = (horizGap + buttonWidth) * x + horizGap;
      int posY = (vertGap + buttonHeight) * y + vertGap;

      Command cmd = CommandBuilder.createCommand().setExecutionObject(adapter).setExecutionMethod("addValue").seteArgs(new String[]{digit.toString()}).buildCommand();
      final GlassButton gb = new GlassButton(digit.toString(), posX, posY, buttonWidth, buttonHeight, Color.white, cmd);
      gb.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
      add(gb);
    }

    Command cmd = CommandBuilder.createCommand().setExecutionObject(this).setExecutionMethod("executeCommand").buildCommand();
    GlassButton execButton = new GlassButton("X", 3, getHeight() - 13, 12, 12, Color.RED, cmd);

    cmd = CommandBuilder.createCommand().setExecutionObject(adapter).setExecutionMethod("addValue").seteArgs(new String[]{"M"}).buildCommand();
    GlassButton moonButton = new GlassButton("M", 18, getHeight() - 13, 12, 12, Color.RED, cmd);

    cmd = CommandBuilder.createCommand().setExecutionObject(adapter).setExecutionMethod("addValue").seteArgs(new String[]{"A"}).buildCommand();
    GlassButton apogeeButton = new GlassButton("A", 33, getHeight() - 13, 11, 11, Color.RED, cmd);

    cmd = CommandBuilder.createCommand().setExecutionObject(adapter).setExecutionMethod("addValue").seteArgs(new String[]{"C"}).buildCommand();
    GlassButton circularizeButton = new GlassButton("C", 48, getHeight() - 13, 11, 11, Color.RED, cmd);

    cmd = CommandBuilder.createCommand().setExecutionObject(adapter).setExecutionMethod("addValue").seteArgs(new String[]{"I"}).buildCommand();
    GlassButton planeChangeButton = new GlassButton("I", 63, getHeight() - 13, 11, 11, Color.RED, cmd);

    cmd = CommandBuilder.createCommand().setExecutionObject(this).setExecutionMethod("storeCommandFragment").buildCommand();
    GlassButton pgmButton = new GlassButton("PGM", 78, getHeight() - 13, 25, 11, Color.red, cmd);
    
    cmd = CommandBuilder.createCommand().setExecutionObject(this).setExecutionMethod("serializeA3Os").buildCommand();
    GlassButton serializeButton = new GlassButton("S", 78, getHeight() - 30, 11, 11, Color.red, cmd);

    cmd = CommandBuilder.createCommand().setExecutionObject(this).setExecutionMethod("deSerializeA3Os").buildCommand();
    GlassButton deSerializeButton = new GlassButton("D", 93, getHeight() - 30, 11, 11, Color.red, cmd);

    add(deSerializeButton);
    add(serializeButton);
    add(pgmButton);
    add(execButton);
    add(moonButton);
    add(apogeeButton);
    add(planeChangeButton);
    add(circularizeButton);
  }

  public void executeCommand() {
    computer.executeCommand(compositeCommand.toString().split(DecimalSymbols.SEPARATOR.toString()));
    compositeCommand = null;
  }

  public void serializeA3Os(){
    IA3OSerializerDeserializer serializerDeserializer = new A3OSerializerDeserializer();
    serializerDeserializer.serializeToFile("save.dat");
  }

  public void deSerializeA3Os(){
    IA3OSerializerDeserializer serializerDeserializer = new A3OSerializerDeserializer();
    serializerDeserializer.deSerializeFromFile("save.dat");
  }

  public void storeCommandFragment() {
    StringBuffer value = GlassKeyPad.this.adapter.getValue();
    if (value != null) {
      if (compositeCommand == null) {
        compositeCommand = new StringBuffer();
      }
      compositeCommand.append(value);
      GlassKeyPad.this.adapter.resetValue();
    } else {
      compositeCommand = null;
    }
  }

  /**
   * @param paintBorder the paintBorder to set
   */
  public void setPaintBorder(boolean paintBorder) {
    this.paintBorder = paintBorder;
  }

  /**
   * @return the paintBorder
   */
  public boolean isPaintBorder() {
    return paintBorder;
  }

  /**
   * @return the adapter
   */
  public KeyPadAdapter getAdapter() {
    return adapter;
  }

  @Override
  public Map<String, Component> getNamedComponents() {
    Map<String, Component> result = new HashMap<>();
    for(Component c : this.getComponents()) {
      if(c instanceof GlassButton) {
        result.put(((GlassButton)c).getText()+"GP", c);
      }              
    }
    return result;
  }


  public class KeyPadAdapter implements MouseListener {

    private boolean dataAvailable;
    private StringBuffer value;

    public StringBuffer getValue() {
      return value;
    }

    public void addValue(String string) {
      if (value == null) {
        value = new StringBuffer();
      }
      value.append(string);
      if ("X".equals(string)) {
        dataAvailable = true;
      }
    }

    public void resetValue() {
      value = null;
      dataAvailable = false;
    }


    /**
     * @return the dataAvailable
     */
    public boolean isDataAvailable() {
      return dataAvailable;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

      Object[] buttons = GlassKeyPad.this.getComponents();
      Point p = e.getPoint();
      //log.debug(buttons.length);
      for (Object o : buttons) {
        final GlassButton gb = (GlassButton) o;
        //log.debug(e.getPoint().x+" "+e.getPoint().y+":"+gb.getBounds().x+" "+gb.getBounds().y);
        if (gb.getBounds().contains(p)) {
          //System.out.println("Found"+gb.getText());
          gb.setSelected(!gb.isSelected());
          //addValue(gb.getText());
          gb.doClick();
          new Thread() {
            public void run() {
              gb.setPaintedColor(gb.getMouseOverColor());
              Utils.sleep(50);
              gb.setPaintedColor(Color.black);
              gb.setSelected(false);
            }
          }.start();
        }
      }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }


  }

}
