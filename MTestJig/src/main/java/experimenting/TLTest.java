package experimenting;

public class TLTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    TestThread t1 = new TestThread();
    t1.setParentThread(true);
    t1.setName("Parent");
    t1.start();
  }

}
