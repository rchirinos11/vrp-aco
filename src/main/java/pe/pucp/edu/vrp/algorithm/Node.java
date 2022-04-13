package pe.pucp.edu.vrp.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
