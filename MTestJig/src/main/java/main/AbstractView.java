package main;

import Foundation.Utils;
import VMath.VMath;
import orbits.Abstract3DModelObject;
import orbits.CoordSys;
import orbits.World3DContainer;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public abstract class AbstractView<T extends Abstract3DModelObject> extends JPanel implements Updateable {

  protected Dimension d;
  protected CoordSys viewingCoordSys;
  private ArrayWrapper<T> drawableList = new ArrayWrapper<T>(14000);
  private double[] objectVectorFromView;
  private double distanceDrawingFactorForView;

  //public abstract void setViewCanvas(RenderingCanvas pilotViewCanvas);
  public abstract RenderingCanvas getViewCanvas();

  public abstract CoordSys getCoordSys();

  public abstract int getDetailLevel();

  /*--------------------------------------------------------------------------*/
  public AbstractView() {
    super();
  }

  public void setD(Dimension d) {
    this.d = d;
  }

  public Dimension getD() {
    return d;
  }


  public void setViewingCoordSys(CoordSys sys) {
    this.viewingCoordSys = sys;
  }

  @Override
  public void update() {

    //Graphics2D g2 = getViewCanvas().getG2();
    //T[] array = (T[]) World3DContainer.getInstance().getItemsAsArray();
    Collection<T> items = (Collection<T>) World3DContainer.getInstance().getItems();

    drawableList.clear();

    //Arrays.sort(array);
    //for(int i = 0;i<array.length;i++) {
   //long t0 = System.nanoTime();
    for (T item : items) {
      distanceDrawingFactorForView = 0;
      objectVectorFromView = VMath.vecSubtract(item.getCoordSys().getPositionVec(), getCoordSys().getPositionVec());
      item.set3DObjectForDraw(this);
    }
    //System.out.println("dt= "+(System.nanoTime() - t0));

    if(drawableList.size() < 100) Utils.sleep(4);
    World3DContainer.getInstance().getBackground().draw(this);
    //Utils.sleep(0, 1000);

    //long t1 = new Date().getTime();
    //****  This sort is proper but more time consuming that Arrays.sort above
    //Collections.sort(drawableList);
    //****
    //System.out.println(drawableList.size());

    drawableList.sort();
    for (int i = 0; i < drawableList.size(); i++) {
      drawableList.get(i).draw(this);
    }

    //System.out.println("%%%% "+(new Date().getTime()-t1));
    getViewCanvas().repaint();
    //System.out.println(drawableList.size());
  }


  public ArrayWrapper<T> getDrawableList() {
    return drawableList;
  }

  public double[] getObjectVectorFromView() {
    return objectVectorFromView;
  }

  public double getDistanceDrawingFactorForView() {
    return distanceDrawingFactorForView;
  }


  public void setDistanceDrawingFactorForView(double distanceDrawingFactorForView) {
    this.distanceDrawingFactorForView = distanceDrawingFactorForView;
  }
}
