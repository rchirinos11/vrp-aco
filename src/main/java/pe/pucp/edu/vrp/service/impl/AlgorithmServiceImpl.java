package pe.pucp.edu.vrp.service.impl;

import org.springframework.stereotype.Service;
import pe.pucp.edu.vrp.algorithm.Depot;
import pe.pucp.edu.vrp.algorithm.Node;
import pe.pucp.edu.vrp.algorithm.Order;
import pe.pucp.edu.vrp.algorithm.Problem;
import pe.pucp.edu.vrp.algorithm.Truck;
import pe.pucp.edu.vrp.request.AlgorithmRequest;
import pe.pucp.edu.vrp.request.OrderRequest;
import pe.pucp.edu.vrp.request.TruckRequest;
import pe.pucp.edu.vrp.response.AlgorithmResponse;
import pe.pucp.edu.vrp.response.DepotResponse;
import pe.pucp.edu.vrp.response.NodeResponse;
import pe.pucp.edu.vrp.response.TruckResponse;
import pe.pucp.edu.vrp.service.AlgorithmService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AlgorithmServiceImpl implements AlgorithmService {
    @Override
    public AlgorithmResponse routeTrucks(AlgorithmRequest request) {
        Problem.resetDepots();

        List<Order> orderList = new ArrayList<>();
        List<Node> nodeList = Problem.nodeList;
        for (OrderRequest order : request.getOrderList()) {
            Node node = nodeList.stream().filter(n -> order.getUbigeo().equals(n.getUbigeo())).findFirst().orElse(null);
            if (Objects.nonNull(node)) {
                orderList.add(Order.builder().orderId(order.getId()).destination(node).packageAmount(order.getPackages()).build());
                Problem.assignClosest(orderList.get(orderList.size() - 1));
            }
        }

        List<Depot> depotList = Problem.depotList;
        for (TruckRequest truck : request.getTruckList()) {
            Depot depot = depotList.stream().filter(d -> d.getUbigeo().equals(truck.getDepot())).findFirst().orElse(null);
            if (Objects.nonNull(depot))
                depot.getCurrentFleet().add(new Truck(truck.getId(), truck.getMaxLoad()));
        }

        long start = System.currentTimeMillis();
        double traveled = Problem.routeOrders();
        long finish = System.currentTimeMillis();
        System.out.println("\nAlgorithm time: " + (finish - start) + " ms");
        System.out.printf("Total time traveled: %3.2f h\n", traveled);

        List<DepotResponse> depotResponseList = new ArrayList<>();
        for (Depot d : depotList) {
            List<TruckResponse> truckResponseList = new ArrayList<>();
            for (Truck t : d.getCurrentFleet()) {
                List<NodeResponse> nodeResponseList = new ArrayList<>();
                for (Node n : t.getNodeRoute())
                    nodeResponseList.add(NodeResponse.builder().ubigeo(n.getUbigeo()).build());

                truckResponseList.add(TruckResponse.builder().id(t.getId()).nodeRoute(nodeResponseList).load(t.getCurrentLoad())
                        .cost(0.0).build());
            }
            depotResponseList.add(DepotResponse.builder().ubigeo(d.getUbigeo()).truckList(truckResponseList).city(d.getCity()).build());
        }
        return AlgorithmResponse.builder().depotList(depotResponseList).build();
    }
}
