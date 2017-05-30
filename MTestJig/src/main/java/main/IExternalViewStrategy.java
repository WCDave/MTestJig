package main;

public interface IExternalViewStrategy<T extends AbstractView> {

  void applyToView(T view);
  void reset();
}
