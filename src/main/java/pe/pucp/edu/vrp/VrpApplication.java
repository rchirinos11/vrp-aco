package pe.pucp.edu.vrp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pe.pucp.edu.vrp.algorithm.Node;
import pe.pucp.edu.vrp.algorithm.Problem;

import java.io.FileNotFoundException;
import java.io.PrintStream;
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
		orderList.add(new Node(1,16,3));
		orderList.add(new Node(2,3,1));
		orderList.add(new Node(3,10,3));
		orderList.add(new Node(4,4,2));
		orderList.add(new Node(5,49,8));
		orderList.add(new Node(6,30,5));
		orderList.add(new Node(7,22,8));
		orderList.add(new Node(8,9,4));
		orderList.add(new Node(9,24,4));
		orderList.add(new Node(10,32,4));
		orderList.add(new Node(11,12,4));
		orderList.add(new Node(12,5,4));
		orderList.add(new Node(13,44,4));
		orderList.add(new Node(14,18,4));
		orderList.add(new Node(15,39,4));
		orderList.add(new Node(16,6,4));
		long start = System.currentTimeMillis();
		double traveled = problem.routeOrders(orderList);
		long finish = System.currentTimeMillis();
		System.out.println("\nElapsed time: " + (finish - start) + " ms");
		System.out.println("Total traveled: " + traveled);

	}
}
