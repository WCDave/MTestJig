package gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;

public class JButtonGroup<T extends ComputerAbstractButton> {

  private List<AbstractButton> buttonList = new ArrayList<AbstractButton>();
  private boolean allowsZeroSelect;
  private boolean handleInProgress;

  public JButtonGroup(boolean allowsZeroSelect) {
    this.allowsZeroSelect = allowsZeroSelect;
  }

  public void add(T a) {
    buttonList.add(a);
    a.setGroup(this);
  }

  public boolean contains(Object o) {
    return buttonList.contains(o);
  }

  public boolean isAllowsZeroSelect() {
    return allowsZeroSelect;
  }

  public void handleButtonClick(T button) {
    if (!handleInProgress) {
      handleInProgress = true;
      for (AbstractButton b : buttonList) {
        if (b != button) {
          if (button.isSelected()) {
            b.setSelected(false);
            b.doClick();
          } else {
            button.setSelected(!allowsZeroSelect);
          }
        }
      }
    }
    handleInProgress = false;
  }
}
