package experimenting;

import main.ADIInstrument;
import main.AbstractInstrument;

public class TestClass<I> implements ITestInterface<I> {


  public static void main(String[] args) {
    TestClass<AbstractInstrument> x = new TestClass<AbstractInstrument>();
    x.test(new ADIInstrument(null));
    char[][] board = new char[][]{{'s', 'r', 'g', 't'}, {'c', 'a', 'r', 'e'}, {'j', 'a', 'f', 'g'}, {'f', 'h', 'a', 't'}};
    int h = 0;
  }

  @Override
  public <T extends I> void test(T x) {

    int h = 0;

  }

}
