package threeD;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class World3DContainer implements Observer {
	
	private HashMap<String,Abstract3DModelObject> itemList;
	private Abstract3DModelObject liteEmitter;
	private static World3DContainer instance;
	
	private World3DContainer(){
		itemList=new HashMap<String,Abstract3DModelObject>(20);
	}
	
	public static World3DContainer getInstance(){
		if(instance==null) {
			instance = new World3DContainer();
		}
		return instance;
	}
	
	public Abstract3DModelObject getItem(String key){
		Abstract3DModelObject res=itemList.get(key);
		return res;		
	}

	public void setLiteEmitter(Abstract3DModelObject liteEmitter) {
		this.liteEmitter = liteEmitter;
	}

	public Abstract3DModelObject getLiteEmitter() {
		return liteEmitter;
	}
	
	public void addItemsAsArray(Abstract3DModelObject[] o){
		for(int i=0;i<o.length;i++){
			itemList.put(o[i].name,o[i]);
		}
	}
	
	public void add3DObject(Abstract3DModelObject o) {
		itemList.put(o.name, o);
	}
	
	
	public boolean hasLiteEmitter(){
		return liteEmitter!=null;
	}

	public void update(Observable arg0, Object arg1) {
		CoordSys viewSys = (CoordSys)arg0;
		Object[] array = itemList.values().toArray();
		for(int i=0;i<array.length;i++) {
			Abstract3DModelObject obj3D = (Abstract3DModelObject)array[i];
			obj3D.setVectorFromView(VMath.VMath.vecSubtract(obj3D.sSys.getPositionVec(), viewSys.getPositionVec()));
		}
		
	}
}
