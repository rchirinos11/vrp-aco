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
    private int maxCapacity;
    private int currentCapacity;

    public Depot(Node origin) {
        super(origin.getUbigeo(), origin.getLongitude(), origin.getLatitude(), origin.getDepartment(), origin.getCity(), origin.getRegion(), origin.getMatrixIndex());
        depotOrders = new ArrayList<>();
        currentFleet = new ArrayList<>();
        currentCapacity = 0;
        maxCapacity = 0;
    }

    public List<Order> depotRouting(Matrix[][] mapGraph, List<Node> nodeList) {
        if (depotOrders.isEmpty()) return null;
        splitPackages(depotOrders, currentFleet);

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
        copyList.removeIf(order ->  Objects.isNull(superColonyList[0].getDepotOrders()
                .stream().filter(o -> order == o).findFirst().orElse(null)));
        if (!copyList.isEmpty()) {
            System.out.println("Error, orders missing to route: " + copyList);
        }
        System.out.printf("Total depot cost: %3.2f h\n", superColonyList[0].getCost());
        return copyList;
    }

    private void splitPackages(List<Order> orderList, List<Truck> truckList) {
        int gcd = truckList.get(0).getMaxLoad();
        int packages;

        for (int i = 1; i < truckList.size(); i++) {
            Truck truck = truckList.get(i);
            gcd = calculateGcd(gcd, truck.getMaxLoad());
        }

        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            packages = order.getPackageAmount();
            if (packages > gcd) {
                order.setPackageAmount(gcd);
                packages -= gcd;
                orderList.add(i--, Order.builder().orderId(order.getOrderId()).packageAmount(packages)
                        .remainingTime(order.getRemainingTime()).destination(order.getDestination()).build());
            }
        }
    }

    private int calculateGcd(int a, int b) {
        if (a == 0) return b;
        if (b == 0) return a;
        return calculateGcd(b, a % b);
    }

}