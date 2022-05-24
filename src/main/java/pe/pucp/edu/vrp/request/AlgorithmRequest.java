package pe.pucp.edu.vrp.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class AlgorithmRequest {
    @JsonProperty("orderList")
    private List<OrderRequest> orderList;

    @JsonProperty("truckList")
    private List<TruckRequest> truckList;
}
