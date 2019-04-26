package de.daniel.mlgrush.countdown.lobby;

import de.dytanic.cloudnet.bridge.CloudServer;
import de.dytanic.cloudnet.lib.server.ServerState;
import de.daniel.mlgrush.MLGRush;
import de.daniel.mlgrush.configuration.player.MLGRushPlayer;
import de.daniel.mlgrush.countdown.MLGRushCountdown;
import de.daniel.mlgrush.scoreboard.IngameScoreboard;
import de.daniel.mlgrush.scoreboard.LobbyScoreboard;
import de.daniel.mlgrush.state.GameState;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.Executors;

public class LobbyCountdown extends MLGRushCountdown {


    private final LobbyScoreboard lobbyScoreboard = new LobbyScoreboard( );
    private final IngameScoreboard ingameScoreboard = new IngameScoreboard( );

    private void sendAllActionBar( final String message ) {
        final String string = ChatColor.translateAlternateColorCodes( '&', message );
        final IChatBaseComponent chatBaseComponent = IChatBaseComponent.ChatSerializer.a( "{\"text\": \"" + string + "\"}" );
        final PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat( chatBaseComponent, ( byte ) 2 );

        Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {
            ( ( CraftPlayer ) all ).getHandle( ).playerConnection.sendPacket( packetPlayOutChat );
        } );
    }

    public LobbyCountdown( ) {
        super( GameState.LOBBY, GameState.IN_GAME );

        addCountdownHook( count -> {
            for ( final Player allPlayer : Bukkit.getOnlinePlayers( ) ) {
                MLGRush.getSERVICE( ).execute( ( ) -> {
                    allPlayer.sendMessage( MLGRush.getPREFIX( ) + "Die §eRunde §7startet in §b§l" + count
                            + " §7Sekunde" + ( count == 1 ? "§8." : "n§8." ) );
                    allPlayer.playSound( allPlayer.getLocation( ), Sound.NOTE_PLING, 10, 3 );
                } );
            }
        }, 60, 45, 30, 15, 10, 5, 4, 3, 2, 1 );

        addCountdownHook( count -> {
            if ( !MLGRush.getInstance( ).getSession( ).isMapForced( ) ) {
                MLGRush.getInstance( ).getSession( ).forceMap( MLGRush.getInstance( ).getSession( )
                        .getConfiguration( ).getMaps( ).
                                get( MLGRush.getInstance( ).getSession( ).getVoting( ).getWinnerMap( ) ) );
            }
            for ( final Player eachPlayer : Bukkit.getOnlinePlayers( ) ) {
                if ( !MLGRush.getInstance( ).getSession( ).isMapForced( ) ) {
                    MLGRush.getInstance( ).getSession( ).
                            forceMap( MLGRush.getInstance( ).getSession( ).getConfiguration( ).getMaps( ).
                                    get( MLGRush.getInstance( ).getSession( ).getVoting( ).getWinnerMap( ) ) );
                }

                lobbyScoreboard.updateMap( eachPlayer, eachPlayer.getScoreboard( ), MLGRush.getInstance( ).getSession( )
                        .getCurrentMap( ) );
                CloudServer.getInstance( ).setMotdAndUpdate( MLGRush.getInstance( ).getSession( ).getCurrentMap( )
                        .getName( ) + " 2x1" );

                eachPlayer.getInventory( ).remove( Material.PAPER );
                eachPlayer.closeInventory( );
                eachPlayer.playSound( eachPlayer.getLocation( ), Sound.ANVIL_LAND, 3, 1 );
                eachPlayer.sendMessage( " " );
                eachPlayer.sendMessage( MLGRush.getPREFIX( ) + "§7Abstimmung beendet §8» §e" +
                        MLGRush.getInstance( ).getSession( ).getCurrentMap( ).getName( ) );
                eachPlayer.sendMessage( " " );
            }
        }, 5 );

        addCountdownHook( count -> {


            Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {

                all.getInventory( ).clear( );
                all.sendMessage( MLGRush.getPREFIX( ) + "§e§lAlle Spieler werden teleportiert§8." );
                all.setGameMode( GameMode.SURVIVAL );
                all.playSound( all.getLocation( ), Sound.ENDERMAN_TELEPORT, 3f, 3f );

                Bukkit.getWorlds( ).forEach( ( world -> {
                    world.setTime( 6000 );
                    world.setStorm( false );
                    world.setThundering( false );
                    world.setGameRuleValue( "doMobSpawning", "false" );
                    world.setGameRuleValue( "doDaylightCycle", "false" );
                } ) );

                MLGRush.getInstance( ).getTeam( ).setRandomTeam( all );
                if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( all.getUniqueId( ) )
                        .equalsIgnoreCase( "Rot" ) ) {
                    MLGRush.getInstance( ).getSession( ).getPlayerMap( ).put( "Rot",
                            new MLGRushPlayer( all.getUniqueId( ), all.getName( ) ) );
                } else if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( all.getUniqueId( ) )
                        .equalsIgnoreCase( "Blau" ) ) {
                    MLGRush.getInstance( ).getSession( ).getPlayerMap( ).put( "Blau",
                            new MLGRushPlayer( all.getUniqueId( ), all.getName( ) ) );
                }
                all.teleport( MLGRush.getInstance( ).getSession( ).getCurrentMap( ).getTeamSpawnsLocations( ).get(
                        MLGRush.getInstance( ).getSession( ).getTeam( ).get( all.getUniqueId( ) )
                ).toLocation( ) );
                MLGRush.getInstance( ).getSession( ).getPlayers( ).add( all.getUniqueId( ) );
                all.setMaxHealth( 20 );
                all.setHealth( 20 );
            } );

            Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {
                ingameScoreboard.sendSidebar( all, Bukkit.getOnlinePlayers( ).size( ) );
                ingameScoreboard.sendTablist( all );
                MLGRush.getInstance( ).getSession( ).getPlayerGameInventory( ).load( all, Executors.newCachedThreadPool( ) );
            } );

            CloudServer.getInstance( ).setServerState( ServerState.INGAME );


        }, 0 );

    }

    @Override
    public void onTick( int time ) {

        if ( time == 1 ) {
            sendAllActionBar( MLGRush.getPREFIX( ) + "Das Spiel startet in §e§l" + time + " §7Sekunde§8." );
        } else {
            sendAllActionBar( MLGRush.getPREFIX( ) + "Das Spiel startet in §e§l" + time + " §7Sekunden§8." );
        }

        if ( Bukkit.getOnlinePlayers( ).size( ) >= MLGRush.getInstance( ).getSession( ).getMaxplayers( ) ) {
            for ( final Player eachPlayer : Bukkit.getOnlinePlayers( ) ) {
                if ( eachPlayer.getLocation( ).getY( ) <= 20 ) {
                    eachPlayer.teleport( MLGRush.getInstance( ).getSession( ).getConfiguration( ).getLobbyLocation( )
                            .toLocation( ) );
                }

                eachPlayer.setLevel( time );
                eachPlayer.setExp( 1f / GameState.LOBBY.getSeconds( ) * time );
            }
        } else {
            time = 60;
            for ( final Player eachPlayer : Bukkit.getOnlinePlayers( ) ) {
                if ( eachPlayer.getLocation( ).getY( ) <= 20 ) {
                    eachPlayer.teleport( MLGRush.getInstance( ).getSession( ).getConfiguration( ).getLobbyLocation( )
                            .toLocation( ) );
                }
                eachPlayer.setLevel( 60 );
                eachPlayer.setExp( 1f / GameState.LOBBY.getSeconds( ) * time );
            }
        }
    }
}
