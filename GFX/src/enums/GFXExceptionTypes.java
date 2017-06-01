package enums;

public enum GFXExceptionTypes {
	SIZE("Error: Container dimensions are invalid: "),
	INVALID_PLANE_SIZE("Error: Invalid plane size:  "),
	CONTAINER_NULL("Error: Container cannot be null"),
	INVALID_FACET_DEFINITION("Error: Invalid Facet defintion"),
	IMPROPER_3D_OBJECT_DEF("Error: 3D Object has null CoordSys object");
	
	private String msg;
	
	private GFXExceptionTypes(String msg){
		this.msg=msg;
	}
	/**
	 * Gets exception message
	 * @return exception message
	 */
	public String getExceptionMessage(){
		return msg;
	}
}
