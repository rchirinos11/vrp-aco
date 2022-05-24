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
public class DepotResponse {
    private String ubigeo;
    private String city;
    private List<TruckResponse> truckList;
    private int packagesRouted;
    private double depotCost;
}
