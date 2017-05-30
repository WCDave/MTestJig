package main;

import java.awt.*;

public interface IDrawingVisitor<T> {

  Color visit(T t);

  void visit(T t, AbstractView view);

}
