package de.daniel.mlgrush.listener.drop;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {

    @EventHandler
    public void onDrop( final PlayerDropItemEvent event ) {
        event.setCancelled( true );
    }
}
