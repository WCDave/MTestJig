package specFx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;

import javax.swing.JFrame;

public class GradPaint extends JFrame {
	
	GradientPaint acyclic;

	public static void main(String[] args) {
		GradPaint gp = new GradPaint();
		gp.setSize(new Dimension(200,200));
		gp.setPreferredSize(gp.getSize());
		gp.pack();
		gp.setVisible(true);
		gp.repaint();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public GradPaint(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.black);
	}
	
	
	@Override
	public void paint(Graphics g ){
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.black);
		g2.fillRect(0,0,this.getWidth(), this.getHeight());
		//System.out.println(Color.red.getRed()+" "+Color.red.getGreen()+" "+Color.red.getBlue());
		acyclic = new GradientPaint (10F, 50F, new Color(1,0,0,0.9f),
                20F, 90F, new Color(0,1,0,0.1f));
		g2.setPaint(acyclic);
		//g2.setColor(new Color(1,0,0,0.1f));
		AffineTransform at = new AffineTransform();
		at.setToRotation(Math.toRadians(40.0),25,40);
		g2.setTransform(at);
		g2.fillOval(80,40,50,80);
		int t = acyclic.getTransparency();
		System.out.println(t);
		int i = Transparency.OPAQUE;
		i = Transparency.TRANSLUCENT;
		i = Transparency.BITMASK;
	}

}
