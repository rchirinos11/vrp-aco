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

    public void routeTrucks(Matrix[][] mapGraph, List<Node> nodeList) {
        int i = 0;
        int count = 0;
        while (i < truckList.size() && !depotOrders.isEmpty()) {
            Truck truck = truckList.get(i++);
            List<Visited> route = antColony.getRoute(truck.getStart(), mapGraph, nodeList, depotOrders, truck.getMaxLoad());
            Ant ant = antColony.getColony()[0];
            if (removeOrders(ant.getOrderList()) && route.size() > 1) {
                count = 0;
                truck.setCost(ant.getTotalCost());
                truck.setCurrentLoad(ant.getCurrentLoad());
                cost += truck.getCost();
                List<Visited> routeBack = antColony.routeBack(route.get(route.size() - 1).getMatrixIndex(), mapGraph, nodeList);
                route.addAll(routeBack);
                truck.setRoute(route);
            } else if (ant.getCurrentLoad() < truck.getMaxLoad() && count < 30) {
                i--;
                count++;
            }
        }
        missingRouted = depotOrders.size();
    }

    private boolean removeOrders(List<Order> orderList) {
        return depotOrders.removeIf(order -> Objects.isNull(orderList.stream()
                .filter(o -> order == o).findFirst().orElse(null)));
    }

    @Override
    public int compareTo(SuperColony sc) {
        return (int) ((missingRouted * 100 + cost) - (sc.getMissingRouted() * 100 + sc.getCost()));
    }
}
