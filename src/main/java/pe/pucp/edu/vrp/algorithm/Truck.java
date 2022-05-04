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
    private double currentLoad;
    private double maxLoad;
    private List<Node> orderList;
    private boolean working;

    public Truck(double max) {
        currentLoad = 0.0;
        orderList = new ArrayList<>();
        maxLoad = max;
        working = true;
    }
}
