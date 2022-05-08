package pe.pucp.edu.vrp.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Order {
    private int orderId;
    private Node destination;
    private int packageAmount;

    @Override
    public String toString() {
        return String.valueOf(destination.getMatrixIndex());
    }
}
