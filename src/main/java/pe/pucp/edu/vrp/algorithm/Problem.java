package pe.pucp.edu.vrp.algorithm;

import lombok.*;

import java.util.List;

/**
 * This cood does something
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem {
    private int xSize;
    private int ySize;
    private Matrix[][] mapGraph;
    private AntColony antColony;
    private Truck[] fleet;

    /*  double[][] pheromoneConc = {{1.0,1.0,1.0,1.0,1.0},
                                    {1.0,0.0,1.0,1.0,1.0},
                                    {1.0,1.0,0.0,1.0,1.0},
                                    {1.0,1.0,1.0,0.0,1.0},
                                    {1.0,1.0,1.0,1.0,0.0}};

                                    3 = i
                                    1 = j
                                          0    1    2    3    4
        double[][] heuristicValue = {0  {0.0, 9.5, 3.0, 7.0, 2.0},
                                     1  {9.5, 0.0, 4.5, 3.5, 8.0},
                                     2  {3.0, 4.5, 0.0, 6.0, 1.0},
                                     3  {7.0, 3.5, 6.0, 0.0, 5.5},
                                     4  {2.0, 8.0, 1.0, 5.5, 0.0}
                                     };
    */
    public void initParams(int truckCount) {
        int i, j;
        double[][] pheromoneConc = {{0.0, 1.0, 1.0, 1.0, 1.0}, {1.0, 0.0, 1.0, 1.0, 1.0}, {1.0, 1.0, 0.0, 1.0, 1.0},
                                    {1.0, 1.0, 1.0, 0.0, 1.0}, {1.0, 1.0, 1.0, 1.0, 0.0}};
        double[][] heuristicValue = {{0.0, 9.5, 3.0, 7.0, 2.0}, {9.5, 0.0, 4.5, 3.5, 8.0}, {3.0, 4.5, 0.0, 6.0, 1.0},
                                    {7.0, 3.5, 6.0, 0.0, 5.5}, {2.0, 8.0, 1.0, 5.5, 0.0}};
        xSize = 5;
        ySize = 5;
        mapGraph = new Matrix[xSize][ySize];
        for (i = 0; i < xSize; i++) {
            mapGraph[i] = new Matrix[ySize];
            for (j = 0; j < ySize; j++) {
                mapGraph[i][j] = new Matrix();
                mapGraph[i][j].setHeuristicValue(heuristicValue[i][j]);
                mapGraph[i][j].setPheromoneConc(pheromoneConc[i][j]);
            }
        }
        fleet = new Truck[truckCount];
        for (i = 0; i < truckCount; i++) {
            fleet[i] = new Truck();
            fleet[i].setMaxLoad(50);
        }
        antColony = new AntColony(10);
    }

    public void route(List<Node> orderList) {
        antColony.work(mapGraph, orderList);




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
