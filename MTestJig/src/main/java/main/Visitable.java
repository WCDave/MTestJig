package main;

import java.awt.Color;

public interface Visitable {

  Color accept(IDrawingVisitor v);

}
