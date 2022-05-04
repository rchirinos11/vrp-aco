package pe.pucp.edu.vrp.algorithm;

import lombok.*;
import pe.pucp.edu.vrp.Constant;
import pe.pucp.edu.vrp.Region;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Node {
    private int orderId;
    private double longitude;
    private double latitude;
    private Region region;
    private int ubigeo;
    private int matrixIndex;
    private int packageAmount;
    private double totalWeight;

    public String imprimirOfi() {
        return (matrixIndex + "," + ubigeo + "," + longitude + "," + latitude + "," + region);
    }

    public Node(int orderId, int matrixIndex, int packageAmount, Region region, double longitude, double latitude, int ubigeo) {
        this.orderId = orderId;
        this.matrixIndex = matrixIndex;
        this.packageAmount = packageAmount;
        this.totalWeight = Constant.PACKAGEWEIGHT * packageAmount;
        this.longitude = longitude;
        this.latitude = latitude;
        this.ubigeo = ubigeo;
        this.region = region;
    }

    public String toString() {
        return String.format("%d", matrixIndex);
    }
}
