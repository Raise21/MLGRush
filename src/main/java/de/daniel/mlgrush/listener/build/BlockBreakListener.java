package de.daniel.mlgrush.listener.build;

import de.daniel.mlgrush.scoreboard.IngameScoreboard;
import de.daniel.mlgrush.state.GameState;
import de.daniel.mlgrush.team.TeamManager;
import de.daniel.mlgrush.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.concurrent.Executors;

public class BlockBreakListener implements Listener {

    private final IngameScoreboard ingameScoreboard = new IngameScoreboard( );

    @EventHandler
    public void onBreak( final BlockBreakEvent event ) {
        if ( MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.IN_GAME ) {
            if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( event.getPlayer( ).getUniqueId( ) ) != null ) {
                final String team = MLGRush.getInstance( ).getSession( ).getTeam( ).get( event.getPlayer( ).getUniqueId( ) );
                if ( team.equals( "Spectator" ) ) {
                    event.setCancelled( true );
                    return;
                }
            }

            if ( event.getBlock( ).getType( ) == Material.BED_BLOCK ) {
                event.setCancelled( true );
                for ( final String strings : MLGRush.getInstance( ).getSession( ).getTeams( ) ) {
                    if ( compareLocation( MLGRush.getInstance( ).getSession( ).getCurrentMap( ).getBedLocations( )
                            .get( strings ).getFirstLocation( ).toLocation( ), event.getBlock( ).getLocation( ) )
                            || compareLocation( MLGRush.getInstance( ).getSession( ).getCurrentMap( ).getBedLocations( )
                            .get( strings ).getSecondLocation( ).toLocation( ), event.getBlock( ).getLocation( ) ) ) {
                        if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( event.getPlayer( ).getUniqueId( ) )
                                .equalsIgnoreCase( strings ) ) {
                            event.getPlayer( ).sendMessage( MLGRush.getPREFIX( ) + "§cDu kannst dein Bett nicht abbauen§8!" );
                            return;
                        }
                    }
                }

                if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( event.getPlayer( ).getUniqueId( ) )
                        .equalsIgnoreCase( "Rot" ) ) {
                    MLGRush.getInstance( ).getSession( ).getPlayerMap( ).get( "Rot" ).setKills(
                            MLGRush.getInstance( ).getSession( ).getPlayerMap( ).get( "Rot" ).getKills( ) + 1
                    );

                    if ( MLGRush.getInstance( ).getSession( ).getPlayerMap( ).get( "Rot" ).getKills( ) == 10 ) {
                        MLGRush.getInstance( ).getSession( ).setCachedWinner( MLGRush.getInstance( ).getSession( )
                                .getPlayerMap( ).get( "Rot" ).getName( ) );
                        GameState.toGameState( GameState.RESTART );
                    }

                } else {
                    MLGRush.getInstance( ).getSession( ).getPlayerMap( ).get( "Blau" ).setKills(
                            MLGRush.getInstance( ).getSession( ).getPlayerMap( ).get( "Blau" ).getKills( ) + 1
                    );

                    if ( MLGRush.getInstance( ).getSession( ).getPlayerMap( ).get( "Blau" ).getKills( ) == 10 ) {
                        MLGRush.getInstance( ).getSession( ).setCachedWinner( MLGRush.getInstance( ).getSession( )
                                .getPlayerMap( ).get( "Blau" ).getName( ) );
                        GameState.toGameState( GameState.RESTART );
                    }
                }

                final String team = MLGRush.getInstance( ).getSession( ).getTeam( ).get( event.getPlayer( )
                        .getUniqueId( ) );
                final TeamManager manager = MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get( team );
                for ( final String strings : MLGRush.getInstance( ).getSession( ).getTeams( ) ) {
                    if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( event.getPlayer( ).getUniqueId( ) )
                            .equalsIgnoreCase( strings ) ) {
                        if ( strings.equalsIgnoreCase( "Rot" ) ) {
                            Bukkit.broadcastMessage( MLGRush.getPREFIX( ) + manager.getPrefix( ) + event.getPlayer( )
                                    .getName( ) +
                                    " §7hat das Bett von §9Team Blau §7zerstört§8." );
                        } else {
                            Bukkit.broadcastMessage( MLGRush.getPREFIX( ) + manager.getPrefix( ) + event.getPlayer( )
                                    .getName( ) +
                                    " §7hat das Bett von §cTeam Rot §7zerstört§8." );
                        }
                    }
                }

                for ( final Location location : MLGRush.getInstance( ).getSession( ).getBlocks( ) ) {
                    final Block block = location.getBlock( );
                    block.setType( Material.AIR );
                }

                Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {
                    ingameScoreboard.updateKills( all, all.getScoreboard( ) );
                    all.getInventory( ).clear( );
                    MLGRush.getInstance( ).getSession( ).getPlayerGameInventory( ).load( all, Executors.newCachedThreadPool( ) );
                    all.teleport( MLGRush.getInstance( ).getSession( ).getCurrentMap( ).getTeamSpawnsLocations( ).
                            get( MLGRush.getInstance( ).getSession( ).getTeam( ).get( all.getUniqueId( ) ) ).toLocation( ) );

                    all.setMaxHealth( 20 );
                    all.setHealth( 20 );
                } );

                for ( final Player all : Bukkit.getOnlinePlayers( ) ) {
                    all.playSound( all.getLocation( ), Sound.ENDERDRAGON_GROWL, 7f, 7f );
                }
            } else if ( event.getBlock( ).getType( ) == Material.SANDSTONE ) {
                if ( MLGRush.getInstance( ).getSession( ).getBlocks( ).contains( event.getBlock( ).getLocation( ) ) ) {
                    event.setCancelled( false );
                    event.getBlock( ).setType( Material.AIR );
                    event.getBlock( ).getDrops( ).clear( );
                } else {
                    event.setCancelled( true );
                }
            } else {
                event.setCancelled( true );
            }
        } else {
            event.setCancelled( true );
        }
    }

    private boolean compareLocation( final Location firstLocation, final Location secondLocation ) {
        if ( firstLocation.getBlockX( ) != secondLocation.getBlockX( ) ) {
            return false;
        }

        if ( firstLocation.getBlockY( ) != secondLocation.getBlockY( ) ) {
            return false;
        }

        if ( firstLocation.getBlockZ( ) != secondLocation.getBlockZ( ) ) {
            return false;
        }

        return true;
    }
}