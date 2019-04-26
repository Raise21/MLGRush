package de.daniel.mlgrush.countdown.restart;

import de.daniel.mlgrush.MLGRush;
import de.daniel.mlgrush.countdown.MLGRushCountdown;
import de.daniel.mlgrush.scoreboard.RestartScoreboard;
import de.daniel.mlgrush.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class RestartCountdown extends MLGRushCountdown {

    private final RestartScoreboard restartScoreboard = new RestartScoreboard( );

    public RestartCountdown( ) {
        super( GameState.RESTART, null );

        addCountdownHook( count -> {
            for ( final Player allPlayer : Bukkit.getOnlinePlayers( ) ) {
                MLGRush.getInstance( ).getSession( ).showPlayers( allPlayer );
                if ( MLGRush.getInstance( ).getSession( ).getCachedWinner( ) == null ) {
                    allPlayer.sendTitle( "§cKeiner", "§7hat §bMLGRush §7gewonnen" );
                } else {
                    allPlayer.sendTitle( "§a" + MLGRush.getInstance( ).getSession( ).getCachedWinner( ),
                            "§7hat §bMLGRush §7gewonnen" );
                }
                restartScoreboard.sendSidebar( allPlayer, MLGRush.getInstance( ).getSession( ).getCachedWinner( ) );
                restartScoreboard.sendTablist( allPlayer );
                allPlayer.setMaxHealth( 20 );
                allPlayer.setAllowFlight( false );
                allPlayer.getInventory( ).clear( );
                allPlayer.setFlying( false );
                allPlayer.setHealth( 20 );
                allPlayer.setFoodLevel( 40 );
            }

            MLGRush.getSERVICE( ).execute( ( ) -> {
                Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {
                    all.teleport( MLGRush.getInstance( ).getSession( ).getConfiguration( ).getLobbyLocation( )
                            .toLocation( ) );
                } );
            } );

        }, 20 );

        addCountdownHook( count -> {
            for ( final Player allPlayer : Bukkit.getOnlinePlayers( ) ) {
                MLGRush.getSERVICE( ).execute( ( ) -> {
                    allPlayer.sendMessage( MLGRush.getPREFIX( ) + "Die §eServer §7stoppt in §b§l" + count
                            + " §7Sekunde" + ( count == 1 ? "§8." : "n§8." ) );
                    allPlayer.playSound( allPlayer.getLocation( ), Sound.NOTE_PLING, 10, 3 );
                } );
            }
        }, 20, 10, 5, 4, 3, 2, 1 );

        addCountdownHook( count -> {
            for ( final Player allPlayer : Bukkit.getOnlinePlayers( ) ) {
                allPlayer.kickPlayer( MLGRush.getPREFIX( ) + "§cDer Server startet neu§8." );
            }
            Bukkit.getScheduler( ).runTaskLater( MLGRush.getInstance( ), ( ) -> {
                Bukkit.getServer( ).shutdown( );
            }, 4 );
        }, 0 );
    }

    @Override
    public void onTick( int time ) {
        if ( Bukkit.getOnlinePlayers( ).size( ) == 0 ) {
            Bukkit.getServer( ).shutdown( );
        }
    }
}
