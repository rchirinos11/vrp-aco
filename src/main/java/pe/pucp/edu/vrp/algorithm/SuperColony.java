package pe.pucp.edu.vrp.algorithm;

import lombok.Getter;
import lombok.Setter;
import pe.pucp.edu.vrp.util.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class SuperColony implements Comparable<SuperColony> {
    private AntColony antColony;
    private List<Truck> truckList;
    private List<Order> depotOrders;
    private double cost;
    private int missingRouted;

    public SuperColony(List<Truck> truckList, List<Order> orderList) {
        this.truckList = new ArrayList<>();
        for (Truck t : truckList) {
            this.truckList.add(new Truck(t));
        }
        depotOrders = new ArrayList<>(orderList);
        antColony = new AntColony(Constant.ITERATIONS);
        cost = 0.0;
    }

    public void routeTrucks(int matrixIndex, Matrix[][] mapGraph, List<Node> nodeList, List<Connection> connectionList) {
        int i = 0;
        while (i < truckList.size() && !depotOrders.isEmpty()) {
            Truck truck = truckList.get(i++);
            List<Node> route = antColony.getRoute(matrixIndex, mapGraph, nodeList, depotOrders, connectionList, truck.getMaxLoad());
            if (removeOrders(route, depotOrders) && route.size() > 1) {
                truck.setNodeRoute(route);
                truck.setCurrentLoad(antColony.getColony()[0].getCurrentLoad());
                cost += antColony.getColony()[0].getTotalCost();
            }
        }
        missingRouted = depotOrders.size();
    }

    private boolean removeOrders(List<Node> route, List<Order> orderList) {
        if (route.isEmpty()) return true;
        boolean removed = false;
        for (Node node : route) {
            Order order = orderList.stream().filter(o -> node == o.getDestination()).findFirst().orElse(null);
            if (Objects.nonNull(order)) {
                orderList.remove(order);
                removed = true;
            }
        }
        return removed;
    }

    @Override
    public int compareTo(SuperColony sc) {
        return missingRouted - sc.getMissingRouted();
    }
}
