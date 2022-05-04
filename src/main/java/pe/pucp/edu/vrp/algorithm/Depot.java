package pe.pucp.edu.vrp.algorithm;

import lombok.Getter;
import lombok.Setter;
import pe.pucp.edu.vrp.Region;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Depot {
    private Truck[] totalFleet;
    private List<Truck> currentFleet;
    private AntColony antColony;
    private double longitude;
    private double latitude;
    private Region region;
    private String location;
    private int matrixIndex;
    private List<Node> depotOrders;

    public Depot(int count, String location, int matrixIndex) {
        totalFleet = new Truck[count];
        currentFleet = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            totalFleet[i] = new Truck(50);
            currentFleet.add(totalFleet[i]);
        }
        antColony = new AntColony(10);
        this.location = location;
        this.matrixIndex = matrixIndex;
        depotOrders = new ArrayList<>();
    }

    public double depotRouting(Matrix[][] mapGraph) {
        double depotCost = 0.0;
        if (!depotOrders.isEmpty()) {
            System.out.println("\nPerforming routing for depot: " + location + "\nOrders: " + depotOrders);
            for (Truck truck : totalFleet) {
                if (depotOrders.isEmpty()) {
                    System.out.println("All routed\nRemaining truck count: " + currentFleet.size());
                    break;
                }
                List<Node> route = antColony.getRoute(matrixIndex, mapGraph, depotOrders, truck.getMaxLoad());
                if (depotOrders.removeAll(route) && route.size() > 1) {
                    truck.setCurrentLoad(antColony.getColony()[0].getCurrentLoad());
                    depotCost += antColony.getColony()[0].getTotalCost();
                    System.out.println("New Truck: " + route);
                    System.out.printf("Route cost: %4.1f h Truck load: %4.1f\n", antColony.getColony()[0].getTotalCost(), truck.getCurrentLoad());
                    currentFleet.remove(truck);
                } else {
                    System.out.println("Error, orders missing to route: " + depotOrders);
                    break;
                }
            }
        }
        System.out.printf("Total depot cost: %3.2f h\n", depotCost);
        return depotCost;
    }
}
