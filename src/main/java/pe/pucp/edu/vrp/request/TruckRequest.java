package pe.pucp.edu.vrp.request;

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
public class TruckRequest {
    private Integer id;
    private String depot;
    private double maxLoad;
    private char type;
    private boolean condition;
}
