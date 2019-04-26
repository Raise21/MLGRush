package de.daniel.mlgrush.listener.damage;

import de.daniel.mlgrush.state.GameState;
import de.daniel.mlgrush.MLGRush;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onDamage( final EntityDamageEvent event ) {

        if ( MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.WAITING ||
                MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.LOBBY ) {
            event.setCancelled( true );
        } else if ( MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.IN_GAME ) {
            event.setDamage( 0.0 );
        } else {
            event.setCancelled( true );
        }
    }

    @EventHandler
    public void onDamage( final EntityDamageByEntityEvent event ) {
        if ( event.getEntity( ) instanceof Player && event.getDamager( ) instanceof Player ) {
            final Player player = ( Player ) event.getEntity( );
            final Player damager = ( Player ) event.getDamager( );
            if ( MLGRush.getInstance( ).getSession( ).getSpectators( ).contains( player.getUniqueId( ) ) &&
                    MLGRush.getInstance( ).getSession( ).getSpectators( )
                            .contains( damager.getUniqueId( ) ) ) {
                event.setCancelled( true );
            }

            if ( MLGRush.getInstance( ).getSession( ).getSpectators( ).contains( damager.getUniqueId( ) )
                    && ( !( MLGRush.getInstance( ).getSession( ).getSpectators( )
                    .contains( player.getUniqueId( ) ) ) ) ) {
                event.setCancelled( true );
            }

            event.setCancelled( false );
            MLGRush.getInstance( ).getSession( ).getCache( ).put( player, damager );
        }
    }

}
