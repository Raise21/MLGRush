package de.daniel.mlgrush.listener;

import de.daniel.mlgrush.state.GameState;
import de.daniel.mlgrush.MLGRush;
import de.daniel.mlgrush.scoreboard.IngameScoreboard;
import de.daniel.mlgrush.scoreboard.LobbyScoreboard;
import de.daniel.mlgrush.scoreboard.RestartScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.concurrent.Executors;

public class PlayerJoinListener implements Listener {

    private final LobbyScoreboard lobbyScoreboard = new LobbyScoreboard( );

    private final IngameScoreboard ingameScoreboard = new IngameScoreboard( );

    private final RestartScoreboard restartScoreboard = new RestartScoreboard( );

    @EventHandler
    public void onJoin( final PlayerJoinEvent event ) {
        final Player player = event.getPlayer( );
        if ( MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.WAITING ||
                MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.LOBBY ) {
            player.setMaxHealth( 20 );
            player.setHealth( 20 );
            player.setFoodLevel( 40 );


            int i = Bukkit.getOnlinePlayers( ).size( );
            lobbyScoreboard.sendSidebar( player, i );
            lobbyScoreboard.sendTablist( player );

            MLGRush.getInstance( ).getSession( ).getPlayerJoinInventory( ).load( player, Executors.newCachedThreadPool( ) );

            event.setJoinMessage( MLGRush.getPREFIX( ) +
                    "§e" + player.getName( ) + " §7ist dem Spiel beigetreten §8(§b" + Bukkit.getOnlinePlayers( )
                    .size( ) + "§8/§e2" + "§8)" );

            MLGRush.getSERVICE( ).execute( ( ) -> {
                Bukkit.getScheduler( ).runTask( MLGRush.getInstance( ), ( ) -> {
                    player.setGameMode( GameMode.ADVENTURE );
                    if ( MLGRush.getInstance( ).getSession( ).getConfiguration( ).getLobbyLocation( ) != null ) {
                        player.teleport( MLGRush.getInstance( ).getSession( ).getConfiguration( ).getLobbyLocation( )
                                .toLocation( ) );
                    }
                } );
            } );

            if ( i == 2 ) {
                if ( MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.WAITING ) {
                    GameState.toGameState( GameState.LOBBY );
                    GameState.CURRENT_COUNTDOWN.forceTime( 15 );
                }
            }

            Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {
                lobbyScoreboard.updatePlayers( all, i, all.getScoreboard( ) );
            } );
        } else if ( MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.IN_GAME ) {
            event.setJoinMessage( null );
            player.getInventory( ).clear( );
            MLGRush.getInstance( ).getSession( ).getTeam( ).put( player.getUniqueId( ), "Spectator" );
            MLGRush.getInstance( ).getSession( ).getSpectators( ).add( player.getUniqueId( ) );
            ingameScoreboard.sendSidebar( player, Bukkit.getOnlinePlayers( ).size( ) );
            ingameScoreboard.sendTablist( player );
            MLGRush.getInstance( ).getSession( ).hidePlayers( player );
            MLGRush.getInstance( ).getSession( ).setSpectator( player );
        } else {
            event.setJoinMessage( null );
            player.getInventory( ).clear( );
            restartScoreboard.sendSidebar( player, MLGRush.getInstance( ).getSession( ).getCachedWinner( ) );
            restartScoreboard.sendTablist( player );
            player.setMaxHealth( 20 );
            player.setAllowFlight( false );
            player.setFlying( false );
            player.setHealth( 20 );
            player.setFoodLevel( 40 );
            player.teleport( MLGRush.getInstance( ).getSession( ).getConfiguration( ).getLobbyLocation( ).toLocation( ) );
        }

    }

}
