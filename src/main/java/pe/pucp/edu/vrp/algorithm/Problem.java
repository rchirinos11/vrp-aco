package pe.pucp.edu.vrp.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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
    private int size;
    private Matrix[][] mapGraph;
    private AntColony antColony;
    private Truck[] totalFleet;
    private List<Truck> currentFleet;

    /*  double[][] pheromoneConc = {{0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                                    {1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                                    {1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                                    {1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0},
                                    {1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0},
                                    {1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0},
                                    {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0},
                                    {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0}}

        double[][] heuristicValue ={{0.0, 7.7, 9.7, 4.5, 8.5, 3.1, 8.3, 2.9},
                                    {4.8, 0.0, 3.3, 9.6, 2.0, 9.5, 7.8, 9.3},
                                    {4.4, 9.7, 0.0, 9.0, 5.1, 5.2, 4.2, 8.6},
                                    {8.7, 4.6, 6.2, 0.0, 2.0, 6.0, 8.0, 1.1},
                                    {2.4, 0.5, 7.9, 2.9, 0.0, 7.1, 3.0, 0.8},
                                    {4.8, 6.1, 9.6, 8.1, 9.3, 0.0, 6.5, 5.2},
                                    {2.7, 5.3, 8.5, 0.2, 8.2, 0.1, 0.0, 3.7},
                                    {8.5, 6.1, 8.5, 1.2, 3.7, 5.2, 0.4, 0.0}}
    */
    public void initParams(int truckCount) {
        int i, j;
        double[][] pheromoneConc = {{0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, {1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0}, {1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0}, {1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0}, {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0}, {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0}};
        double[][] heuristicValue = {{372.0, 258.7, 939.5, 143.1, 890.1, 624.2, 408.0, 758.3}, {386.6, 210.1, 241.7, 43.3, 537.1, 437.7, 378.9, 227.1},
                {43.2, 53.5, 838.1, 35.1, 761.7, 71.4, 60.3, 40.1}, {314.5, 600.8, 189.3, 771.9, 937.1, 19.1, 132.3, 866.4}, {631.3, 402.6, 974.1, 488.6, 665.3, 248.2, 744.4, 298.5},
                {428.6, 579.3, 545.2, 605.3, 960.1, 297.3, 253.7, 960.4}, {296.3, 184.8, 269.7, 744.6, 682.2, 473.0, 160.0, 565.1}, {613.5, 561.0, 794.7, 76.7, 629.7, 969.8, 388.9, 189.4}};
        size = pheromoneConc.length;
        mapGraph = new Matrix[size][size];
        for (i = 0; i < size; i++) {
            mapGraph[i] = new Matrix[size];
            for (j = 0; j < size; j++) {
                mapGraph[i][j] = new Matrix();
                mapGraph[i][j].setHeuristicValue(heuristicValue[i][j]);
                mapGraph[i][j].setPheromoneConc(pheromoneConc[i][j]);
            }
        }
        totalFleet = new Truck[truckCount];
        currentFleet = new ArrayList<>();
        for (i = 0; i < truckCount; i++) {
            totalFleet[i] = new Truck();
            totalFleet[i].setMaxLoad(50);
            currentFleet.add(totalFleet[i]);
        }
        antColony = new AntColony(10);
    }

    public void route(List<Node> orderList) {
        for (Truck truck : totalFleet) {
            if (orderList.isEmpty()) {
                System.out.println("\nAll routed\nRemaining truck count: " + currentFleet.size());
                break;
            }
            List<Node> route = antColony.getRoute(mapGraph, orderList, truck.getMaxLoad());
            if (route.size() > 1 && orderList.removeAll(route)) {
                System.out.println("New Truck: " + route);
                currentFleet.remove(truck);
            } else {
                System.out.println("Error");
            }
        }
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
