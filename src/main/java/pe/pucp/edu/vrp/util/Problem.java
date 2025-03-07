package pe.pucp.edu.vrp.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.pucp.edu.vrp.algorithm.Connection;
import pe.pucp.edu.vrp.algorithm.Depot;
import pe.pucp.edu.vrp.algorithm.Matrix;
import pe.pucp.edu.vrp.algorithm.Node;
import pe.pucp.edu.vrp.algorithm.Order;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public static Matrix[][] mapGraph;
    public static List<Node> nodeList;
    public static List<Depot> depotList;

    public static void initParams() throws IOException {
        int size = readOffices();
        double distance;
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
                    distance = getDistance(nodeI.getLatitude(), nodeJ.getLatitude(), nodeI.getLongitude(), nodeJ.getLongitude());
                    mapGraph[i][j].setHeuristicValue(distance);
                }
            }
        }
        readConnections();
    }

    public static List<Order> routeOrders() {
        List<Order> missingOrders = new ArrayList<>();
        List<Thread> threadList = new ArrayList<>();
        for (Depot depot : depotList) {
            Thread t = new Thread(() -> {
                List<Order> orderList = depot.depotRouting(mapGraph, nodeList);
                if (Objects.nonNull(orderList) && !orderList.isEmpty()) {
                    missingOrders.addAll(depot.getDepotOrders());
                }
            });
            t.start();
            threadList.add(t);
        }
        try {
            for (Thread t : threadList) {
                t.join();
            }
        } catch (Exception ex) {
            System.out.println("Failed to join");
            return missingOrders;
        }
        return missingOrders;
    }

    private static int readOffices() throws IOException {
        nodeList = new ArrayList<>();
        depotList = new ArrayList<>();

        File file = new File("inf226.oficinas.txt");
        Scanner sc = new Scanner(file);

        //Carga de archivos al objeto Oficinas y Almacenes
        int n = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] values = line.split(",");

            //Cargando los Oficinas
            Node node = Node.builder().matrixIndex(n).ubigeo(values[0]).department(values[1]).city(values[2]).
                    latitude(Double.parseDouble(values[3])).longitude(Double.parseDouble(values[4])).region(Region.valueOf(values[5])).build();
            nodeList.add(node);

            //Cargando los 3 Almacenes Principales
            if (values[2].equals("LIMA"))
                depotList.add(new Depot(node));
            if (values[2].equals("TRUJILLO"))
                depotList.add(new Depot(node));
            if (values[2].equals("AREQUIPA"))
                depotList.add(new Depot(node));

            n = n + 1;
        }
        return n;
    }

    private static void readConnections() throws IOException {
        File file = new File("inf226.tramos.v.2.0.txt");
        Scanner sc = new Scanner(file);
        int x, y;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] values = line.trim().split(" => ");

            Node n = findNodeByUbigeo(values[0]);
            x = n.getMatrixIndex();
            n = findNodeByUbigeo(values[1]);
            y = n.getMatrixIndex();
            Connection connection = new Connection(x, y);
            nodeList.get(x).getConnectionList().add(connection);
            nodeList.get(y).getConnectionList().add(connection);
        }
    }

    private static double getDistance(double lat1, double lat2, double lon1, double lon2) {
        double rad, p, left, right, sum, dist = 0;
        rad = 6371;
        p = Math.PI / 180;

        left = 0.5 - Math.cos((lat2 - lat1) * p) / 2;
        right = Math.cos(lat1 * p) * Math.cos(lat2 * p) * (1 - Math.cos((lon2 - lon1) * p)) / 2;
        sum = left + right;

        dist = 2 * rad * Math.asin(Math.sqrt(sum));
        return dist;
    }

    public static void resetDepots() {
        for (Depot d : depotList) {
            d.getDepotOrders().clear();
            d.getCurrentFleet().clear();
        }

        for (Matrix[] array : mapGraph) {
            for (Matrix m : array) {
                if (m.getHeuristicValue() != 0) {
                    m.setPheromoneConc(1.0);
                }
            }
        }
    }

    public static void assignClosest(Order order) {
        double minLength = 9999.9;
        int x = -1;
        for (int i = 0; i < depotList.size(); i++) {
            Depot depot = depotList.get(i);
            Matrix matrixVal = mapGraph[depot.getMatrixIndex()][order.getDestination().getMatrixIndex()];
            if (!depot.getCurrentFleet().isEmpty() && matrixVal.getHeuristicValue() < minLength && matrixVal.getHeuristicValue() > 0
                    && depot.getCurrentCapacity() + order.getPackageAmount() <= depot.getMaxCapacity()) {
                minLength = matrixVal.getHeuristicValue();
                x = i;
            }
        }
        if (x != -1) {
            Depot depot = depotList.get(x);
            depot.getDepotOrders().add(order);
            depot.setCurrentCapacity(depot.getCurrentCapacity() + order.getPackageAmount());
        }
    }

    public static boolean isBlocked(int x, int y, double timePassed) {
        Node xNode = nodeList.get(x);
        for (Connection cn : xNode.getConnectionList()) {
            if (cn.getXIndex() == x && cn.getYIndex() == y || cn.getXIndex() == y && cn.getYIndex() == x) {
                if (!cn.isBlocked()) {
                    return false;
                } else {
                    return cn.getBlockadeStart() <= timePassed && cn.getBlockadeEnd() >= timePassed;
                }
            }
        }

        Node yNode = nodeList.get(y);
        for (Connection cn : yNode.getConnectionList()) {
            if (cn.getXIndex() == x && cn.getYIndex() == y || cn.getXIndex() == y && cn.getYIndex() == x) {
                if (!cn.isBlocked()) {
                    return false;
                } else {
                    return cn.getBlockadeStart() <= timePassed && cn.getBlockadeEnd() >= timePassed;
                }
            }
        }
        return true;
    }

    public static void setBlockades(int x, int y, double start, double end) {
        Node xNode = nodeList.get(x);
        for (Connection cn : xNode.getConnectionList()) {
            if (cn.getXIndex() == x && cn.getYIndex() == y) {
                cn.setBlocked(true);
                cn.setBlockadeStart(start);
                cn.setBlockadeEnd(end);
            }

            if (cn.getXIndex() == y && cn.getYIndex() == x) {
                cn.setBlocked(true);
                cn.setBlockadeStart(start);
                cn.setBlockadeEnd(end);
            }
        }

        Node yNode = nodeList.get(y);
        for (Connection cn : yNode.getConnectionList()) {
            if (cn.getXIndex() == x && cn.getYIndex() == y) {
                cn.setBlocked(true);
                cn.setBlockadeStart(start);
                cn.setBlockadeEnd(end);
            }

            if (cn.getXIndex() == y && cn.getYIndex() == x) {
                cn.setBlocked(true);
                cn.setBlockadeStart(start);
                cn.setBlockadeEnd(end);
            }
        }
    }

    public static Node findNodeByUbigeo(String ubigeo) {
        return nodeList.stream().filter(node -> node.getUbigeo().equals(ubigeo)).findFirst().orElse(null);
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
