package de.daniel.mlgrush.inventories;

import de.daniel.mlgrush.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.concurrent.ExecutorService;

public class TeamInventory implements Inventory {

    private org.bukkit.inventory.Inventory inventory;

    public TeamInventory( ) {
        inventory = Bukkit.createInventory( null, 9, "§8» §eTeams" );

        inventory.setItem( 2, MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get( "Rot" ).getItem( ) );
        inventory.setItem( 6, MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get( "Blau" ).getItem( ) );

    }

    @Override
    public void load( Player player, final ExecutorService service ) {
        service.execute( ( ) -> {
            player.openInventory( inventory );
            player.playSound( player.getLocation( ), Sound.LEVEL_UP, 3f, 3f );
        } );
    }
}
