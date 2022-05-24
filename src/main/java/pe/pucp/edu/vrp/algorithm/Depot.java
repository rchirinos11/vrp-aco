package pe.pucp.edu.vrp.algorithm;

import lombok.Getter;
import lombok.Setter;
import pe.pucp.edu.vrp.util.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Depot extends Node {
    private List<Truck> currentFleet;
    private List<Order> depotOrders;


    public Depot(Node origin) {
        super(origin.getUbigeo(), origin.getLongitude(), origin.getLatitude(), origin.getDepartment(), origin.getCity(), origin.getRegion(), origin.getMatrixIndex());
        depotOrders = new ArrayList<>();
        currentFleet = new ArrayList<>();
    }

    public double depotRouting(Matrix[][] mapGraph, List<Node> nodeList, List<Connection> connectionList) {
        if (depotOrders.isEmpty()) return 0;

        System.out.println("\nPerforming routing for depot: " + getCity() + "\nOrders: " + depotOrders);
        SuperColony[] superColonyList = new SuperColony[Constant.ITERATIONS];
        for (int i = 0; i < Constant.ITERATIONS; i++) {
            SuperColony sc = new SuperColony(currentFleet, depotOrders);
            sc.routeTrucks(getMatrixIndex(), mapGraph, nodeList, connectionList);
            superColonyList[i] = sc;
        }
        Arrays.sort(superColonyList);
        currentFleet = superColonyList[0].getTruckList();
        System.out.println();
        for (Truck t : currentFleet) {
            removeOrders(t.getNodeRoute(), depotOrders);
        }
        if (!depotOrders.isEmpty()) {
            System.out.println("\nError, orders missing to route: " + depotOrders);
        }
        System.out.printf("Total depot cost: %3.2f h\n", superColonyList[0].getCost());
        return superColonyList[0].getCost();
    }

    private void removeOrders(List<Node> route, List<Order> orderList) {
        if (route.isEmpty()) return;
        boolean removed = false;
        for (Node node : route) {
            Order order = orderList.stream().filter(o -> node == o.getDestination()).findFirst().orElse(null);
            if (Objects.nonNull(order)) {
                orderList.remove(order);
                removed = true;
            }
        }
    }
}
