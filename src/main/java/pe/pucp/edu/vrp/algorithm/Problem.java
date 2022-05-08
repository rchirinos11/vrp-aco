package pe.pucp.edu.vrp.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;
import pe.pucp.edu.vrp.util.Region;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * F
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem {
    private int size;
    private Matrix[][] mapGraph;
    private List<Node> nodeList;
    private List<Depot> depotList;
    private List<Connection> connectionList;

    public void initParams() throws IOException {
        int size = readOffices();
        mapGraph = new Matrix[size][size];
        for (int i = 0; i < size; i++) {
            mapGraph[i] = new Matrix[size];
            for (int j = 0; j < size; j++) {
                mapGraph[i][j] = new Matrix();
                if (i == j) {
                    mapGraph[i][j].setPheromoneConc(0.0);
                    mapGraph[i][j].setHeuristicValue(0.0);
                } else {
                    mapGraph[i][j].setPheromoneConc(1.0);
                    Node nodeI = nodeList.get(i);
                    Node nodeJ = nodeList.get(j);
                    double distance = Math.pow(nodeI.getLatitude() - nodeJ.getLatitude(), 2) + Math.pow(nodeI.getLongitude() - nodeJ.getLongitude(), 2);
                    mapGraph[i][j].setHeuristicValue(Math.sqrt(distance));
                }
            }
        }
        readConnections();
        readOrderFile();
    }

    public double routeOrders() {
        double totalTraveled = 0;
        for (Depot depot : depotList) {
            totalTraveled += depot.depotRouting(mapGraph, nodeList, connectionList);
        }
        return totalTraveled;
    }

    private int readOffices() throws IOException {
        nodeList = new ArrayList<>();
        depotList = new ArrayList<>();

        File file = new ClassPathResource("inf226.oficinas.txt").getFile();
        Scanner sc = new Scanner(file);

        //Carga de archivos al objeto Oficinas y Almacenes
        int n = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] values = line.split(",");

            //Cargando los Oficinas
            Node node = Node.builder().matrixIndex(n).ubigeo(values[0]).department(values[1]).city(values[2]).
                    longitude(Double.parseDouble(values[3])).latitude(Double.parseDouble(values[4])).region(Region.valueOf(values[5])).build();
            nodeList.add(node);

            //Cargando los 3 Almacenes Principales
            if (values[2].equals("LIMA"))
                depotList.add(new Depot(node, 21));
            if (values[2].equals("TRUJILLO"))
                depotList.add(new Depot(node, 10));
             if (values[2].equals("AREQUIPA"))
                 depotList.add(new Depot(node, 14));

            n = n + 1;
        }
        return n;
    }

    private void readConnections() throws IOException {
        connectionList = new ArrayList<>();
        File file = new ClassPathResource("inf226.tramos.v.2.0.txt").getFile();
        Scanner sc = new Scanner(file);
        int x, y;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] values = line.trim().split(" => ");

            Node n = nodeList.stream().filter(node -> node.getUbigeo().equals(values[0])).findFirst().orElse(null);
            x = n.getMatrixIndex();
            n = nodeList.stream().filter(node -> node.getUbigeo().equals(values[1])).findFirst().orElse(null);
            y = n.getMatrixIndex();
            connectionList.add(new Connection(x, y));
        }
    }

    private void readOrderFile() throws IOException {
        File file = new ClassPathResource("inf226.ventas202205.txt").getFile();
        Depot depot;
        Node node;
        int x = 0;

        String line;
        String[] splittedLine;
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((line = br.readLine()) != null) {
            if (x == 50)
                break;
            splittedLine = line.split(",");
            int demand = Integer.parseInt(splittedLine[2].trim());
            splittedLine = splittedLine[1].trim().split(" =>  ");
            String depotUbigeo = splittedLine[0];
            String nodeUbigeo = splittedLine[1];
            depot = depotList.stream().filter(d -> d.getUbigeo().equals(depotUbigeo)).findFirst().orElse(null);
            node = nodeList.stream().filter(n -> n.getUbigeo().equals(nodeUbigeo)).findFirst().orElse(null);
            depot.getDepotOrders().add(new Order(x++, node, demand));
        }

    }

    private void findClosest(List<Order> orderList) {
        for (Order order : orderList) {
            double minLength = 999.9;
            int x = -1;
            for (int i = 0; i < depotList.size(); i++) {
                Matrix matrixVal = mapGraph[depotList.get(i).getMatrixIndex()][order.getDestination().getMatrixIndex()];
                if (matrixVal.getHeuristicValue() < minLength && matrixVal.getHeuristicValue() > 0) {
                    minLength = matrixVal.getHeuristicValue();
                    x = i;
                }
            }
            if (x != -1) {
                depotList.get(x).getDepotOrders().add(order);
            }
        }
    }

    public static float invSqrt(float number) {
        int i;
        float x2, y;
        final float threehalfs = 1.5F;
        x2 = number * 0.5f;
        y = number;
        i = Float.floatToIntBits(y);                    // evil floating point bit hack
        i = 0x5f3759df - (i >> 1);                      // what the fuck?
        y = Float.intBitsToFloat(i);
        y = y * (threehalfs - x2 * y * y);              // 1st iteration
        //  y = y * (threehalfs - x2 * number * number);    // 2nd iteration, can be removed

        return y;
    }
}
