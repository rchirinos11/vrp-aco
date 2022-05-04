package pe.pucp.edu.vrp.algorithm;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.pucp.edu.vrp.Constant;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AntColony {
    private Ant[] colony;
    private int colonySize;

    public AntColony(int n) {
        colony = new Ant[n];
        colonySize = n;
    }

    public List<Node> getRoute(int start, Matrix[][] mapGraph, List<Node> orders, double maxLoad) {
        for (int i = 0; i < Constant.ITERATIONS; i++)
            work(start, mapGraph, orders, maxLoad);
        return colony[0].getVisitedNodes();
    }

    public void displayColony() {
        for (int i = 0; i < colonySize; i++) {
            System.out.println(colony[i].getVisitedNodes() + "\nCost: " + colony[i].getTotalCost());
        }
    }

    private void work(int start, Matrix[][] mapGraph, List<Node> orders, double maxLoad) {
        int i;
        for (i = 0; i < colonySize; i++) {
            colony[i] = new Ant();
            colony[i].work(start, mapGraph, orders, maxLoad);
        }
        Arrays.sort(colony);
        for (i = 0; i < Constant.BEST; i++) {
            globalUpdate(mapGraph, colony[i], start);
        }
    }

    private void globalUpdate(Matrix[][] mapGraph, Ant ant, int start) {
        int i = start, j;
        double concValue;
        for (Node node : ant.getVisitedNodes()) {
            j = node.getMatrixIndex();
            if (mapGraph[i][j].getHeuristicValue() > 0) {
                concValue = mapGraph[i][j].getPheromoneConc();
                concValue = (1 - Constant.RHO) * concValue + Constant.RHO * (1.5 / mapGraph[i][j].getHeuristicValue());
                mapGraph[i][j].setPheromoneConc(concValue);
            }
            i = j;
        }
    }
}
