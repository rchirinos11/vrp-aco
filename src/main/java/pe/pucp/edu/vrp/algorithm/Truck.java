package pe.pucp.edu.vrp.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Truck {
    private int id;
    private char type;
    private double currentLoad;
    private double maxLoad;
    private List<Node> nodeRoute;
    private boolean working;

    public Truck(int id, double max) {
        currentLoad = 0.0;
        nodeRoute = new ArrayList<>();
        maxLoad = max;
        working = true;
    }

    public Truck(Truck t) {
        id = t.getId();
        type = t.getType();
        currentLoad = t.getCurrentLoad();
        maxLoad = t.getMaxLoad();
        working = t.isWorking();
        nodeRoute = new ArrayList<>(t.getNodeRoute());
    }
}
