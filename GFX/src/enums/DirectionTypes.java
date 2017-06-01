package enums;

public enum DirectionTypes {
FORWARD("AFT","DOWN"), AFT("LEFT","FORWARD"), LEFT("RIGHT","AFT"), RIGHT("UP","LEFT"), UP("DOWN","RIGHT"), DOWN("FORWARD","UP");

private String next;
private String previous;


	private DirectionTypes(String next, String previous)
	{
		this.next = next;
		this.previous = previous;
	}
	
	public DirectionTypes getNext()
	{
		return DirectionTypes.valueOf(next);
	}
	
	public DirectionTypes getPrevious()
	{
		return DirectionTypes.valueOf(previous);
	}
}
