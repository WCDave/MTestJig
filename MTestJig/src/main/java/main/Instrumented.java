package main;

public interface Instrumented<T> {
  T[] getInstruments();

  void drawHud();
}
