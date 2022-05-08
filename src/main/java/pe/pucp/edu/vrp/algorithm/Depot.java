package pe.pucp.edu.vrp.algorithm;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Depot extends Node {
    private Truck[] totalFleet;
    private List<Truck> currentFleet;
    private AntColony antColony;
    private List<Order> depotOrders;


    public Depot(Node node, int truckCount) {
        super(node.getUbigeo(), node.getLongitude(), node.getLatitude(), node.getDepartment(), node.getCity(), node.getRegion(), node.getMatrixIndex());
        totalFleet = new Truck[truckCount];
        currentFleet = new ArrayList<>();
        for (int i = 0; i < truckCount; i++) {
            totalFleet[i] = new Truck(1000);
            currentFleet.add(totalFleet[i]);
        }
        antColony = new AntColony(10);
        depotOrders = new ArrayList<>();
    }

    public double depotRouting(Matrix[][] mapGraph, List<Node> nodeList, List<Connection> connectionList) {
        double depotCost = 0.0;
        if (!depotOrders.isEmpty()) {
            System.out.println("\nPerforming routing for depot: " + getCity() + "\nOrders: " + depotOrders);
            for (Truck truck : totalFleet) {
                if (depotOrders.isEmpty()) {
                    System.out.println("All routed\nRemaining truck count: " + currentFleet.size());
                    break;
                }
                List<Node> route = antColony.getRoute(getMatrixIndex(), mapGraph, nodeList, depotOrders, connectionList, truck.getMaxLoad());
                if (removeOrders(route) && route.size() > 1) {
                    truck.setCurrentLoad(antColony.getColony()[0].getCurrentLoad());
                    depotCost += antColony.getColony()[0].getTotalCost();
                    System.out.println("New Truck: " + route);
                    System.out.printf("Route cost: %4.1f h Truck load: %4.1f\n", antColony.getColony()[0].getTotalCost(), truck.getCurrentLoad());
                    currentFleet.remove(truck);
                } else {
                    break;
                }
            }
            if (!depotOrders.isEmpty())
                System.out.println("Error, orders missing to route: " + depotOrders);

        }
        System.out.printf("Total depot cost: %3.2f h\n", depotCost);
        return depotCost;
    }

    private boolean removeOrders(List<Node> route) {
        if (route.isEmpty()) return true;
        boolean removed = false;
        for (Node node : route) {
            Order order = depotOrders.stream().filter(o -> node == o.getDestination()).findFirst().orElse(null);
            if (Objects.nonNull(order)) {
                depotOrders.remove(order);
                removed = true;
            }
        }
        return removed;
    }
}
