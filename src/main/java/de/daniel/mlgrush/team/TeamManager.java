package de.daniel.mlgrush.team;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public final class TeamManager {

    private final List< UUID > players;

    private final String prefix;

    private final ItemStack itemStack;

    private final Color color;

    private final String teamname;

    private final String name;

    public TeamManager( final String prefix, final ItemStack itemStack, final Color color, final String teamname, final String name ) {
        this.players = Lists.newArrayList( );
        this.prefix = prefix;
        this.itemStack = itemStack;
        this.color = color;
        this.teamname = teamname;
        this.name = name;
    }

    public void addPlayer( final Player player ) {
        players.add( player.getUniqueId( ) );
    }

    public void removePlayer( final Player player ) {
        players.remove( player.getUniqueId( ) );
    }

    public void resetPlayers( ) {
        players.clear( );
    }

    public ItemStack getItem( ) {
        final ItemStack item = itemStack;
        final ItemMeta meta = item.getItemMeta( );
        final List< String > lore = Lists.newArrayList( );
        if ( players.size( ) != 0 ) {
            lore.add( "§8§m─────────" );
            for ( final UUID string : players ) {
                lore.add( "§8• " + prefix + Bukkit.getPlayer( string ).getName( ) );
            }
            meta.setLore( lore );
        } else {
            meta.setLore( null );
        }
        item.setItemMeta( meta );
        return itemStack;
    }
}
