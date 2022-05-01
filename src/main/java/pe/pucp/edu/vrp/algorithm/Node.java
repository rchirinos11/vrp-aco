package pe.pucp.edu.vrp.algorithm;

import lombok.*;
import pe.pucp.edu.vrp.Constant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Node {
    private int orderId;
    private int ubigeo;
    private double xCoordinate;
    private double yCoordinate;
    private int matrixIndex;
    private int packageAmount;
    private double totalWeight;
    private String region;

    public Node(int  matrixIndex, int ubigeo, double xCoordinate, double yCoordinate, String region){
        this.ubigeo=ubigeo;
        this.xCoordinate=xCoordinate;
        this.yCoordinate=yCoordinate;
        this.region=region;
        this.matrixIndex= matrixIndex;
    }
    public String imprimirOfi(){
        return  (matrixIndex+","+ ubigeo+ ","+xCoordinate + "," + yCoordinate+","+region);
    }
    public Node(int orderId, int matrixIndex, int packageAmount) {
        this.orderId = orderId;
        this.matrixIndex = matrixIndex;
        this.packageAmount = packageAmount;
        this.totalWeight = Constant.PACKAGEWEIGHT * packageAmount;
    }

    public String toString() {
            return String.format("%d", matrixIndex);
    }
}
