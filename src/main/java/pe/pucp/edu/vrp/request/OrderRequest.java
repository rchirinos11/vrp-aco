package pe.pucp.edu.vrp.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NonNull
    @JsonProperty("id")
    private Integer id;

    @NonNull
    @JsonProperty("ubigeo")
    private String ubigeo;

    @NonNull
    @JsonProperty("packages")
    private int packages;

    @NonNull
    @JsonProperty("remainingTime")
    private double remainingTime;
}
