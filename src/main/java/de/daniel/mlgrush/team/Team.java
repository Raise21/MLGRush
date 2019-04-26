package de.daniel.mlgrush.team;

import de.daniel.mlgrush.MLGRush;
import de.daniel.mlgrush.scoreboard.LobbyScoreboard;
import org.bukkit.entity.Player;

public class Team {

    private final LobbyScoreboard lobbyScoreboard = new LobbyScoreboard( );

    public void join( final Player player, final String name ) {
        String team = null;

        if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) ) != null ) {
            team = MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) );
        }

        if ( team != null ) {
            if ( team.equals( name ) ) {
                player.sendMessage( MLGRush.getPREFIX( ) + "Du bist §cbereits §7in diesem Team§8." );
                player.closeInventory( );
                return;
            }
        }

        if ( MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get( name ).getPlayers( ).size( )
                >= MLGRush.getInstance( ).getSession( ).getMaxplayers( ) ) {
            player.sendMessage( MLGRush.getPREFIX( ) + "§7Das Team §8(" + MLGRush.getInstance( )
                    .getSession( ).getTeamManagerMap( ).get( name ).getPrefix( ) + name + "§8) §7ist bereits §cvoll§8." );
            player.closeInventory( );
            return;
        }

        if ( team != null ) {
            MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get( team ).removePlayer( player );
        }

        MLGRush.getInstance( ).getSession( ).getTeam( ).put( player.getUniqueId( ), name );
        MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get( name ).addPlayer( player );
        player.sendMessage( MLGRush.getPREFIX( ) + "Du bist dem Team §8(" + MLGRush.getInstance( )
                .getSession( ).getTeamManagerMap( ).get( name ).getPrefix( ) + name + "§8) §7beigetreten§8!" );
        lobbyScoreboard.updateTeam( player, player.getScoreboard( ) );
        lobbyScoreboard.sendTablist( player );
        player.closeInventory( );

    }

    public void setRandomTeam( final Player player ) {

        if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) ) != null ) {
            return;
        }

        for ( final String teams : MLGRush.getInstance( ).getSession( ).getTeams( ) ) {
            if ( MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get( teams ).getPlayers( ).size( )
                    < MLGRush.getInstance( ).getSession( ).getMaxplayers( ) ) {
                MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get( teams ).addPlayer( player );
                MLGRush.getInstance( ).getSession( ).getTeam( ).put( player.getUniqueId( ), teams );
                player.sendMessage( MLGRush.getPREFIX( ) + "Du bist nun im Team §8(" + MLGRush.getInstance( )
                        .getSession( ).getTeamManagerMap( ).get( teams ).getPrefix( ) + teams + "§8)" );
                return;
            }
        }

    }

}
