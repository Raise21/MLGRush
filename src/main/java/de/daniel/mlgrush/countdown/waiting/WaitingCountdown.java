package de.daniel.mlgrush.countdown.waiting;


import de.daniel.mlgrush.MLGRush;
import de.daniel.mlgrush.countdown.MLGRushCountdown;
import de.daniel.mlgrush.state.GameState;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

public class WaitingCountdown extends MLGRushCountdown {

    private void sendAllActionBar( final String message ) {
        final String string = ChatColor.translateAlternateColorCodes( '&', message );
        final IChatBaseComponent chatBaseComponent = IChatBaseComponent.ChatSerializer.a( "{\"text\": \"" + string +
                "\"}" );
        final PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat( chatBaseComponent, ( byte ) 2 );

        Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {
            ( ( CraftPlayer ) all ).getHandle( ).playerConnection.sendPacket( packetPlayOutChat );
        } );
    }


    public WaitingCountdown( ) {
        super( GameState.WAITING, GameState.LOBBY );

        addCountdownHook( count -> {
            super.forceTime( GameState.WAITING.getSeconds( ) );
        }, 1 );
    }

    @Override
    public void onTick( int time ) {
        Bukkit.getOnlinePlayers( ).forEach( ( all -> {
            if ( all.getLocation( ).getY( ) <= 20 ) {
                all.teleport( MLGRush.getInstance( ).getSession( ).getConfiguration( ).getLobbyLocation( )
                        .toLocation( ) );
            }
            all.setLevel( 60 );
            all.setExp( 1f / GameState.LOBBY.getSeconds( ) * 60 );

            int i = Bukkit.getOnlinePlayers( ).size( );
            if ( i == 1 ) {
                sendAllActionBar( MLGRush.getPREFIX( ) + "§7Es wird noch §e§l" + i + " §7Spieler benötigt§8." );
            } else {
                sendAllActionBar( MLGRush.getPREFIX( ) + "§7Es werden noch §e§l" + i + " §7Spieler benötigt§8." );
            }
        } ) );
    }
}