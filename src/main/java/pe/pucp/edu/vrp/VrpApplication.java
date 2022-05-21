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
//		SpringApplication.run(VrpApplication.class, args);
//		System.setOut(new PrintStream("output"));
        Problem problem = new Problem();
        problem.initParams();

        long start = System.currentTimeMillis();
        double traveled = problem.routeOrders();
        long finish = System.currentTimeMillis();
        System.out.println("\nElapsed time: " + (finish - start) + " ms");
        System.out.printf("Total time traveled: %3.2f h\n", traveled);
    }
}
