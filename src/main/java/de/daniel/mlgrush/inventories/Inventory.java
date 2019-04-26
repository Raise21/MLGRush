package de.daniel.mlgrush.inventories;

import org.bukkit.entity.Player;

import java.util.concurrent.ExecutorService;

public interface Inventory {

    void load( final Player player, final ExecutorService service );

}
