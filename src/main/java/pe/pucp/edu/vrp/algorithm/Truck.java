package pe.pucp.edu.vrp.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private double currentFuel;
    private double maxFuel;
    private List<Node> orderList;

    public Truck() {
        currentFuel = 0.0;
        currentLoad = 0.0;
        orderList = new ArrayList<>();
    }

    public void addOrder(Node order) {
        orderList.add(order);
    }
}
