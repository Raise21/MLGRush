package de.daniel.mlgrush.configuration.bed;

import de.daniel.mlgrush.location.SerializableLocation;
import lombok.Data;

@Data
public class MLGRushBed {
    private SerializableLocation firstLocation;
    private SerializableLocation secondLocation;
}
