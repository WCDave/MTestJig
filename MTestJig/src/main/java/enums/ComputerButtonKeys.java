package enums;

public enum ComputerButtonKeys {

  ANTINORMAL("A"), NORMAL("N"), RETROGRADE("R"), PROGRADE("P"), AUTOLAUNCH("AP Lnch");

  private String keyLabel;

  private ComputerButtonKeys(String label) {
    keyLabel = label;
  }

  @Override
  public String toString() {
    return keyLabel;
  }
}
