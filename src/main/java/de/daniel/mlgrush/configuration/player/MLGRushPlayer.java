package de.daniel.mlgrush.configuration.player;

import lombok.Data;

import java.util.UUID;

@Data
public class MLGRushPlayer {

    private final UUID uuid;
    private final String name;
    private int kills;

}
