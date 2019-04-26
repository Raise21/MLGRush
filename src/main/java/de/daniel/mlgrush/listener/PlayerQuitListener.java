package de.daniel.mlgrush.listener;

import de.dytanic.cloudnet.bridge.CloudServer;
import de.daniel.mlgrush.MLGRush;
import de.daniel.mlgrush.scoreboard.LobbyScoreboard;
import de.daniel.mlgrush.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final LobbyScoreboard lobbyScoreboard = new LobbyScoreboard( );

    @EventHandler
    public void onQuit( final PlayerQuitEvent event ) {
        final Player player = event.getPlayer( );

        if ( MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.WAITING ||
                MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.LOBBY ) {
            final int i = Bukkit.getOnlinePlayers( ).size( ) - 1;

            event.setQuitMessage( MLGRush.getPREFIX( ) +
                    "§e" + event.getPlayer( ).getName( ) + " §7hat das Spiel verlassen §8(§b" + i + "§8/§e2" + "§8)" );

            final String team = MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) );

            if ( team != null ) {
                MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get( team ).removePlayer( player );
                MLGRush.getInstance( ).getSession( ).getTeam( ).remove( player.getUniqueId( ) );
            }


            if ( i < 2 ) {
                GameState.toGameState( GameState.WAITING );
                CloudServer.getInstance( ).setMotdAndUpdate( "Voting " + MLGRush.getInstance( ).getSession( )
                        .getCurrentMap( ).getType( ) );
            }

            Bukkit.getOnlinePlayers( ).forEach( ( all ) -> lobbyScoreboard.updatePlayers( all, i, all.getScoreboard( ) ) );

        } else if ( MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.IN_GAME ) {
            event.setQuitMessage( null );
            if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) )
                    .equalsIgnoreCase( "Rot" ) ) {
                MLGRush.getInstance( ).getSession( ).setCachedWinner( MLGRush.getInstance( ).getSession( )
                        .getPlayerMap( ).get( "Blau" ).getName( ) );
            } else {
                MLGRush.getInstance( ).getSession( ).setCachedWinner( MLGRush.getInstance( ).getSession( )
                        .getPlayerMap( ).get( "Rot" ).getName( ) );
            }
            MLGRush.getInstance( ).getSession( ).getPlayers( ).remove( player.getUniqueId( ) );
            if ( MLGRush.getInstance( ).getSession( ).getPlayers( ).size( ) == 1 ) {
                GameState.toGameState( GameState.RESTART );
            }
        } else {
            event.setQuitMessage( null );
        }
    }
}
