package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import main.Command;

public abstract class ComputerAbstractButton extends AbstractButton implements ActionListener {

  protected JButtonGroup group;
  protected Command command;
  protected int xPos;
  protected int yPos;

  public ComputerAbstractButton(Command cmd, int xpos, int ypos) {
    super();
    this.xPos = xpos;
    this.yPos = ypos;
    addActionListener(this);
    command = cmd;
  }

  public JButtonGroup getGroup() {
    return group;
  }

  public void setGroup(JButtonGroup group) {
    this.group = group;
  }

  public void doClick() {
    //this.setSelected(!this.isSelected());
    super.doClick();
  }

  public void actionPerformed(ActionEvent arg0) {
    if (command != null) {
      if (group != null) {
        group.handleButtonClick(this);
      }
      if (isSelected()) {
        command.execute();
      } else {
        command.cancel();
      }

    }
  }
}
