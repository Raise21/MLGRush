package de.daniel.mlgrush.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class PlayerBedEnterListener implements Listener {

    @EventHandler
    public void onBed( final PlayerBedEnterEvent enterEvent ) {
        enterEvent.setCancelled( true );
    }

}
