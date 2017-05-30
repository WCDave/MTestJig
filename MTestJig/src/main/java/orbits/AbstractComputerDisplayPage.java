package orbits;

import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Map;

import enums.OrbitElementKeys;
import gui.ComputerAbstractButton;

public abstract class AbstractComputerDisplayPage {
  protected Map<OrbitElementKeys, Object> orbitElements;
  protected NavComputer computer;
  protected MouseListener listener;
  public static int pageCount = 0;
  private List<ComputerAbstractButton> buttonList;

  public AbstractComputerDisplayPage(NavComputer computer) {
    orbitElements = computer.getOrbitElements();
    this.computer = computer;
    pageCount++;
  }

  public abstract void drawPage(Graphics2D g2);

  public abstract void initPage();

  public static int getPageCount() {
    return pageCount;
  }

  /**
   * @param buttonList the buttonList to set
   */
  protected void setButtonList(List<ComputerAbstractButton> buttonList) {
    this.buttonList = buttonList;
  }

  /**
   * @return the buttonList
   */
  protected List<ComputerAbstractButton> getButtonList() {
    return buttonList;
  }

}
