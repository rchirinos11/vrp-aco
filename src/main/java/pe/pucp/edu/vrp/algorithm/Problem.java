package pe.pucp.edu.vrp.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Depot[] depotList;

    public void initParams(int truckCount) {
        int i, j;
        double[][] pheromoneConc = {{0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, {1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, {1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, {1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0}, {1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0}, {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0}, {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0}};
        double[][] heuristicValue = {{0.0, 918.4, 609.5, 131.9, 223.7, 520.8, 989.3, 737.7, 844.0, 921.1},
                                     {918.4, 0.0, 876.4, 828.9, 820.4, 590.9, 210.1, 190.4, 388.4, 951.0},
                                     {609.5, 876.4, 0.0, 853.3, 737.8, 778.8, 303.4, 185.0, 873.9, 932.9},
                                     {131.9, 828.9, 853.3, 0.0, 14.8, 615.8, 142.8, 291.7, 893.7, 478.3},
                                     {223.7, 820.4, 737.8, 14.8, 0.0, 449.7, 374.0, 990.1, 848.3, 753.9},
                                     {520.8, 590.9, 778.8, 615.8, 449.7, 0.0, 255.4, 729.4, 341.1, 398.2},
                                     {989.3, 210.1, 303.4, 142.8, 374.0, 255.4, 0.0, 39.6, 714.3, 419.4},
                                     {737.7, 190.4, 185.0, 291.7, 990.1, 729.4, 39.6, 0.0, 399.4, 173.7},
                                     {844.0, 388.4, 873.9, 893.7, 848.3, 341.1, 714.3, 399.4, 0.0, 426.8},
                                     {921.1, 951.0, 932.9, 478.3, 753.9, 398.2, 419.4, 173.7, 426.8, 0.0}};

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
        depotList = new Depot[]{new Depot(truckCount, "Lima"), new Depot(truckCount, "Trujillo"),
                new Depot(truckCount, "Arequipa")};
    }

    public void routeOrders(List<Node> orderList) {
        findClosest(orderList);
        depotList[0].depotRouting(mapGraph);
        depotList[1].depotRouting(mapGraph);
        depotList[2].depotRouting(mapGraph);
    }

    private void findClosest(List<Node> orderList) {
        for (Node order : orderList) {
            double minLength = 999.9;
            int x = -1;
            for (int i = 0; i < depotList.length; i++) {
                if (mapGraph[i][order.getMatrixIndex()].getHeuristicValue() < minLength) {
                    minLength =  mapGraph[i][order.getMatrixIndex()].getHeuristicValue();
                    x = i;
                }
            }
            if (x != -1) {
                depotList[x].getDepotOrders().add(order);
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
