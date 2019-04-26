package de.daniel.mlgrush.inventories;

import de.daniel.mlgrush.builder.ItemBuilder;
import de.daniel.mlgrush.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ExecutorService;

public final class VotingInventory implements de.daniel.mlgrush.inventories.Inventory {

    private final Inventory inventory = Bukkit.createInventory( null, 9, "§8» §eVoting" );

    public VotingInventory( ) {
        final ItemStack placeholder = new ItemBuilder( Material.STAINED_GLASS_PANE, " ", 1 ).build( );

        for ( int i = 0; i < inventory.getSize( ); i++ ) {
            this.inventory.setItem( i, placeholder );
        }
        int slot = 0;

        for ( final String eachMap : MLGRush.getInstance( ).getSession( ).getVoting( ).getVotes( ).keySet( ) ) {
            inventory.setItem( slot, new ItemBuilder( Material.EMPTY_MAP, 0, "§e"
                    + eachMap, new String[]{"§7Klicke zum voten"}, 1 ).build( ) );
            slot = slot + 4;
        }
    }

    @Override
    public void load( Player player, final ExecutorService service ) {
        service.execute( ( ) -> {
            player.openInventory( this.inventory );
            player.playSound( player.getLocation( ), Sound.CHEST_OPEN, 3, 1 );
        } );
    }
}
