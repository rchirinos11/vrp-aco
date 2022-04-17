package pe.pucp.edu.vrp.algorithm;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AntColony {
    private Ant[] colony;
    private int bestAnts;
    private int colonySize;

    public AntColony(int n) {
        colony = new Ant[n];
        colonySize = n;
        bestAnts = 3;
    }

    public void work(Matrix[][] mapGraph, List<Node> orders) {
        int i;
        System.out.println(colonySize);
        System.out.println("\nPrint all:\n");
        for (i = 0; i < colonySize; i++) {
            colony[i] = new Ant();
            colony[i].work(mapGraph, orders);
            System.out.println(colony[i].getVisitedNodes() + "\nCost:" + colony[i].getTotalCost());
        }

        System.out.println("\n\nPrint best:\n");
        Arrays.sort(colony);
        for (i = 0; i < bestAnts; i++) {
            System.out.println(colony[i].getVisitedNodes() + "\nCost:" + colony[i].getTotalCost());
        }
    }
}
