package pe.pucp.edu.vrp.algorithm;

import lombok.*;
import pe.pucp.edu.vrp.util.Region;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Node {
    private String ubigeo;
    private double longitude;
    private double latitude;
    private String department;
    private String city;
    private Region region;
    private int matrixIndex;

    public String toString() {
        return city;
    }
}
