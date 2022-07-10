package pe.pucp.edu.vrp.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.pucp.edu.vrp.util.Region;

import java.util.ArrayList;
import java.util.List;

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
    private final List<Connection> connectionList = new ArrayList<>();

    public String toString() {
        return city;
    }
}
