package pe.pucp.edu.vrp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pe.pucp.edu.vrp.algorithm.Node;
import pe.pucp.edu.vrp.algorithm.Problem;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class VrpApplication {
	public static void main(String[] args) {
		SpringApplication.run(VrpApplication.class, args);
		Problem problem = new Problem();
		problem.initParams(10);
		List<Node> orderList = new ArrayList<>();
		orderList.add(new Node(1,7,3));
		orderList.add(new Node(2,4,1));
		orderList.add(new Node(3,9,3));
		orderList.add(new Node(4,3,2));
		orderList.add(new Node(5,6,8));
		orderList.add(new Node(6,5,5));
		orderList.add(new Node(7,8,4));
		problem.routeOrders(orderList);
	}

}
