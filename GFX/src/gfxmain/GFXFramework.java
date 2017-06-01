package gfxmain;

import enums.GFXExceptionTypes;
import enums.KeyPadArrows;
import enums.MouseStates;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.RootPaneContainer;
import javax.swing.event.MouseInputAdapter;


public class GFXFramework extends Canvas {
	private int key;
	private boolean newKeyPressed;
	private BufferStrategy bs;
	private boolean numLockOn;
	private boolean animationOn;
	private BufferedImage animBackgroundImg;
	private BufferedImage saveImage;
	private Graphics2D backG2;
	private Color backgroundColor=Color.BLACK;
	private MouseStates mouseState=MouseStates.NONE;
	private Component parentComponent;
	
	/**
	 * Creates Graphics Framework Object
	 * @param c Container to which to add this Graphics Framework
	 * @param animation animation ON/OFF
	 * @throws GFXException thrown if Container c has invalid size or Container c is null
	 */
	public GFXFramework(Container c, boolean animation) throws GFXException {
		super();
		if(c==null)
		{
			throw new GFXException(null, GFXExceptionTypes.CONTAINER_NULL);
		}
		parentComponent=c;
		if(c.getPreferredSize().width == 0)
		{
			throw new GFXException(c, GFXExceptionTypes.SIZE);
		}
		addKeyListener(new UserKeyInputAdapter());
		UserMouseAdapter uma = new UserMouseAdapter();
		addMouseListener(uma);	
		//c.addKeyListener(new UserKeyInputAdapter());
		addMouseMotionListener(uma); 
		setSize(c.getPreferredSize());
		backgroundColor = c.getBackground();
		animBackgroundImg = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
		backG2=(Graphics2D)animBackgroundImg.getGraphics();
		numLockOn=Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK);
		animationOn=animation;
		if(c instanceof RootPaneContainer) 
		{
			((RootPaneContainer)c).getContentPane().add(this);
		}
		else 
		{
			c.add(this);
		}
	}

	
	public KeyPadArrows getKeyPadCommand() {
		newKeyPressed=false;
		return KeyPadArrows.getDirection(key);
	}
	
	/**
	 * Initialize the Graphics Framework.  Must be run after instantiation and before Graphics Framework
	 * Object is used
	 */
	public void init(){
		if(parentComponent instanceof Window)
		{
			((Window)parentComponent).pack();
		}
		parentComponent.setVisible(true);
		setVisible(true);
		createBufferStrategy(2);
	    bs=getBufferStrategy();
	    setVisible(true);
	}
	
	/**
	 * Get Graphics2D object
	 * @return Graphics2D object for drawing
	 */
	public Graphics2D getG2(){
		return backG2;
	}
	
	/*
	 * 
	 * <Phyx-> well i can't think of any direct way to do this, 
	 * what i can think of requires a small detour. Create a buffered image same size 
	 * and image type as the environment, (or TYPE_INT_ARGB) and from that you create a 
	 * graphics2D object. you then always draw using that graphics2D and the image will 
	 * have the content. you can do with it what you want. to also get it on the 
	 * bufferedStrategy you paint the image as a whole on to the screen
	 * <Phyx-> you can save time and memory by not recreating the image everytime, just create it once and reuse it and it's graphics
      just a speed tip, if you're clearing the background on every frame with a color. consider using setBackground and clearRect
      instead of fillRect
      it's exponentially faster
	 */
	
	/**
	 * Update display
	 */
	public void update(){	
		Graphics2D g2=(Graphics2D)bs.getDrawGraphics();
		g2.drawImage(animBackgroundImg, 0, 0, this);
		if(animationOn)
		{				
			if(saveImage==null)
			{				
				backG2.setBackground(backgroundColor);
				backG2.clearRect(0, 0, getWidth(), getHeight());
			}
			else 
			{
				backG2.drawImage(saveImage, 0, 0, this);
			}
		}
		bs.show();		
	}

	public boolean isNewKeyPressed() {
		return newKeyPressed;
	}

	public boolean isNumLockOn() {
		return numLockOn;
	}

	private class UserKeyInputAdapter extends KeyAdapter {
		private boolean shiftKeySelected;
		public void keyPressed(KeyEvent e){
			newKeyPressed=true;
			shiftKeySelected = e.isShiftDown();
			
			key=e.getKeyCode();			
			
			if(key==KeyEvent.VK_NUM_LOCK)
				numLockOn=Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK);
			
		}
		public void keyReleased(KeyEvent e){
			key=0;
		}
		
		/**
		 * @return the shiftKeySelected
		 */
		public boolean isShiftKeySelected() {
			boolean temp = shiftKeySelected;
			shiftKeySelected = false;
			return temp;
		}
	}
	
	/**
	 * Returns 0 or key pressed by user
	 * 
	 * @return key pressed by user
	 */
	
	public boolean isShiftKeySelected()
	{
		return ((UserKeyInputAdapter)this.getKeyListeners()[0]).isShiftKeySelected();
	}
	public int getKey(){
		return key;
	}
	private class UserMouseAdapter extends MouseInputAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			mouseState=MouseStates.CLICKED;
			mouseState.setMousePositionStatus(e.getX(), e.getY(),true);
			super.mouseClicked(e);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			mouseState=MouseStates.DRAGGED;
			mouseState.setMousePositionStatus(e.getX(), e.getY(),true);
			super.mouseDragged(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			mouseState=MouseStates.ENTERED;
			mouseState.setMousePositionStatus(e.getX(), e.getY(),true);
			super.mouseEntered(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			mouseState=MouseStates.EXITED;
			mouseState.setMousePositionStatus(e.getX(), e.getY(),true);
			super.mouseExited(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mouseState=MouseStates.MOVED;
			mouseState.setMousePositionStatus(e.getX(), e.getY(),true);
			super.mouseMoved(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			mouseState=MouseStates.PRESSED;
			mouseState.setMousePositionStatus(e.getX(), e.getY(),true);
			super.mousePressed(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mouseState=MouseStates.RELEASED;
			mouseState.setMousePositionStatus(e.getX(), e.getY(),true);
			super.mouseReleased(e);
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			//System.out.println("W moved"+animBackgroundImg);
			super.mouseWheelMoved(e);
		}
		
	}
	
	public void setAnimateOnImage(BufferedImage bi){
		animBackgroundImg=bi;
		animationOn=true;
	}
	
	public void setAnimate(boolean animate) {
		this.animationOn = animate;
	}

	/**
	 * returns true if animation is on
	 * @return animation flag
	 */
	public boolean isAnimationOn() {
		return animationOn;
	}

	/**
	 * Saves currently displayed image for use as a background
	 */
	public void setSaveImage() {
		saveImage = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
		saveImage.getGraphics().drawImage(animBackgroundImg, 0, 0, null);
	}
	
	/**
	 * Unset background image
	 */
	public void clearSaveImage(){
		saveImage=null;
	}
	
	public MouseStates getMouseState(){
		return mouseState;
	}
	
	/**
	 * Sleep for i milliseconds
	 * @param i milliseconds to sleep
	 */
	public static void sleep(int i){
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {}
	}


	/**
	 * Set background color
	 * @param backgroundColor background color used when  "Save Image" is not set
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}


	/**
	 * Retrieve background color
	 * @return Color used in background drawing
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
}
