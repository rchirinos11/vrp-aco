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
    private int xCoordinate;
    private int yCoordinate;
    private int matrixIndex;
    private int packageAmount;
    private double totalWeight;

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
