package de.daniel.mlgrush.listener.build;

import de.daniel.mlgrush.state.GameState;
import de.daniel.mlgrush.MLGRush;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {


    @EventHandler
    public void onPlace( final BlockPlaceEvent event ) {
        if ( MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.WAITING ||
                MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.LOBBY ) {
            event.setCancelled( true );
            return;
        }

        if ( MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.RESTART ) {
            event.setCancelled( true );
            return;
        }

        if ( event.getBlockPlaced( ).getType( ) == Material.SANDSTONE ) {
            if ( event.getBlockPlaced( ).getLocation( ).getY( ) == MLGRush.getInstance( ).getSession( ).getCurrentMap( )
                    .getBlocklimit( ) ) {
                event.setCancelled( true );
                return;
            }
            if ( event.getBlockPlaced( ).getLocation( ).distance( MLGRush.getInstance( ).getSession( ).getCurrentMap( )
                    .getTeamSpawnsLocations( ).get( "Blau" ).toLocation( ) ) <= 1 ) {
                event.setCancelled( true );
            } else if ( event.getBlockPlaced( ).getLocation( ).distance( MLGRush.getInstance( ).getSession( )
                    .getCurrentMap( )
                    .getTeamSpawnsLocations( ).get( "Rot" ).toLocation( ) ) <= 1 ) {
                event.setCancelled( true );
            } else {
                MLGRush.getInstance( ).getSession( ).getBlocks( ).add( event.getBlockPlaced( ).getLocation( ) );
                event.setCancelled( false );
            }

        } else {
            event.setCancelled( true );
        }

    }

}
