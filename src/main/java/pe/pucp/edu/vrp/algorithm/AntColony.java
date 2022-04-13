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
    }

    public void work(Matrix[][] mapGraph, List<Node> orders) {
        System.out.println(colonySize);
        for (Ant ant : colony) {
            ant.work(mapGraph, orders);
        }
        Arrays.sort(colony);
        for (int i = 0; i < bestAnts; i++) {
            colony[i].getVisitedNodes();
        }
    }
}
