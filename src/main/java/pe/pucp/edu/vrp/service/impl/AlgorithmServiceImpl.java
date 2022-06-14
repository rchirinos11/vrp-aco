package pe.pucp.edu.vrp.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pe.pucp.edu.vrp.algorithm.Depot;
import pe.pucp.edu.vrp.algorithm.Matrix;
import pe.pucp.edu.vrp.algorithm.Node;
import pe.pucp.edu.vrp.algorithm.Order;
import pe.pucp.edu.vrp.algorithm.Truck;
import pe.pucp.edu.vrp.algorithm.Visited;
import pe.pucp.edu.vrp.request.AlgorithmRequest;
import pe.pucp.edu.vrp.request.BlockadeRequest;
import pe.pucp.edu.vrp.request.OrderRequest;
import pe.pucp.edu.vrp.request.TruckRequest;
import pe.pucp.edu.vrp.response.AlgorithmResponse;
import pe.pucp.edu.vrp.response.DepotResponse;
import pe.pucp.edu.vrp.response.NodeResponse;
import pe.pucp.edu.vrp.response.TruckResponse;
import pe.pucp.edu.vrp.service.AlgorithmService;
import pe.pucp.edu.vrp.util.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AlgorithmServiceImpl implements AlgorithmService {
    @Override
    public ResponseEntity<?> routeTrucks(AlgorithmRequest request) {
        Problem.resetDepots();
        List<Node> nodeList = Problem.nodeList;
        List<Depot> depotList = Problem.depotList;

        splitPackages(request.getOrderList(), request.getTruckList());
        if (readBlockades(request.getBlockadeList(), nodeList)) {
            return ResponseEntity.badRequest().body(new Exception("Error en la lista de bloqueos"));
        }

        for (TruckRequest truck : request.getTruckList()) {
            Depot depot = depotList.stream().filter(d -> d.getUbigeo().equals(truck.getUbigeo())).findFirst().orElse(null);
            if (Objects.nonNull(depot)) {
                Node n = findNode(nodeList, truck.getUbigeo());
                if (Objects.isNull(n)) {
                    return ResponseEntity.badRequest().body(new Exception("No se encontro el nodo del almacen"));
                }
                depot.getCurrentFleet().add(new Truck(truck.getId(), n.getMatrixIndex(), truck.getMaxLoad()));
            } else if (!assignTruck(nodeList, depotList, truck, Problem.mapGraph)) {
                return ResponseEntity.badRequest().body(new Exception("No se encontro el Ubigeo del camion"));
            }
        }

        List<Order> orderList = new ArrayList<>();
        for (OrderRequest requestOrder : request.getOrderList()) {
            Node node = nodeList.stream().filter(n -> requestOrder.getUbigeo().equals(n.getUbigeo())).findFirst().orElse(null);
            if (Objects.nonNull(node)) {
                Order order = Order.builder().orderId(requestOrder.getId()).destination(node).packageAmount(requestOrder.getPackages())
                        .remainingTime(requestOrder.getRemainingTime()).build();
                orderList.add(order);
                Problem.assignClosest(orderList.get(orderList.size() - 1));
            }
        }

        long start = System.currentTimeMillis();
        List<Order> missingOrders = Problem.routeOrders();
        long finish = System.currentTimeMillis();
        System.out.println("\nAlgorithm time: " + (finish - start) + " ms");

        return createResponse(depotList, missingOrders);
    }

    private boolean readBlockades(List<BlockadeRequest> blockadeList, List<Node> nodeList) {
        if (Objects.isNull(blockadeList))
            return false;

        for (BlockadeRequest blockade : blockadeList) {
            String xUbigeo = blockade.getFirstNode();
            Node x = nodeList.stream().filter(node -> node.getUbigeo().equals(xUbigeo)).findFirst().orElse(null);
            String yUbigeo = blockade.getSecondNode();
            Node y = nodeList.stream().filter(node -> node.getUbigeo().equals(yUbigeo)).findFirst().orElse(null);
            if (Objects.isNull(x) || Objects.isNull(y))
                return true;
            Problem.setBlockades(x.getMatrixIndex(), y.getMatrixIndex(), blockade.getDuration());
        }
        return false;
    }

    private ResponseEntity<AlgorithmResponse> createResponse(List<Depot> depotList, List<Order> orderList) {
        List<DepotResponse> depotResponseList = new ArrayList<>();
        for (Depot d : depotList) {
            List<TruckResponse> truckResponseList = new ArrayList<>();
            double cost = 0.0;
            int load = 0;
            for (Truck t : d.getCurrentFleet()) {
                List<NodeResponse> nodeResponseList = new ArrayList<>();
                List<Integer> orderResponseList = new ArrayList<>();
                for (Visited n : t.getRoute()) {
                    nodeResponseList.add(NodeResponse.builder().ubigeo(n.getUbigeo()).idOrder(n.getOrderId()).travelCost(n.getTravelCost()).build());
                    if (n.getOrderId() > 0) {
                        orderResponseList.add(n.getOrderId());
                    }
                }

                truckResponseList.add(TruckResponse.builder().id(t.getId()).nodeRoute(nodeResponseList).load(t.getCurrentLoad())
                        .orderList(orderResponseList).cost(t.getCost()).build());
                cost += t.getCost();
                load += t.getCurrentLoad();
            }
            depotResponseList.add(DepotResponse.builder().ubigeo(d.getUbigeo()).truckList(truckResponseList).city(d.getCity())
                    .depotCost(cost).packagesRouted(load).build());
        }

        List<NodeResponse> missingOrderList = new ArrayList<>();
        for (Order order : orderList) {
            NodeResponse node = NodeResponse.builder().idOrder(order.getOrderId()).ubigeo(order.getDestination().getUbigeo()).build();
            missingOrderList.add(node);
        }
        return ResponseEntity.ok().body(new AlgorithmResponse(depotResponseList, missingOrderList));
    }

    private boolean assignTruck(List<Node> nodeList, List<Depot> depotList, TruckRequest truck, Matrix[][] mapGraph) {
        double minLength = 9999.9;
        String ubigeo = truck.getUbigeo();
        Node node = nodeList.stream().filter(n -> n.getUbigeo().equals(truck.getUbigeo())).findFirst().orElse(null);
        if (Objects.isNull(node))
            return false;

        int x = -1;
        for (int i = 0; i < depotList.size(); i++) {
            Matrix matrixVal = mapGraph[depotList.get(i).getMatrixIndex()][node.getMatrixIndex()];
            if (matrixVal.getHeuristicValue() < minLength && matrixVal.getHeuristicValue() > 0) {
                minLength = matrixVal.getHeuristicValue();
                x = i;
            }
        }
        if (x != -1) {
            Node n = findNode(nodeList, ubigeo);
            if (Objects.isNull(n))
                return false;
            depotList.get(x).getCurrentFleet().add(0, new Truck(truck.getId(), n.getMatrixIndex(), truck.getMaxLoad()));
        }
        return true;
    }

    private void splitPackages(List<OrderRequest> orderList, List<TruckRequest> truckList) {
        int gcd = truckList.get(0).getMaxLoad();
        int packages;

        for (int i = 1; i < truckList.size(); i++) {
            TruckRequest truck = truckList.get(i);
            gcd = calculateGcd(gcd, truck.getMaxLoad());
        }

        for (int i = 0; i < orderList.size(); i++) {
            OrderRequest order = orderList.get(i);
            packages = order.getPackages();
            if (packages > gcd) {
                order.setPackages(gcd);
                packages -= gcd;
                orderList.add(i--, OrderRequest.builder().id(order.getId()).packages(packages)
                        .remainingTime(order.getRemainingTime()).ubigeo(order.getUbigeo()).build());
            }
        }

    }

    private Node findNode(List<Node> nodeList, String ubigeo) {
        return nodeList.stream().filter(node -> node.getUbigeo().equals(ubigeo)).findFirst().orElse(null);
    }

    private int calculateGcd(int a, int b) {
        if (a == 0) return b;
        if (b == 0) return a;
        return calculateGcd(b, a % b);
    }
}
