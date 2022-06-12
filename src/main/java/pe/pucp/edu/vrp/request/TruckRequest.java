package pe.pucp.edu.vrp.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import pe.pucp.edu.vrp.algorithm.Visited;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckRequest {

    @NonNull
    @JsonProperty("id")
    private Integer id;

    @NonNull
    @JsonProperty("ubigeo")
    private String ubigeo;

    @NonNull
    @JsonProperty("maxLoad")
    private Integer maxLoad;

    @NonNull
    @JsonProperty("orderList")
    private List<Visited> orderList;
}
