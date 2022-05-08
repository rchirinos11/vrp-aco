package pe.pucp.edu.vrp;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import pe.pucp.edu.vrp.algorithm.Node;
import pe.pucp.edu.vrp.algorithm.Order;
import pe.pucp.edu.vrp.algorithm.Problem;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class VrpApplication {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
//		SpringApplication.run(VrpApplication.class, args);
//		System.setOut(new PrintStream("output"));
        Problem problem = new Problem();
        problem.initParams(10);

        List<Node> nodeList = problem.getNodeList();
        List<Order> orderList = new ArrayList<>();
        orderList.add(new Order(1, nodeList.get(2), 3));
        orderList.add(new Order(2, nodeList.get(10), 6));
        orderList.add(new Order(3, nodeList.get(4), 1));
        orderList.add(new Order(4, nodeList.get(8), 9));
        orderList.add(new Order(5, nodeList.get(9), 8));
        orderList.add(new Order(6, nodeList.get(46), 9));
        orderList.add(new Order(7, nodeList.get(137), 5));
        orderList.add(new Order(8, nodeList.get(7), 4));
        orderList.add(new Order(9, nodeList.get(38), 3));
        orderList.add(new Order(10, nodeList.get(20), 7));
        orderList.add(new Order(11, nodeList.get(19), 9));
        orderList.add(new Order(12, nodeList.get(26), 2));
        orderList.add(new Order(13, nodeList.get(41), 8));
        orderList.add(new Order(14, nodeList.get(22), 4));
        orderList.add(new Order(15, nodeList.get(31), 3));
        orderList.add(new Order(16, nodeList.get(5), 1));
        orderList.add(new Order(17, nodeList.get(40), 6));
        orderList.add(new Order(18, nodeList.get(128), 6));
        orderList.add(new Order(19, nodeList.get(129), 6));

        double traveled = problem.routeOrders(orderList);
        long finish = System.currentTimeMillis();
        System.out.println("\nElapsed time: " + (finish - start) + " ms");
        System.out.printf("Total time traveled: %3.2f h\n", traveled);
    }
}
