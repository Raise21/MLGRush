package de.daniel.mlgrush.inventories;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.concurrent.ExecutorService;

public class PlayerGameInventory implements Inventory {

    private static ItemStack stick = new ItemStack( Material.STICK );
    private static ItemMeta stickItemMeta = stick.getItemMeta( );

    private static ItemStack pickaxe = new ItemStack( Material.STONE_PICKAXE );
    private static ItemMeta pickaxeItemMeta = pickaxe.getItemMeta( );

    private static ItemStack sandstone = new ItemStack( Material.SANDSTONE, 64 );
    private static ItemMeta sandstoneItemMeta = sandstone.getItemMeta( );

    public PlayerGameInventory( ) {
        stickItemMeta.setDisplayName( "§eStick" );
        stickItemMeta.addEnchant( Enchantment.KNOCKBACK, 1, true );

        for ( ItemFlag flag : ItemFlag.values( ) ) {
            stickItemMeta.removeItemFlags( flag );
        }
        stick.setItemMeta( stickItemMeta );

        pickaxeItemMeta.setDisplayName( "§eHolzspitzhacke" );
        pickaxeItemMeta.addEnchant( Enchantment.DIG_SPEED, 1, true );

        for ( ItemFlag flag : ItemFlag.values( ) ) {
            pickaxeItemMeta.removeItemFlags( flag );
        }
        pickaxe.setItemMeta( pickaxeItemMeta );

        sandstoneItemMeta.setDisplayName( "§eSandstein" );

        for ( ItemFlag flag : ItemFlag.values( ) ) {
            sandstoneItemMeta.removeItemFlags( flag );
        }
        sandstone.setItemMeta( sandstoneItemMeta );
    }


    @Override
    public void load( final Player player, final ExecutorService service ) {
        player.getInventory( ).clear( );
        player.getInventory( ).setItem( 0, stick );
        player.getInventory( ).setItem( 1, pickaxe );
        player.getInventory( ).setItem( 2, sandstone );
    }
}
