package de.daniel.mlgrush.session.voting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.daniel.mlgrush.configuration.map.MLGRushMap;
import de.daniel.mlgrush.MLGRush;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class MapVoting {

    private final Map< String, Integer > votes = Maps.newConcurrentMap( );

    private final Set< Player > players = Sets.newConcurrentHashSet( );

    private List< MLGRushMap > copy;

    public void prepareVoting( ) {
        this.copy = Lists.newArrayList( MLGRush.getInstance( ).getSession( ).getConfiguration( ).getMaps( ).values( ) );

        copy.remove( MLGRush.getInstance( ).getSession( ).getCurrentMap( ) );

        this.votes.put( MLGRush.getInstance( ).getSession( ).getCurrentMap( ).getName( ), 0 );

        Collections.shuffle( this.copy );

        this.votes.put( copy.get( 0 ).getName( ), 0 );

        this.votes.put( copy.get( 1 ).getName( ), 0 );
    }

    public void vote( final Player player, final String name ) {
        player.closeInventory( );

        if ( this.players.contains( player ) ) {
            player.sendMessage( MLGRush.getPREFIX( ) + "§cDu hast bereits abgestimmt§8." );
            return;
        }
        this.players.add( player );

        if ( this.votes.containsKey( name ) ) {
            this.votes.put( name, this.votes.get( name ) + 1 );
        }
        player.sendMessage( MLGRush.getPREFIX( ) + "§7Du hast für die Map §e" + name + " §7abgestimmt§8." );
        player.playSound( player.getLocation( ), Sound.GHAST_SCREAM, 3, 1 );
    }

    public String getWinnerMap( ) {
        String winnerMap = "";

        int i = 0;

        for ( final String eachMap : this.votes.keySet( ) ) {
            if ( i <= this.votes.get( eachMap ) ) {
                i = this.votes.get( eachMap );
                winnerMap = eachMap;
            }
        }
        return winnerMap;
    }

}
