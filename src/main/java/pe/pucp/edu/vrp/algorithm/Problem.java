package pe.pucp.edu.vrp.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    public void initParams(int truckCount) throws FileNotFoundException {
        //read file
        File matrixFile = new File("matrix");
        Scanner sc = new Scanner(matrixFile);
        List<List<Double>> bufferList = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            line = line.trim();
            while (line.contains("  "))
                line = line.replace("  ", " ");
            String[] doubleArray = line.split(" ");
            List<Double> doubleList = new ArrayList<>();
            for (String value : doubleArray)
                doubleList.add(Double.parseDouble(value));
            bufferList.add(doubleList);
        }
        size = bufferList.size();

        //init data
        mapGraph = new Matrix[size][size];
        for (int i = 0; i < size; i++) {
            mapGraph[i] = new Matrix[size];
            for (int j = 0; j < size; j++) {
                mapGraph[i][j] = new Matrix();
                mapGraph[i][j].setHeuristicValue(bufferList.get(i).get(j));
                if (i == j)
                    mapGraph[i][j].setPheromoneConc(0.0);
                else
                    mapGraph[i][j].setPheromoneConc(1.0);
            }
        }
        depotList = new Depot[]{new Depot(truckCount, "Lim@", 0), new Depot(truckCount, "Trujillo", 1),
                new Depot(truckCount, "Arequipa", 2)};
    }

    public double routeOrders(List<Node> orderList) {
        double totalTraveled = 0;
        findClosest(orderList);
        for (Depot depot : depotList) {
            totalTraveled += depot.depotRouting(mapGraph);
        }
        return totalTraveled;
    }

    private void findClosest(List<Node> orderList) {
        for (Node order : orderList) {
            double minLength = 999.9;
            int x = -1;
            for (int i = 0; i < depotList.length; i++) {
                if (mapGraph[i][order.getMatrixIndex()].getHeuristicValue() < minLength) {
                    minLength = mapGraph[i][order.getMatrixIndex()].getHeuristicValue();
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
