package enums;

public enum AxisType {
X(new float[]{1,0,0,0}),Y(new float[]{0,1,0,0}),
Z(new float[]{0,0,1,0}),W(new float[]{0,0,0,1});

	float[] unitVector;
	
	private AxisType(float[] vec) {
		unitVector = vec;
	}
	
	public float[] getUnitVector1(){
		return unitVector;
	}
	
	public double[] getUnitVector(){
		return new double[]{unitVector[0],unitVector[1],unitVector[2],unitVector[3]};
	}
}
