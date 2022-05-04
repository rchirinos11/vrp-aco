package pe.pucp.edu.vrp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Region {
    COAST("Costa", 15.0, 24),
    MOUNTAIN("Sierra", 25.0, 48),
    JUNGLE("Selva", 35.0, 72);

    private String name;
    private double speed;
    private int maxHours;
}
