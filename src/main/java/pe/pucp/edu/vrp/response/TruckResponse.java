package pe.pucp.edu.vrp.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckResponse {
    private Integer id;
    private List<SubOrderResponse> orderList;
    private List<NodeResponse> nodeRoute;
    private double load;
    private double cost;
}
