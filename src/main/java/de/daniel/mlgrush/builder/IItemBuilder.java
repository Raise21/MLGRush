package de.daniel.mlgrush.builder;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public interface IItemBuilder extends Builder<ItemStack> {


    IItemBuilder setType( Material material );


    IItemBuilder addEnchantment( Enchantment enchantment, int level );


    IItemBuilder setDisplayName( String displayName );


    String getDisplayName( );


    String[] getLore( );


    IItemBuilder setLore( String[] lore );


    IItemBuilder setAmount( int amount );


    IItemBuilder setDurability( short durability );

    IItemBuilder hideEnchantments( );
}
