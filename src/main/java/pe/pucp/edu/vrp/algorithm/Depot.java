package pe.pucp.edu.vrp.algorithm;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Depot {
    private Truck[] totalFleet;
    private List<Truck> currentFleet;
    private AntColony antColony;
    private String location;
    private List<Node> depotOrders;
    private double longitude;
    private double latitude;

    public Depot(int count, String location) {
        totalFleet = new Truck[count];
        currentFleet = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            totalFleet[i] = new Truck();
            totalFleet[i].setMaxLoad(50);
            currentFleet.add(totalFleet[i]);
        }
        antColony = new AntColony(10);
        this.location = location;
        depotOrders = new ArrayList<>();
    }

    public void depotRouting(Matrix[][] mapGraph) {
        if (!depotOrders.isEmpty()) {
            System.out.println("Performing routing for depot: " + location + "\nOrders: " + depotOrders);
            for (Truck truck : totalFleet) {
                if (depotOrders.isEmpty()) {
                    System.out.println("\nAll routed\nRemaining truck count: " + currentFleet.size());
                    break;
                }
                List<Node> route = antColony.getRoute(mapGraph, depotOrders, truck.getMaxLoad());
                if (route.size() > 1 && depotOrders.removeAll(route)) {
                    System.out.println("New Truck: " + route);
                    currentFleet.remove(truck);
                } else {
                    System.out.println("Error");
                }
            }
        }
    }
}
