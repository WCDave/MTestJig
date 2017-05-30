package orbits;


public interface ICachingDetailer<T> extends IDetailer<T> {
  void flushCache();
}
