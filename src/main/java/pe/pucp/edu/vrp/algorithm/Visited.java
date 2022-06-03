package pe.pucp.edu.vrp.algorithm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Visited {
    private int orderId;
    private String ubigeo;
    private int matrixIndex;
    private double travelCost;

    public Visited(String ubigeo, int matrixIndex, double travelCost) {
        orderId = 0;
        this.ubigeo = ubigeo;
        this.matrixIndex = matrixIndex;
        this.travelCost = travelCost;
    }
}
