package de.daniel.mlgrush.commands;


import de.daniel.mlgrush.configuration.map.MLGRushMap;
import de.daniel.mlgrush.session.Session;
import de.daniel.mlgrush.state.GameState;
import de.daniel.mlgrush.MLGRush;
import de.daniel.mlgrush.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ForceMapCommand implements CommandExecutor {

    private final Session session = MLGRush.getInstance( ).getSession( );

    @Override
    public boolean onCommand( final CommandSender sender, final Command command, final String label, final String[] args ) {
        if ( !( sender instanceof Player ) ) {
            sender.sendMessage( "you have to be a player." );
            return true;
        }
        final Player player = ( Player ) sender;

        if ( !player.hasPermission( "mlgrush.command.forcemap" ) ) {
            player.sendMessage( MLGRush.getNO_PERMISSION( ) );
            return true;
        }

        if ( this.session.getCurrentState( ) != GameState.LOBBY && this.session.getCurrentState( ) != GameState.WAITING ) {
            player.sendMessage( MLGRush.getPREFIX( ) + "§cDie Karte kann nicht mehr verändert werden§8." );
            return true;
        }

        if ( this.session.isMapForced( ) ) {
            player.sendMessage( MLGRush.getPREFIX( ) + "§cDie Karte wurde bereits verändert§8." );
            return true;
        }
        final Inventory inventory = Bukkit.createInventory( null, 27, "§8» §eMapauswahl" );

        final ItemStack placeholder = new ItemBuilder( Material.STAINED_GLASS_PANE, "", 1 ).build( );

        for ( final MLGRushMap maps : MLGRush.getInstance( ).getSession( ).getConfiguration( ).getMaps( ).values( ) ) {
            inventory.addItem( new ItemBuilder( Material.EMPTY_MAP, 0, "§e"
                    + maps.getName( ), new String[]{"§7Klicke zum auswählen"}, 1 ).build( ) );

        }

        for ( int i = 0; i < inventory.getSize( ); i++ ) {
            if ( inventory.getItem( i ) == null || inventory.getItem( i ).getType( ) == Material.AIR ) {
                inventory.setItem( i, placeholder );
            }
        }
        player.openInventory( inventory );


        return true;
    }
}
