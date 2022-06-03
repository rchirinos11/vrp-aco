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

    public List<Order> depotRouting(Matrix[][] mapGraph, List<Node> nodeList) {
        if (depotOrders.isEmpty()) return null;

        System.out.println("\nPerforming routing for depot: " + getCity() + "\nOrders: " + depotOrders);
        SuperColony[] superColonyList = new SuperColony[Constant.ITERATIONS];
        for (int i = 0; i < Constant.ITERATIONS; i++) {
            SuperColony sc = new SuperColony(currentFleet, depotOrders);
            sc.routeTrucks(mapGraph, nodeList);
            superColonyList[i] = sc;
        }
        Arrays.sort(superColonyList);
        currentFleet = superColonyList[0].getTruckList();
        List<Order> copyList = new ArrayList<>(depotOrders);
        for (Truck t : currentFleet) {
            removeOrders(t.getRoute(), copyList);
        }
        if (!copyList.isEmpty()) {
            System.out.println("Error, orders missing to route: " + copyList);
        }
        System.out.printf("Total depot cost: %3.2f h\n", superColonyList[0].getCost());
        return copyList;
    }

    private void removeOrders(List<Visited> route, List<Order> orderList) {
        if (route.isEmpty()) return;
        for (Visited node : route) {
            Order order = orderList.stream().filter(o -> node.getOrderId() == o.getOrderId()).findFirst().orElse(null);
            if (Objects.nonNull(order)) {
                orderList.remove(order);
            }
        }
    }
}