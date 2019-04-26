package de.daniel.mlgrush.scoreboard;

import de.daniel.mlgrush.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class RestartScoreboard {

    private void registerTeams( final Scoreboard scoreboard ) {
        scoreboard.registerNewTeam( "default" ).setPrefix( "§7" );
    }

    private void setupScoreboardTeams( final Scoreboard scoreboard ) {
        final Team map = scoreboard.registerNewTeam( "map" );
        map.setPrefix( " §8• §e" + MLGRush.getInstance( ).getSession( ).getCurrentMap( ).getName( ) );
        map.addEntry( ChatColor.LIGHT_PURPLE.toString( ) );

    }

    private void setupWinner( final Scoreboard scoreboard, final String name ) {
        final Team winner = scoreboard.registerNewTeam( "winner" );
        winner.setPrefix( " §8• §a" );
        winner.setSuffix( name );
        winner.addEntry( ChatColor.GREEN.toString( ) );
    }

    private void setupScoreboardObjective( final Objective objective ) {
        objective.getScore( " " ).setScore( 6 );
        objective.getScore( "§7Karte§8:" ).setScore( 5 );
        objective.getScore( ChatColor.LIGHT_PURPLE.toString( ) ).setScore( 4 );
        objective.getScore( "§f" ).setScore( 3 );
        objective.getScore( "§7Gewinner§8:" ).setScore( 2 );
        objective.getScore( ChatColor.GREEN.toString( ) ).setScore( 1 );
        objective.getScore( "§7" ).setScore( 0 );
    }

    public void sendSidebar( final Player player, final String name ) {

        final Scoreboard scoreboard = Bukkit.getScoreboardManager( ).getNewScoreboard( );
        final Objective objective = scoreboard.registerNewObjective( "aaa", "bbb" );
        objective.setDisplaySlot( DisplaySlot.SIDEBAR );
        objective.setDisplayName( "§aVerany §8┃ §eMLGRush" );

        setupScoreboardTeams( scoreboard );
        setupWinner( scoreboard, name );
        setupScoreboardObjective( objective );

        player.setScoreboard( scoreboard );
    }


    private void setupTablistTeams( final Scoreboard scoreboard, final Player player ) {
        if ( scoreboard.getTeam( "default" ) == null ) {
            registerTeams( scoreboard );
        }
    }

    private void setupTablist( final Scoreboard scoreboard, final Player player ) {
        scoreboard.getTeam( "default" ).addPlayer( player );

        Bukkit.getOnlinePlayers( ).forEach( ( all -> {
            final Scoreboard allScoreboard = all.getScoreboard( );

            if ( allScoreboard.getTeam( "default" ) == null ) {
                registerTeams( allScoreboard );
            }

            scoreboard.getTeam( "default" ).addPlayer( all );
            allScoreboard.getTeam( "default" ).addPlayer( player );
            all.setScoreboard( allScoreboard );
        } ) );
        player.setScoreboard( scoreboard );
        player.setPlayerListName( "§7" + player.getName( ) );
    }

    public void sendTablist( final Player player ) {
        final Scoreboard scoreboard = player.getScoreboard( );
        setupTablistTeams( scoreboard, player );
        setupTablist( scoreboard, player );
    }
}
