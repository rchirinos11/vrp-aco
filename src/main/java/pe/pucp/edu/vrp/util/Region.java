package pe.pucp.edu.vrp.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Region {
    COSTA(24),
    SIERRA(48),
    SELVA(72);

    private final int maxHours;
}
