package main;

import gfxmain.GFXException;
import gfxmain.GFXFramework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JFrame;

public class MainTest extends JFrame {
    private GFXFramework gfx;
    private final static int SCREEN_WIDTH=800;
    private final static int SCREEN_HEIGHT=800;
    private Graphics2D g2;
    int x=0;
    int y=0;
    
    public MainTest(){
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setLayout(null);
    	setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
    	try {
			gfx=new GFXFramework(this,true);
		} catch (GFXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     	
    	getContentPane().add(gfx); 
    	pack();
    	gfx.setVisible(true);
    	gfx.setBackground(Color.BLACK);
    	gfx.init();    	
        setVisible(true); 
        g2=gfx.getG2();
    }
    public static void main(String[] args){
    	new MainTest().simLoop(); 
    }
    
    private void simLoop(){
       
       while(true){
    	   render();
    	   gfx.update();
    	   sleepABit();
       }
    }
    private void render(){
    	g2.setColor(Color.RED);    	
	    switch (gfx.getKeyPadCommand()){
	    	case LEFT : x--;break;
	    	case RIGHT: x++;break;
	    	case UP   : y--;break;
	    	case DOWN : y++;break;    	
    	}
    	g2.fillRect(x,y,25,25);
    }
    
    private void sleepABit(){
    	try {
			Thread.sleep(1);
		} catch (InterruptedException e) {}
    }
}

