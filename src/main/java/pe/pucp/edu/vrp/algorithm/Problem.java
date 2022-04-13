package pe.pucp.edu.vrp.algorithm;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * This cood does something
 */
@Getter
@Setter
@Builder
public class Problem {
    private int xSize;
    private int ySize;
    private Matrix[][] mapGraph;
    private AntColony antColony;
    private Truck[] fleet;

    public void initParams(int x, int y, int truckCount) {
        xSize = x;
        ySize = y;
        mapGraph = new Matrix[x][y];
        fleet = new Truck[truckCount];
    }

    public void route() {

    }


    public static float invSqrt(float number) {
        int i;
        float x2, y;
        final float threehalfs = 1.5F;
        x2 = number * 0.5f;
        y = number;
        i = Float.floatToIntBits(y);                    // evil floating point bit hack
        i = 0x5f3759df - (i >> 1);                      // what the fuck?
        y = Float.intBitsToFloat(i);
        y = y * (threehalfs - x2 * y * y);              // 1st iteration
    //  y = y * (threehalfs - x2 * number * number);    // 2nd iteration, can be removed

        return y;
    }
}
