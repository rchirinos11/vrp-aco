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
    private int matrixIndex;
    private List<Node> depotOrders;
    private double longitude;
    private double latitude;
    private int ubigeo;

    public Depot ( int matrixIndex, int ubigeo, double longitude, double latitude){
        this.ubigeo=ubigeo;
        this.longitude=longitude;
        this.latitude=latitude;
        this.matrixIndex=matrixIndex;
    }
    public String imprimirAlma(){
        return  (matrixIndex+"," +ubigeo+ ","+longitude + "," + latitude);
    }

    public Depot(int count, String location, int matrixIndex) {
        totalFleet = new Truck[count];
        currentFleet = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            totalFleet[i] = new Truck();
            totalFleet[i].setMaxLoad(50);
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
                    System.out.println("Route cost: " + antColony.getColony()[0].getTotalCost() + " Truck load: " + truck.getCurrentLoad());
                    currentFleet.remove(truck);
                } else {
                    System.out.println("Error");
                }
            }
        }
        System.out.println("Total depot cost: " + depotCost);
        return depotCost;
    }
}
