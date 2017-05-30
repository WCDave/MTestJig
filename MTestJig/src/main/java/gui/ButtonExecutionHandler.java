package gui;

import Foundation.Utils;

import javax.swing.*;

/**
 * Created by DaveMain on 5/14/2017.
 */
public class ButtonExecutionHandler implements IButtonExecutionHandler {


  @Override
  public void click(AbstractButton button, float delaySeconds) {
    if (button instanceof GlassButton) {
      click((GlassButton) button, delaySeconds);
    }
    if(button instanceof JRadioButton) {
      click((JRadioButton) button);
    }
  }


  void click(GlassButton gb, float delaySeconds) {
    gb.setSelected(true);
    gb.doClick();
    Utils.sleep((long) (delaySeconds * 1000));
    if(gb.getGroup() == null) {
      gb.setSelected(false);
    }
  }

  void click(JRadioButton button) {
     button.doClick();
  }

}
