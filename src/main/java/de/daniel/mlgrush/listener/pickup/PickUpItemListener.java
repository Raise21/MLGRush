package de.daniel.mlgrush.listener.pickup;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PickUpItemListener implements Listener {

    @EventHandler
    public void onPickup( final PlayerPickupItemEvent event ) {
        event.setCancelled( true );
    }
}
