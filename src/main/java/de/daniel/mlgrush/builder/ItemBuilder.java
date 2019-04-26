package de.daniel.mlgrush.builder;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ItemBuilder implements IItemBuilder {

    private Material material;

    private int amount = 1;

    private boolean hide = false;

    private String displayName = "";

    private String[] lore;

    private int durability = 0;


    private final Map< Enchantment, Integer > enchantmentMap = new HashMap<>( );

    public ItemBuilder( ) {

    }

    public ItemBuilder( final Material material ) {
        this.material = material;
    }


    public ItemBuilder( final Material material, final int subID ) {
        this( material );
        this.durability = subID;
    }

    public ItemBuilder( final Material material, final int subID, final int amount, final String displayName ) {
        this( material, subID );
        this.amount = amount;
        this.displayName = displayName;
    }

    public ItemBuilder( final Material material, final String displayName, final int amount ) {
        this( material );
        this.amount = amount;
        this.displayName = displayName;
    }

    public ItemBuilder( final Material material, final String displayName, final int amount, final Enchantment enchantment ) {
        this( material );
        this.displayName = displayName;
        this.amount = amount;
        enchantmentMap.put( enchantment, 1 );
    }

    public ItemBuilder( final Material material, final int subID, final String displayName, final String[] lore, final int amount ) {
        this( material, subID );
        this.displayName = displayName;
        this.lore = lore;
        this.amount = amount;
    }

    @Override
    public IItemBuilder setType( final Material material ) {
        this.material = material;
        return this;
    }

    @Override
    public IItemBuilder addEnchantment( final Enchantment enchantment, final int level ) {
        enchantmentMap.put( enchantment, level );
        return this;
    }

    @Override
    public IItemBuilder setDisplayName( String name ) {
        this.displayName = name;
        return this;
    }

    @Override
    public String getDisplayName( ) {
        return this.displayName;
    }

    @Override
    public String[] getLore( ) {
        return this.lore;
    }

    @Override
    public IItemBuilder setLore( final String[] lore ) {
        this.lore = lore;
        return this;
    }

    @Override
    public IItemBuilder setAmount( final int amount ) {
        this.amount = amount;
        return this;
    }

    @Override
    public IItemBuilder setDurability( short durability ) {
        this.durability = durability;
        return this;
    }


    @Override
    public IItemBuilder hideEnchantments( ) {
        this.hide = !this.hide;
        return this;
    }


    @Override
    public ItemStack build( ) {
        final ItemStack stack;

        stack = new ItemStack( material, amount, ( short ) durability );

        final ItemMeta meta = stack.getItemMeta( );

        meta.setDisplayName( displayName );

        if ( hide ) {
            meta.addItemFlags( ItemFlag.HIDE_ATTRIBUTES );
        }

        for ( final Enchantment enchantments : enchantmentMap.keySet( ) ) {
            meta.addEnchant( enchantments, enchantmentMap.get( enchantments ), true );
        }

        if ( lore != null ) {
            meta.setLore( Arrays.asList( lore ) );
        }


        stack.setItemMeta( meta );
        return stack;
    }
}
