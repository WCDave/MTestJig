package gfxmain;

import java.awt.Container;
import java.awt.Dimension;

import enums.GFXExceptionTypes;

public class GFXException extends Exception {
	
	private Container container;
	private GFXExceptionTypes type;

	public GFXException(Container c, GFXExceptionTypes t){
		type=t;
		container = c;
	}
	
	public Dimension getContainerDimension(){
		return container.getPreferredSize();
	}
	
	@Override
	public String getMessage(){
		StringBuffer msg=new StringBuffer(type.getExceptionMessage());
		switch(type){
			case SIZE : msg.append(getContainerDimension().width+
				" "+ getContainerDimension().height);break;
		}
		return msg.toString();
	}
}
