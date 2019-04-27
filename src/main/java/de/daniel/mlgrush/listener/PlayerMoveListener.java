package de.daniel.mlgrush.listener;

import de.daniel.mlgrush.scoreboard.IngameScoreboard;
import de.daniel.mlgrush.state.GameState;
import de.daniel.mlgrush.team.TeamManager;
import de.daniel.mlgrush.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.concurrent.Executors;

public class PlayerMoveListener implements Listener {

    private final IngameScoreboard ingameScoreboard = new IngameScoreboard( );

    @EventHandler
    public void onMove( final PlayerMoveEvent event ) {
        if ( MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.IN_GAME ) {
            if ( !MLGRush.getInstance( ).getSession( ).getSpectators( ).contains( event.getPlayer( ).getUniqueId( ) ) ) {
                if ( event.getPlayer( ).getLocation( ).getY( ) <= MLGRush.getInstance( ).getSession( ).getCurrentMap( )
                        .getDeath( ) ) {
                    final Player deather = event.getPlayer( );
                    final Player killer = MLGRush.getInstance( ).getSession( ).getCache( ).getIfPresent( deather );

                    if ( killer != null ) {
                        MLGRush.getInstance( ).getSession( ).getCache( ).asMap( ).remove( deather );
                        Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {
                            ingameScoreboard.updateKills( all, all.getScoreboard( ) );
                        } );
                        final String deatherTeam = MLGRush.getInstance( ).getSession( ).getTeam( )
                                .get( deather.getUniqueId( ) );
                        final String killerTeam = MLGRush.getInstance( ).getSession( ).getTeam( )
                                .get( killer.getUniqueId( ) );
                        deather.getInventory( ).clear( );
                        MLGRush.getInstance( ).getSession( ).getPlayerGameInventory( ).load( deather,
                                Executors.newCachedThreadPool( ) );
                        final TeamManager teamManager = MLGRush.getInstance( ).getSession( ).getTeamManagerMap( )
                                .get( deatherTeam );

                        Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {
                            all.sendMessage( MLGRush.getPREFIX( ) + teamManager.getPrefix( ) + event.getPlayer( )
                                    .getName( ) + " §7wurde von " +
                                    MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get( killerTeam )
                                            .getPrefix( ) +
                                    event.getPlayer( ).getKiller( ).getName( ) + " §7getötet." );
                        } );
                    } else {
                        final String deatherTeam = MLGRush.getInstance( ).getSession( ).getTeam( )
                                .get( deather.getUniqueId( ) );
                        final TeamManager teamManager = MLGRush.getInstance( ).getSession( ).getTeamManagerMap( )
                                .get( deatherTeam );
                        deather.getInventory( ).clear( );
                        MLGRush.getInstance( ).getSession( ).getPlayerGameInventory( )
                                .load( deather, Executors.newCachedThreadPool( ) );
                        Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {
                            all.sendMessage( MLGRush.getPREFIX( ) + teamManager.getPrefix( )
                                    + event.getPlayer( ).getName( ) + " §7ist gestorben§8." );
                        } );
                    }
                    Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {
                        ingameScoreboard.updateKills( all, all.getScoreboard( ) );
                        deather.teleport( MLGRush.getInstance( ).getSession( ).getCurrentMap( )
                                .getTeamSpawnsLocations( )
                                .get( MLGRush.getInstance( ).getSession( ).getTeam( ).get( deather.getUniqueId( ) ) )
                                .toLocation( ) );

                        all.setMaxHealth( 20 );
                        all.setHealth( 20 );
                    } );
                }
            }
        }
    }

}
