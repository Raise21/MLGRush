package de.daniel.mlgrush.listener.environment;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {

    @EventHandler
    public void onFood( final FoodLevelChangeEvent event ) {
        event.setCancelled( true );
    }
}
