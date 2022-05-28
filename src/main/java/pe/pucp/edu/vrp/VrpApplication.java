package pe.pucp.edu.vrp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pe.pucp.edu.vrp.util.Problem;


@SpringBootApplication
public class VrpApplication {
    public static void main(String[] args) throws Exception {
        Problem.initParams();
        SpringApplication.run(VrpApplication.class, args);
    }
}
