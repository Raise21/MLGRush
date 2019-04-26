package de.daniel.mlgrush.countdown.ingame;

import de.daniel.mlgrush.MLGRush;
import de.daniel.mlgrush.countdown.MLGRushCountdown;
import de.daniel.mlgrush.state.GameState;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class IngameCountdown extends MLGRushCountdown {

    private void sendAllActionBar( final String message ) {
        final String string = ChatColor.translateAlternateColorCodes( '&', message );
        final IChatBaseComponent chatBaseComponent = IChatBaseComponent.ChatSerializer.a( "{\"text\": \"" + string +
                "\"}" );
        final PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat( chatBaseComponent, ( byte ) 2 );

        Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {
            ( ( CraftPlayer ) all ).getHandle( ).playerConnection.sendPacket( packetPlayOutChat );
        } );
    }

    private String returnTime( Integer integer ) {
        int minutes = 0;

        while ( integer >= 60 ) {
            integer -= 60;
            minutes++;
        }

        if ( minutes == 0 && integer < 10 ) {
            return "00:0" + integer;
        } else if ( minutes == 0 && integer > 9 ) {
            return "00:" + integer;
        } else if ( minutes < 10 && integer < 10 ) {
            return "0" + minutes + ":0" + integer;
        } else if ( minutes < 10 && integer > 9 ) {
            return "0" + minutes + ":" + integer;
        } else if ( minutes > 9 && integer < 10 ) {
            return "" + minutes + ":0" + integer;
        } else if ( minutes > 9 && integer > 9 ) {
            return "" + minutes + ":" + integer;
        } else {
            return "00:00";
        }
    }

    public IngameCountdown( ) {
        super( GameState.IN_GAME, GameState.RESTART );

        addCountdownHook( count -> {
            if ( count == 600 ) {
                for ( final Player allPlayer : Bukkit.getOnlinePlayers( ) ) {
                    MLGRush.getSERVICE( ).execute( ( ) -> {
                        allPlayer.sendMessage( MLGRush.getPREFIX( ) + "Die §eRunde §7endet in §b§l10 §7Minuten§8." );
                        allPlayer.playSound( allPlayer.getLocation( ), Sound.NOTE_PLING, 10, 3 );
                    } );
                }
            } else if ( count == 180 ) {
                for ( final Player allPlayer : Bukkit.getOnlinePlayers( ) ) {
                    MLGRush.getSERVICE( ).execute( ( ) -> {
                        allPlayer.sendMessage( MLGRush.getPREFIX( ) + "Die §eRunde §7endet in §b§l3 §7Minuten§8." );
                        allPlayer.playSound( allPlayer.getLocation( ), Sound.NOTE_PLING, 10, 3 );
                    } );
                }
            } else if ( count == 120 ) {
                for ( final Player allPlayer : Bukkit.getOnlinePlayers( ) ) {
                    MLGRush.getSERVICE( ).execute( ( ) -> {
                        allPlayer.sendMessage( MLGRush.getPREFIX( ) + "Die §eRunde §7endet in §b§l2 §7Minuten§8." );
                        allPlayer.playSound( allPlayer.getLocation( ), Sound.NOTE_PLING, 10, 3 );
                    } );
                }
            } else {
                for ( final Player allPlayer : Bukkit.getOnlinePlayers( ) ) {
                    MLGRush.getSERVICE( ).execute( ( ) -> {
                        allPlayer.sendMessage( MLGRush.getPREFIX( ) + "Die §eRunde §7endet in §b§l" + count
                                + " §7Sekunde" + ( count == 1 ? "§8." : "n§8." ) );
                        allPlayer.playSound( allPlayer.getLocation( ), Sound.NOTE_PLING, 10, 3 );
                    } );
                }
            }
        }, 600, 180, 120, 60, 30, 15, 10, 5, 4, 3, 2, 1 );

    }

    @Override
    public void onTick( int time ) {
        Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {
            sendAllActionBar( MLGRush.getPREFIX( ) + "Verbleibende Zeit§8: §e§l" +
                    returnTime( GameState.CURRENT_COUNTDOWN.getCacheTime( ) ) );
        } );
    }
}
