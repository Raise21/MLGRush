package de.daniel.mlgrush.configuration;

import com.google.common.collect.Maps;
import de.daniel.mlgrush.location.SerializableLocation;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MLGRushMap {

    private Map< String, SerializableLocation > teamSpawnsLocations = new HashMap<>( );

    private Map< String, MLGRushBed > bedLocations = Maps.newConcurrentMap( );

    private String name = "Mapname";

    private String type;

    private int death;

    private int blocklimit;

    private SerializableLocation spectatorLocation;

}
