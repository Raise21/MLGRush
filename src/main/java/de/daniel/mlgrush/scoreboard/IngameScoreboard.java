package de.daniel.mlgrush.scoreboard;

import de.daniel.mlgrush.configuration.map.MLGRushMap;
import de.daniel.mlgrush.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class IngameScoreboard {

    private void registerTeams( final Scoreboard scoreboard ) {
        scoreboard.registerNewTeam( "0001red" ).setPrefix( "§c" );
        scoreboard.registerNewTeam( "0002blue" ).setPrefix( "§9" );
        final Team spectator = scoreboard.registerNewTeam( "0003spectator" );
        spectator.setPrefix( "§7" );
        spectator.setNameTagVisibility( NameTagVisibility.NEVER );
    }

    private void setupScoreboardTeams( final Scoreboard scoreboard, final Player player ) {
        final Team map = scoreboard.registerNewTeam( "map" );
        map.setPrefix( " §8• §e" + MLGRush.getInstance( ).getSession( ).getCurrentMap( ).getName( ) );
        map.addEntry( ChatColor.LIGHT_PURPLE.toString( ) );

        final Team kills = scoreboard.registerNewTeam( "kills" );
        kills.setPrefix( " §8• §c" + MLGRush.getInstance( ).getSession( ).getPlayerMap( ).get( "Rot" ).getKills( ) +
                "§8/§9" + MLGRush.getInstance( ).getSession( ).getPlayerMap( ).get( "Blau" ).getKills( ) );
        kills.addEntry( ChatColor.GRAY.toString( ) + ChatColor.YELLOW.toString( ) );

        final Team team = scoreboard.registerNewTeam( "team" );
        if ( !MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) )
                .equalsIgnoreCase( "Spectator" ) ) {
            team.setPrefix( " §8• " + MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get(
                    MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) )
            ).getPrefix( ) + MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) ) );
        } else {
            team.setPrefix( " §8• " + "§cKeins" );
        }

        team.addEntry( ChatColor.GREEN.toString( ) + ChatColor.YELLOW.toString( ) );
    }

    private void setupScoreboardObjective( final Objective objective ) {
        objective.getScore( " " ).setScore( 9 );
        objective.getScore( "§7Karte§8:" ).setScore( 8 );
        objective.getScore( ChatColor.LIGHT_PURPLE.toString( ) ).setScore( 7 );
        objective.getScore( "§f" ).setScore( 6 );
        objective.getScore( "§7Betten§8:" ).setScore( 5 );
        objective.getScore( ChatColor.GRAY.toString( ) + ChatColor.YELLOW.toString( ) ).setScore( 4 );
        objective.getScore( "§9" ).setScore( 3 );
        objective.getScore( "§7Team§8:" ).setScore( 2 );
        objective.getScore( ChatColor.GREEN.toString( ) + ChatColor.YELLOW.toString( ) ).setScore( 1 );
        objective.getScore( "§7" ).setScore( 0 );
    }

    public void sendSidebar( final Player player, final int i ) {

        final Scoreboard scoreboard = Bukkit.getScoreboardManager( ).getNewScoreboard( );
        final Objective objective = scoreboard.registerNewObjective( "aaa", "bbb" );
        objective.setDisplaySlot( DisplaySlot.SIDEBAR );
        objective.setDisplayName( "§aVerany §8┃ §eMLGRush" );

        setupScoreboardTeams( scoreboard, player );
        setupScoreboardObjective( objective );

        player.setScoreboard( scoreboard );
    }


    public void updateKills( final Player player, final Scoreboard scoreboard ) {
        scoreboard.getTeam( "kills" ).setPrefix( " §8• §c" + MLGRush.getInstance( ).getSession( ).getPlayerMap( )
                .get( "Rot" ).getKills( ) +
                "§8/§9" + MLGRush.getInstance( ).getSession( ).getPlayerMap( ).get( "Blau" ).getKills( ) );
    }

    public void updateMap( final Player player, final Scoreboard scoreboard, final MLGRushMap map ) {
        scoreboard.getTeam( "map" ).setPrefix( " §8• §e" + map.getName( ) );
    }

    private void setupTablistTeams( final Scoreboard scoreboard, final Player player ) {
        if ( scoreboard.getTeam( "0001red" ) == null ) {
            registerTeams( scoreboard );
        }
    }

    private void setupTablist( final Scoreboard scoreboard, final Player player ) {

        if ( MLGRush.getInstance( ).

                getSession( ).

                getTeam( ).

                get( player.getUniqueId( ) ).

                equalsIgnoreCase( "Rot" ) ) {
            scoreboard.getTeam( "0001red" ).addPlayer( player );
        } else {
            scoreboard.getTeam( "0002blue" ).addPlayer( player );
        }

        Bukkit.getOnlinePlayers( ).

                forEach( ( all ->

                {
                    final Scoreboard allScoreboard = all.getScoreboard( );
                    if ( allScoreboard.getTeam( "0001red" ) == null ) {
                        registerTeams( allScoreboard );
                    }
                    if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( all.getUniqueId( ) )
                            .equalsIgnoreCase( "Rot" ) ) {
                        scoreboard.getTeam( "0001red" ).addPlayer( all );
                    } else {
                        scoreboard.getTeam( "0002blue" ).addPlayer( all );
                    }
                    if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) )
                            .equalsIgnoreCase( "Rot" ) ) {
                        scoreboard.getTeam( "0001red" ).addPlayer( player );
                    } else {
                        scoreboard.getTeam( "0002blue" ).addPlayer( player );
                    }
                    all.setScoreboard( allScoreboard );
                } ) );
        player.setScoreboard( scoreboard );

        if ( MLGRush.getInstance( ).

                getSession( ).

                getTeam( ).

                get( player.getUniqueId( ) ).

                equalsIgnoreCase( "Rot" ) ) {
            player.setPlayerListName( "§c" + player.getName( ) );
        } else {
            player.setPlayerListName( "§9" + player.getName( ) );
        }

        if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) )
                .equalsIgnoreCase( "Spectator" ) ) {
            if ( scoreboard.getTeam( "0003spectator" ) == null ) {
                registerTeams( scoreboard );
            }

            scoreboard.getTeam( "0003spectator" ).addPlayer( player );

            Bukkit.getOnlinePlayers( ).forEach( ( all -> {
                final Scoreboard allScoreboard = all.getScoreboard( );

                if ( allScoreboard.getTeam( "0003spectator" ) == null ) {
                    registerTeams( allScoreboard );
                }

                allScoreboard.getTeam( "0003spectator" ).addPlayer( player );
                all.setScoreboard( allScoreboard );
            } ) );

            player.setScoreboard( scoreboard );
            player.setPlayerListName( "§7" + player.getName( ) );
        }

    }

    public void sendTablist( final Player player ) {
        final Scoreboard scoreboard = player.getScoreboard( );
        setupTablistTeams( scoreboard, player );
        setupTablist( scoreboard, player );
    }


}
