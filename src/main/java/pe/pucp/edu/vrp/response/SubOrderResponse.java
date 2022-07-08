package pe.pucp.edu.vrp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubOrderResponse {
  @JsonProperty("orderId")
  private Long orderId;

  @JsonProperty("suborderId")
  private Integer suborderId;

  @JsonProperty("packageAmount")
  private Integer packageAmount;
}
