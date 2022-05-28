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
public class OrderRequest {
    private Integer id;
    private String ubigeo;
    private int packages;
    private double remainingTime;
}
