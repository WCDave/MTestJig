package experimenting;

public interface ITestInterface<I> {

  <T extends I> void test(T x);
}
