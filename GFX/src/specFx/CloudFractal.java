package specFx;

import java.util.Random;

/**
 * @author iban ferreira
 * a three-dimensional implementation of the Midpoint Displacement Algorithm
 * (Diamond-Square algorithm) which produces two-dimensional clouds fractals
 */
public class CloudFractal implements HeightMap {

    private int resolution;
    private double[][] data;

    private double variance;

    private Random random;


    
    public static void main(String[] args)
    {
    	CloudFractal cf = new CloudFractal(10,.3);
    	int oo=0;
    }
    public CloudFractal(int coefficient, double variance) {

        this.resolution = (1 << coefficient) + 1; // 1 << coefficient = 2 ^ coefficient
        this.data = new double[this.resolution][this.resolution];

        this.variance = variance;

        this.random = new Random();

        // assign a random channel value to each corner
        this.data[0][0] = rnd();
        this.data[0][this.resolution - 1] = rnd();
        this.data[this.resolution - 1][0] = rnd();
        this.data[this.resolution - 1][this.resolution - 1] = rnd();

        for (int k = 0; k <= coefficient; k++) {
            //print();
            int l = 1 << (coefficient - k); // current sub-square length
            for (int i = 0; i < this.resolution - 1; i += l) {
                for (int j = 0; j < this.resolution - 1; j += l) {
                    diamond(i, j, l); // midpoint random perturbation
                    square(i, j, l); // midpoint random perturbation
                }
            }

        }
    }


    private void diamond(int i, int j, int length) {
        // take the average of four corners, add a random perturbation
        // and assign this to the midpoint of the heightmap
        if (length > 1){
            int half = length/2;
            double avg = (this.data[i][j] + this.data[i + length][j] +
                this.data[i + length][j] + this.data[i + length][j + length]) * 0.25;
            this.data[i + half][j + half] = avg + rnd() * this.variance;
System.out.println(""+(avg + rnd() * this.variance));
        }

    }


    private void square(int i, int j, int length) {
        double avg = 0.0;
        int half = length / 2;
        avg += this.data[i][j + half];
        avg += this.data[i + half][j];
        avg += this.data[i][j + half];
        avg += this.data[i + half][j + half];
    }


    private double rnd() {
        return this.random.nextGaussian();
    }


    public double getChannel(int i, int j) {
        return this.data[i][j];
    }


    private void print() {
        StringBuffer stringBuffer;
        for (int i = 0; i < this.resolution; i++) {
            stringBuffer = new StringBuffer(this.resolution);
            for (int j = 0; j < this.resolution; j++) {
                String element = (data[i][j] == 0) ? "-" : "X";
                stringBuffer.append("[");
                stringBuffer.append(element);
                stringBuffer.append("]");
            }
            System.out.println(stringBuffer);
        }
        System.out.println();

    }

}
