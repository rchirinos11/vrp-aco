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
		problem.initParams(5);
		List<Node> orderList = new ArrayList<>();
		orderList.add(new Node(0,0,0));
		orderList.add(new Node(1,1,3));
		orderList.add(new Node(2,2,1));
		orderList.add(new Node(3,3,2));
		orderList.add(new Node(4,4,5));
		problem.route(orderList);
	}

}
