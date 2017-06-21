package main.externalView.keyResponder;


public interface IKeyResponder<T> {

  void respondToKeyPressed(T t);

  void respondToKeyReleased(T t);
}
