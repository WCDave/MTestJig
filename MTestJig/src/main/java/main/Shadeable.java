package main;


public interface Shadeable {

  IDrawingVisitor getShadingVisitor();

  void setShadingVisitor(IDrawingVisitor v);

  ColorModel getColorModel();
}
