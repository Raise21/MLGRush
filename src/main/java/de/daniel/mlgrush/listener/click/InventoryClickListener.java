package de.daniel.mlgrush.listener.click;

import de.daniel.mlgrush.state.GameState;
import de.daniel.mlgrush.MLGRush;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick( final InventoryClickEvent event ) {
        final Player player = ( Player ) event.getWhoClicked( );

        if ( event.getClickedInventory( ) == null ) {
            return;
        }

        if ( MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.WAITING ||
                MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.LOBBY ) {
            event.setCancelled( true );
            if ( event.getClickedInventory( ).getName( ).equalsIgnoreCase( "§8» §eTeams" ) ) {
                final String[] team = event.getCurrentItem( ).getItemMeta( ).getDisplayName( ).split( " " );
                MLGRush.getInstance( ).getTeam( ).join( player, team[ 1 ] );
                player.playSound( player.getLocation( ), Sound.LEVEL_UP, 3f, 3f );
            } else if ( event.getClickedInventory( ).getTitle( ).equalsIgnoreCase( "§8» §eVoting" ) ) {
                if ( event.getCurrentItem( ).getType( ) == Material.EMPTY_MAP ) {
                    final String map = event.getCurrentItem( ).getItemMeta( ).getDisplayName( )
                            .replace( "§e", "" );
                    MLGRush.getInstance( ).getSession( ).getVoting( ).vote( player, map );
                }
            }
        }


    }

}
