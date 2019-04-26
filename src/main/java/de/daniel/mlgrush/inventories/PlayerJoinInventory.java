package de.daniel.mlgrush.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.concurrent.ExecutorService;

public class PlayerJoinInventory implements Inventory {


    private static ItemStack armorstand = new ItemStack( Material.ARMOR_STAND );
    private static ItemMeta armorstandItemMeta = armorstand.getItemMeta( );

    private static ItemStack paper = new ItemStack( Material.PAPER );
    private static ItemMeta paperItemMeta = paper.getItemMeta( );

    private static ItemStack inventory = new ItemStack( Material.REDSTONE_COMPARATOR );
    private static ItemMeta inventoryItemMeta = inventory.getItemMeta( );

    private static ItemStack slimeball = new ItemStack( Material.SLIME_BALL );
    private static ItemMeta slimeballItemMeta = slimeball.getItemMeta( );

    public PlayerJoinInventory( ) {
        armorstandItemMeta.setDisplayName( "§8» §eTeams" );
        armorstand.setItemMeta( armorstandItemMeta );

        paperItemMeta.setDisplayName( "§8» §eMapVoting" );
        paper.setItemMeta( paperItemMeta );

        inventoryItemMeta.setDisplayName( "§8» §eInventar" );
        inventory.setItemMeta( inventoryItemMeta );

        slimeballItemMeta.setDisplayName( "§8» §eVerlassen" );
        slimeball.setItemMeta( slimeballItemMeta );
    }


    @Override
    public void load( final Player player, final ExecutorService service ) {
        service.execute( ( ) -> {
            player.getInventory( ).clear( );
            player.getInventory( ).setItem( 2, armorstand );
            player.getInventory( ).setItem( 3, paper );
            player.getInventory( ).setItem( 5, inventory );
            player.getInventory( ).setItem( 6, slimeball );
        } );
    }
}
