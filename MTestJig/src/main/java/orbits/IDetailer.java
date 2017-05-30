package orbits;


import main.AbstractView;

public interface IDetailer<T> {

  void createDetails(T t, AbstractView view);
}
