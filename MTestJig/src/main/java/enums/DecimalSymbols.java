package enums;

public enum DecimalSymbols {
  ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), ZERO("0"), DECIMAL_POINT("."), MINUS("-"),SEPARATOR(":");
  private String value;

  private DecimalSymbols(String value) {
    this.value = value;
  }

  public String toString() {
    return "" + value;
  }

  public int getIntValue() {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return -1;
    }
  }
}
