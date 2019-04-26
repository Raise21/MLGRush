package de.daniel.mlgrush.listener.interact;

import de.daniel.mlgrush.MLGRush;
import de.daniel.mlgrush.inventories.TeamInventory;
import de.daniel.mlgrush.state.GameState;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.concurrent.Executors;

public class PlayerInteractListener implements Listener {


    @EventHandler
    public void onInteract( final PlayerInteractEvent event ) {
        if ( event.getItem( ) == null || !event.getItem( ).hasItemMeta( ) ||
                event.getItem( ).getItemMeta( ).getDisplayName( ) == null ) {
            return;
        }

        if ( MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.WAITING ||
                MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.LOBBY ) {
            if ( event.getItem( ).getItemMeta( ).getDisplayName( ).equalsIgnoreCase( "§8» §eTeams" ) ) {
                event.setCancelled( true );
                new TeamInventory( ).load( event.getPlayer( ), Executors.newCachedThreadPool( ) );
            } else if ( event.getItem( ).getItemMeta( ).getDisplayName( ).equalsIgnoreCase( "§8» §eVerlassen" ) ) {
                event.getPlayer( ).kickPlayer( MLGRush.getPREFIX( ) + "Du hast die §bRunde §7verlassen§8." );
            } else if ( event.getItem( ).getItemMeta( ).getDisplayName( ).equalsIgnoreCase( "§8» §eMapVoting" ) ) {
                MLGRush.getInstance( ).getSession( ).getVotingInventory( ).load( event.getPlayer( ), Executors.newCachedThreadPool( ) );
            }
        } else if ( MLGRush.getInstance( ).getSession( ).getCurrentState( ) == GameState.IN_GAME ) {
            if ( event.getAction( ) == Action.RIGHT_CLICK_BLOCK ) {
                if ( event.getClickedBlock( ).getType( ) == Material.BED ||
                        event.getClickedBlock( ).getType( ) == Material.BED_BLOCK ) {
                    event.setCancelled( true );
                    return;
                }
            }

            if ( event.getItem( ).getType( ) == Material.BED_BLOCK || event.getItem( ).getType( ) == Material.BED ||
                    event.getItem( ).getItemMeta( ).getDisplayName( )
                    .equalsIgnoreCase( "§eBlöcke" ) || event.getItem( ).getItemMeta( ).getDisplayName( )
                    .equalsIgnoreCase( "§eStick" ) || event.getItem( ).getItemMeta( ).getDisplayName( )
                    .equalsIgnoreCase( "§eHolzspitzhacke" ) ) {
                event.setCancelled( false );
            }
        }
    }

    @EventHandler
    public void onInteract( final PlayerInteractAtEntityEvent event ) {
        if ( event.getRightClicked( ) instanceof ArmorStand ) {
            event.setCancelled( true );
            if ( event.getRightClicked( ).getCustomName( ).startsWith( "§2§lMap" ) ) {
                MLGRush.getInstance( ).getSession( ).getVotingInventory( ).load( event.getPlayer( ),
                        Executors.newCachedThreadPool( ) );
            }
        }
    }
}
