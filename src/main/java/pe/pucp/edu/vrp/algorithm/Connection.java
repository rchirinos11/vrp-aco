package pe.pucp.edu.vrp.algorithm;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Connection {
    private int xIndex;
    private int yIndex;
    private boolean blocked;
}
