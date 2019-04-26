package de.daniel.mlgrush.commands;

import de.daniel.mlgrush.session.Session;
import de.daniel.mlgrush.state.GameState;
import de.daniel.mlgrush.MLGRush;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private final Session session = MLGRush.getInstance( ).getSession( );

    @Override
    public boolean onCommand( final CommandSender sender, final Command command, final String label, final String[] args ) {
        if ( !( sender instanceof Player ) ) {
            sender.sendMessage( "You have to be a player." );
            return true;
        }
        final Player player = ( Player ) sender;

        if ( !player.hasPermission( "mlgrush.command.start" ) ) {
            player.sendMessage( MLGRush.getNO_PERMISSION( ) );
            return true;
        }

        if ( session.getCurrentState( ) == GameState.WAITING ) {
            player.sendMessage( MLGRush.getPREFIX( ) + "§7Das §bSpiel §7benötigt mindestens §e§2 §7Spieler." );
            return true;
        } else if ( session.getCurrentState( ) == GameState.LOBBY ) {
            if ( GameState.CURRENT_COUNTDOWN.getCacheTime( ) <= 6 ) {
                player.sendMessage( MLGRush.getPREFIX( ) + "§cDas Spiel startet bereits." );
            } else {
                GameState.CURRENT_COUNTDOWN.forceTime( 6 );
                player.sendMessage( MLGRush.getPREFIX( ) + "§a§lDu hast das Spiel erfolgreich gestartet§8." );
            }
        } else {
            player.sendMessage( MLGRush.getPREFIX( ) + "§cDas Spiel hat bereits gestartet." );
        }

        return true;
    }
}
