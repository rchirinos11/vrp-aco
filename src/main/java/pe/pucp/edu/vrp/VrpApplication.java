package pe.pucp.edu.vrp;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import pe.pucp.edu.vrp.algorithm.Connection;
import pe.pucp.edu.vrp.algorithm.Node;
import pe.pucp.edu.vrp.algorithm.Problem;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class VrpApplication {
    public static void main(String[] args) throws FileNotFoundException {
//		SpringApplication.run(VrpApplication.class, args);
//		System.setOut(new PrintStream("output"));
        Problem problem = new Problem();
        problem.initParams(10);
        List<Node> orderList = new ArrayList<>();
        orderList.add(new Node(1, 16, 3, Region.COAST));
        orderList.add(new Node(2, 3, 1, Region.COAST));
        orderList.add(new Node(3, 10, 3, Region.MOUNTAIN));
        orderList.add(new Node(4, 4, 2, Region.JUNGLE));
        orderList.add(new Node(5, 49, 8, Region.MOUNTAIN));
        orderList.add(new Node(6, 30, 5, Region.COAST));
        orderList.add(new Node(7, 22, 4, Region.JUNGLE));
        orderList.add(new Node(8, 9, 7, Region.JUNGLE));
        orderList.add(new Node(9, 24, 1, Region.MOUNTAIN));
        orderList.add(new Node(10, 32, 2, Region.COAST));
        orderList.add(new Node(11, 12, 5, Region.JUNGLE));
        orderList.add(new Node(12, 5, 4, Region.JUNGLE));
        orderList.add(new Node(13, 44, 1, Region.MOUNTAIN));
        orderList.add(new Node(14, 18, 4, Region.MOUNTAIN));
        orderList.add(new Node(15, 39, 3, Region.JUNGLE));
        orderList.add(new Node(16, 6, 6, Region.COAST));

        //WIP
        List<Connection> connections = new ArrayList<>();
        connections.add(Connection.builder().xIndex(0).yIndex(1).build());

        long start = System.currentTimeMillis();
        double traveled = problem.routeOrders(orderList);
        long finish = System.currentTimeMillis();
        System.out.println("\nElapsed time: " + (finish - start) + " ms");
        System.out.printf("Total time traveled: %3.2f h", traveled);

    }
}
