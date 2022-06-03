package pe.pucp.edu.vrp.response;

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
public class NodeResponse {
    private Integer idOrder;
    private String ubigeo;
    private double travelCost;
}
