package de.daniel.mlgrush.listener.chat;

import de.daniel.mlgrush.state.GameState;
import de.daniel.mlgrush.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void onChat( final AsyncPlayerChatEvent event ) {
        if ( MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.WAITING ||
                MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.LOBBY ) {
            event.setCancelled( true );
            Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {
                all.sendMessage( "§7" + event.getPlayer( ).getName( ) + " §8» §7" + event.getMessage( ) );
            } );

        } else if ( MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.IN_GAME ) {

            if ( MLGRush.getInstance( ).getSession( ).getSpectators( ).contains( event.getPlayer( ).getUniqueId( ) ) ) {
                Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {
                    if ( MLGRush.getInstance( ).getSession( ).getSpectators( ).contains( all.getUniqueId( ) ) ) {
                        event.setCancelled( true );
                        all.sendMessage( "§7" + event.getPlayer( ).getName( ) + " §8» §7" + event.getMessage( ) );
                    }
                } );
            } else {
                if ( event.getMessage( ).contains( "%" ) ) {
                    event.setCancelled( true );
                    if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( event.getPlayer( ).getUniqueId( ) )
                            .equalsIgnoreCase( "Rot" ) ) {
                        Bukkit.broadcastMessage( MLGRush.getInstance( ).getSession( ).getTeamManagerMap( )
                                .get( "Rot" ).getPrefix( )
                                + event.getPlayer( ).getName( ) + " §8» §7" + event.getMessage( ) );
                    } else {
                        event.setCancelled( true );
                        Bukkit.broadcastMessage( MLGRush.getInstance( ).getSession( ).getTeamManagerMap( )
                                .get( "Blau" ).getPrefix( )
                                + event.getPlayer( ).getName( ) + " §8» §7" + event.getMessage( ) );
                    }
                    return;
                }
            }


            if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( event.getPlayer( ).getUniqueId( ) )
                    .equalsIgnoreCase( "Rot" ) ) {
                event.setFormat( MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get( "Rot" ).getPrefix( )
                        + event.getPlayer( ).getName( ) + " §8» §7" + event.getMessage( ) );
            } else {
                event.setFormat( MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get( "Blau" ).getPrefix( )
                        + event.getPlayer( ).getName( ) + " §8» §7" + event.getMessage( ) );
            }
        } else {
            event.setCancelled( true );
            Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {
                all.sendMessage( "§7" + event.getPlayer( ).getName( ) + " §8» §7" + event.getMessage( ) );
            } );
        }
    }
}
