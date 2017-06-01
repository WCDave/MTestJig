package VMath;

public final class VMath {
    private static final double R2D_Conversion = 180/Math.PI;
	public static float mag(float vector[]) {
		if(vector == null) return 0;
		float magnitude = 0;
		int vecLength=vector.length;
		for (int i=0;i<vecLength;i++){
			magnitude = magnitude+vector[i]*vector[i];}
		return (float)Math.sqrt(magnitude);
	}
	public static double mag(double vector[]) {
		if(vector == null) return 0;
		double magnitude = 0;
		int vecLength=vector.length;
		for (int i=0;i<vecLength;i++){
			magnitude = magnitude+vector[i]*vector[i];}
		return Math.sqrt(magnitude);
	}
	
	public static float [] normalize(float vec[]) {
		int vecLength=vec.length;
		float vecr[] = new float[vecLength];
		final float tmag = mag(vec);
		if(tmag!=0){
		    for (int i=0;i<vecLength;i++) 
			    vecr[i]=vec[i]/tmag;
		}
		return vecr;
	}

	public static double [] normalize(double vec[]) {
		int vecLength=vec.length;
		double vecr[] = new double[vecLength];
		final double tmag = mag(vec);
		if(tmag!=0){
		    for (int i=0;i<vecLength;i++) 
			    vecr[i]=vec[i]/tmag;
		}
		return vecr;
	}
	
	public static float dotprod(float vec1[], float vec2[]) {
		float dumb=0;
		int vecLength=vec1.length;
		for (int i=0;i<vecLength;i++ ){
			dumb += vec1[i]*vec2[i];}
		return dumb;
	}
	public static double dotprod(double vec1[], double vec2[]) {
		double dumb=0;
		int vecLength=vec1.length;
		for (int i=0;i<vecLength;i++ ){
			dumb += vec1[i]*vec2[i];}
		return dumb;
	}
	
	public static float [] crossprd(float vec1[], float vec2[]) {
		float vecr[] = {0, 0, 0, 0};
		vecr[0]=vec1[1]*vec2[2]-vec1[2]*vec2[1];
		vecr[1]=vec1[2]*vec2[0]-vec1[0]*vec2[2];
		vecr[2]=vec1[0]*vec2[1]-vec1[1]*vec2[0];
		return vecr;
	}
	public static double [] crossprd(double vec1[], double vec2[]) {
		double vecr[] = {0, 0, 0, 0};
		vecr[0]=vec1[1]*vec2[2]-vec1[2]*vec2[1];
		vecr[1]=vec1[2]*vec2[0]-vec1[0]*vec2[2];
		vecr[2]=vec1[0]*vec2[1]-vec1[1]*vec2[0];
		return vecr;
	}
	
	public static double modulo(double arg1,double arg2){
		double aValue = arg1/arg2;
		aValue = aValue - (long)(aValue);
		aValue = aValue*arg2;
		return aValue;
	}
	public static float[] rotate2D(float anInputVec[],double anAngle){
		float aVec [] = new float[2];
		/*x2 = x1 cos w - y1 sin w
		 y2 = x1 sin w + y1 cos w
		 z2 = z1*/
		aVec[0]=(float)(anInputVec[0]*Math.cos(anAngle)-anInputVec[1]*Math.sin(anAngle));
		aVec[1]=(float)(anInputVec[0]*Math.sin(anAngle)+anInputVec[1]*Math.cos(anAngle));
		return aVec;
	}
	public static double[] rotate2D(double anInputVec[],double anAngle){
		double aVec [] = new double[2];
		/*x2 = x1 cos w - y1 sin w
		 y2 = x1 sin w + y1 cos w
		 z2 = z1*/
		aVec[0]=(anInputVec[0]*Math.cos(anAngle)-anInputVec[1]*Math.sin(anAngle));
		aVec[1]=(anInputVec[0]*Math.sin(anAngle)+anInputVec[1]*Math.cos(anAngle));
		return aVec;
	}
	
	public static float[] transform(float[] inVec,float[][] aMatrix){
		float [] aVec = {0,0,0};
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
				aVec[i]+=inVec[j]*aMatrix[j][i];
		return aVec;
	}
	public static double[] transform(double[] inVec,double[][] aMatrix){
		double [] aVec = {0,0,0};
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
				aVec[i]+=inVec[j]*aMatrix[j][i];
		return aVec;
	}
	
	public static float[] vecAdd(float [] a, float[] b){
		float[] aResult={a[0]+b[0],a[1]+b[1],a[2]+b[2],0};
		return aResult;
	}
	public static double[] vecAdd(double [] a, double[] b){
		double[] aResult={a[0]+b[0],a[1]+b[1],a[2]+b[2],0};
		return aResult;
	}
	
	public static float[] vecSubtract(float[] a, float[] b){
		float[] aResult = new float[a.length];
		for(int i=0;i<a.length;i++){
		   aResult[i] = a[i]-b[i];
		}
		return aResult;
	}
	public static double[] vecSubtract(double[] a, double[] b){
		double[] aResult = new double[a.length];
		for(int i=0;i<a.length;i++){
		   aResult[i] = a[i]-b[i];
		}
		return aResult;
	}
	
	public static float[] vecMultByScalar(float[] aVec, float aScalar){
		float[] aResult = new float[aVec.length];
		for(int i=0;i<aVec.length;i++)
			aResult[i]=aVec[i]*aScalar;
		return aResult;
	}
	public static double[] vecMultByScalar(double[] aVec, double aScalar){
		double[] aResult = new double[aVec.length];
		for(int i=0;i<aVec.length;i++)
			aResult[i]=aVec[i]*aScalar;
		return aResult;
	}
	public static float[] vecMultByScalar(float[] aVec, double aScalar){
		float[] aResult = new float[aVec.length];
		for(int i=0;i<aVec.length;i++)
			aResult[i]=(float)(aVec[i]*aScalar);
		return aResult;
	}	
	
	public static float[] reflectionOf(float[] inVec, float[] normalVec){
		//x=V-2*(V dot n)*n
		float[] aResult = new float[inVec.length];
		float aTerm = 2*dotprod(inVec,normalVec);
		aResult = vecMultByScalar(normalVec,aTerm);
		aResult = vecSubtract(inVec,aResult);
		return aResult;
	}
	
	public static double[][] transposeMatrix(double[][] mat)
	{		
		double[][] resultMat = new double[3][3];
		for(int i = 0;i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				resultMat[j][i] = mat[i][j];
			}
		}
		return resultMat;
	}
	
	public static boolean vecsAreEqual(double[] a, double[] b)
	{
		return 		a[0] == b[0] 
				&&  a[1] == b[1]
				&&  a[2] == b[2];		
	}

    public static float R2D(float radianAngle) {
         return (float) (radianAngle * R2D_Conversion);
    }

    public static double R2D(double radianAngle) {
        return  (radianAngle * R2D_Conversion);
    }
}
