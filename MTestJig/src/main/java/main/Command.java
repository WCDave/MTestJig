package main;

public interface Command {
  double alignError = .0002;

  void execute();

  void cancel();
}
