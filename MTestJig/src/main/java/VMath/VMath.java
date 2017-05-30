package VMath;

import orbits.CoordSys;
import org.apache.commons.math3.geometry.euclidean.threed.SphericalCoordinates;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;

public final class VMath {
  private static final double R2D_Conversion = 180 / Math.PI;

  public static float mag(float vector[]) {
    if (vector == null) return 0;
    float magnitude = 0;
    int vecLength = vector.length;
    for (int i = 0; i < vecLength; i++) {
      magnitude = magnitude + vector[i] * vector[i];
    }
    return (float) FastMath.sqrt(magnitude);
  }

  public static double mag(double vector[]) {
    //if(vector == null) return 0;
    return FastMath.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);
  }

  public static float[] normalize(float vec[]) {
    int vecLength = vec.length;
    float vecr[] = new float[vecLength];
    final float tmag = mag(vec);
    if (tmag != 0) {
      for (int i = 0; i < vecLength; i++)
        vecr[i] = vec[i] / tmag;
    }
    return vecr;
  }

  public static double[] normalize(double vec[]) {
    final double tmag = mag(vec);
    if (tmag != 0)
      return vecMultByScalar(vec, 1 / tmag);
    else
      return new double[]{0, 0, 0};
  }

  public static float dotprod(float vec1[], float vec2[]) {
    float dumb = 0;
    int vecLength = vec1.length;
    for (int i = 0; i < vecLength; i++) {
      dumb += vec1[i] * vec2[i];
    }
    return dumb;
  }

  public static double dotprod(double vec1[], double vec2[]) {
    return vec1[0] * vec2[0] + vec1[1] * vec2[1] + vec1[2] * vec2[2];
  }

  public static float[] crossprd(float vec1[], float vec2[]) {
    float vecr[] = {0, 0, 0};
    vecr[0] = vec1[1] * vec2[2] - vec1[2] * vec2[1];
    vecr[1] = vec1[2] * vec2[0] - vec1[0] * vec2[2];
    vecr[2] = vec1[0] * vec2[1] - vec1[1] * vec2[0];
    return vecr;
  }

  public static double[] crossprd(double vec1[], double vec2[]) {
    double vecr[] = {0, 0, 0};
    vecr[0] = vec1[1] * vec2[2] - vec1[2] * vec2[1];
    vecr[1] = vec1[2] * vec2[0] - vec1[0] * vec2[2];
    vecr[2] = vec1[0] * vec2[1] - vec1[1] * vec2[0];
    return vecr;
  }

  public static double modulo(double arg1, double arg2) {
    double aValue = arg1 / arg2;
    aValue = aValue - (long) (aValue);
    aValue = aValue * arg2;
    return aValue;
  }

  public static float[] rotate2D(float anInputVec[], double anAngle) {
    float aVec[] = new float[2];
    /*x2 = x1 cos w - y1 sin w
		 y2 = x1 sin w + y1 cos w
		 z2 = z1*/
    aVec[0] = (float) (anInputVec[0] * FastMath.cos(anAngle) - anInputVec[1] * FastMath.sin(anAngle));
    aVec[1] = (float) (anInputVec[0] * FastMath.sin(anAngle) + anInputVec[1] * FastMath.cos(anAngle));
    return aVec;
  }

  public static double[] rotate2D(double anInputVec[], double anAngle) {
    double aVec[] = new double[2];
		/*x2 = x1 cos w - y1 sin w
		 y2 = x1 sin w + y1 cos w
		 z2 = z1*/
    aVec[0] = (anInputVec[0] * FastMath.cos(anAngle) - anInputVec[1] * FastMath.sin(anAngle));
    aVec[1] = (anInputVec[0] * FastMath.sin(anAngle) + anInputVec[1] * FastMath.cos(anAngle));
    return aVec;
  }

  public static float[] transform(float[] inVec, float[][] aMatrix) {
    float[] aVec = {0, 0, 0};
    for (int i = 0; i < 3; i++)
      for (int j = 0; j < 3; j++)
        aVec[i] += inVec[j] * aMatrix[j][i];
    return aVec;
  }

  public static double[] transform(double[] inVec, double[][] aMatrix) {
    double[] aVec = {0, 0, 0};
    for (int i = 0; i < 3; i++)
      for (int j = 0; j < 3; j++)
        aVec[i] += inVec[j] * aMatrix[j][i];
    return aVec;
  }

  public static float[] vecAdd(float[] a, float[] b) {
    float[] aResult = {a[0] + b[0], a[1] + b[1], a[2] + b[2], 0};
    return aResult;
  }

  public static double[] vecAdd(double[] a, double[] b) {
    return new double[]{a[0] + b[0], a[1] + b[1], a[2] + b[2]};
  }

  public static float[] vecSubtract(float[] a, float[] b) {
    float[] aResult = new float[a.length];
    for (int i = 0; i < a.length; i++) {
      aResult[i] = a[i] - b[i];
    }
    return aResult;
  }

  public static double[] vecSubtract(double[] a, double[] b) {
    return new double[]{a[0] - b[0], a[1] - b[1], a[2] - b[2]};
  }

  public static float[] vecMultByScalar(float[] aVec, float aScalar) {
    float[] aResult = new float[aVec.length];
    for (int i = 0; i < aVec.length; i++)
      aResult[i] = aVec[i] * aScalar;
    return aResult;
  }

  public static double[] vecMultByScalar(double[] aVec, double aScalar) {
    return new double[]{aVec[0] * aScalar, aVec[1] * aScalar, aVec[2] * aScalar};
  }

  public static float[] vecMultByScalar(float[] aVec, double aScalar) {
    float[] aResult = new float[aVec.length];
    for (int i = 0; i < aVec.length; i++)
      aResult[i] = (float) (aVec[i] * aScalar);
    return aResult;
  }

  public static float[] reflectionOf(float[] inVec, float[] normalVec) {
    //x=V-2*(V dot n)*n
    float[] aResult = new float[inVec.length];
    float aTerm = 2 * dotprod(inVec, normalVec);
    aResult = vecMultByScalar(normalVec, aTerm);
    aResult = vecSubtract(inVec, aResult);
    return aResult;
  }

  public static double[][] transposeMatrix(double[][] mat) {
    double[][] resultMat = new double[3][3];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        resultMat[j][i] = mat[i][j];
      }
    }
    return resultMat;
  }

  public static boolean vecsAreEqual(double[] a, double[] b) {
    return a[0] == b[0]
            && a[1] == b[1]
            && a[2] == b[2];
  }

  public static float R2D(float radianAngle) {
    return (float) (radianAngle * R2D_Conversion);
  }

  public static double R2D(double radianAngle) {
    return (radianAngle * R2D_Conversion);
  }

  public static void zero3DVect(double[] vec) {
    vec[0] = 0;
    vec[1] = 0;
    vec[2] = 0;
  }

  public static double[] triangleSSS(double a, double b, double c) {
    double aa = a * a;
    double bb = b * b;
    double cc = c * c;

    double A = FastMath.acos((bb + cc - aa) / (2 * b * c));
    double B = FastMath.acos((aa + cc - bb) / (2 * a * c));
    double C = FastMath.acos((aa + bb - cc) / (2 * a * b));
    return new double[]{A, B, C};
  }

  public static double[][] multiplyMatrix(double[][] a, double[][] b) {
    if (a[0].length != b.length) {
      throw new IllegalArgumentException("# columns in A must equal # rows in B");
    }

    int rowsInA = a.length;
    int columnsInA = a[0].length; // same as rows in B
    int columnsInB = b[0].length;
    double[][] c = new double[rowsInA][columnsInB];
    for (int i = 0; i < rowsInA; i++) {
      for (int j = 0; j < columnsInB; j++) {
        for (int k = 0; k < columnsInA; k++) {
          c[i][j] = c[i][j] + a[i][k] * b[k][j];
        }
      }
    }
    return c;
  }

  public static double[][] invert(double a[][])
  {
    int n = a.length;
    double x[][] = new double[n][n];
    double b[][] = new double[n][n];
    int index[] = new int[n];
    for (int i=0; i<n; ++i)
      b[i][i] = 1;

    // Transform the matrix into an upper triangle
    gaussian(a, index);

    // Update the matrix b[i][j] with the ratios stored
    for (int i=0; i<n-1; ++i)
      for (int j=i+1; j<n; ++j)
        for (int k=0; k<n; ++k)
          b[index[j]][k]
                  -= a[index[j]][i]*b[index[i]][k];

    // Perform backward substitutions
    for (int i=0; i<n; ++i)
    {
      x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
      for (int j=n-2; j>=0; --j)
      {
        x[j][i] = b[index[j]][i];
        for (int k=j+1; k<n; ++k)
        {
          x[j][i] -= a[index[j]][k]*x[k][i];
        }
        x[j][i] /= a[index[j]][j];
      }
    }
    return x;
  }

// Method to carry out the partial-pivoting Gaussian
// elimination.  Here index[] stores pivoting order.

  public static void gaussian(double a[][], int index[])
  {
    int n = index.length;
    double c[] = new double[n];

    // Initialize the index
    for (int i=0; i<n; ++i)
      index[i] = i;

    // Find the rescaling factors, one from each row
    for (int i=0; i<n; ++i)
    {
      double c1 = 0;
      for (int j=0; j<n; ++j)
      {
        double c0 = FastMath.abs(a[i][j]);
        if (c0 > c1) c1 = c0;
      }
      c[i] = c1;
    }

    // Search the pivoting element from each column
    int k = 0;
    for (int j=0; j<n-1; ++j)
    {
      double pi1 = 0;
      for (int i=j; i<n; ++i)
      {
        double pi0 = FastMath.abs(a[index[i]][j]);
        pi0 /= c[index[i]];
        if (pi0 > pi1)
        {
          pi1 = pi0;
          k = i;
        }
      }

      // Interchange rows according to the pivoting order
      int itmp = index[j];
      index[j] = index[k];
      index[k] = itmp;
      for (int i=j+1; i<n; ++i)
      {
        double pj = a[index[i]][j]/a[index[j]][j];

        // Record pivoting ratios below the diagonal
        a[index[i]][j] = pj;

        // Modify other elements accordingly
        for (int l=j+1; l<n; ++l)
          a[index[i]][l] -= pj*a[index[j]][l];
      }
    }
  }

  public static void renormalizeCoordSys(CoordSys cs ) {
    //synchronized (cs) {
    double[] zAxis = VMath.normalize(cs.zAxis().getVectorForm());
    double[] yAxis = VMath.normalize(cs.yAxis().getVectorForm());
    double[] xAxis = VMath.crossprd(yAxis, zAxis);
    yAxis = VMath.crossprd(zAxis, xAxis);
    cs.setXAxis(xAxis);
    cs.setYAxis(yAxis);
    cs.setZAxis(zAxis);
    //}
  }

  public static double[] vectorRandomizer(double[] inputVec, float degree) {
    double[] result = new double[inputVec.length];
    double inputMag = mag(inputVec);
    for(int i=0;i< result.length; i++) {
      int signum = (int) FastMath.signum(Math.random()-.5);
      result[i] = inputVec[i] + signum * Math.random() * degree * inputMag;
    }
    return vecMultByScalar(normalize(result), inputMag);
  }

  public static CoordSys createCoordSys(double[] zAxis, double[] position, double[] starterYAxis) {
    CoordSys result = new CoordSys(position[0], position[1], position[2]);
    double[] yAxis = crossprd(zAxis, starterYAxis == null ? new double[]{1, 0, 0} : starterYAxis);
    double[] xAxis = crossprd(yAxis, zAxis);
    result.setXAxis(normalize(xAxis));
    result.setYAxis(normalize(yAxis));
    result.setZAxis(normalize(zAxis));
    return result;
  }

  public static void main(String[] args) {
    double[] result = VMath.vectorRandomizer(new double[]{1,2,3}, .2f);
  }
}
