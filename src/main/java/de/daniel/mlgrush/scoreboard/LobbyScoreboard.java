package de.daniel.mlgrush.scoreboard;

import de.daniel.mlgrush.configuration.MLGRushMap;
import de.daniel.mlgrush.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class LobbyScoreboard {

    private void registerTeams( final Scoreboard scoreboard ) {
        scoreboard.registerNewTeam( "0001Rot" ).setPrefix( "§c" );
        scoreboard.registerNewTeam( "0002Blau" ).setPrefix( "§9" );
        scoreboard.registerNewTeam( "0003default" ).setPrefix( "§7" );
    }

    private void setupScoreboardTeams( final Scoreboard scoreboard, final Player player ) {
        final Team map = scoreboard.registerNewTeam( "map" );
        map.setPrefix( " §8• §eVoting..." );
        map.addEntry( ChatColor.LIGHT_PURPLE.toString( ) );

        final Team team = scoreboard.registerNewTeam( "team" );
        if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) ) != null ) {
            team.setPrefix( " §8• " + MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get(
                    MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) )
            ).getPrefix( ) + MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) ) );
        } else {
            team.setPrefix( " §8• §cKeins" );
        }
        team.addEntry( ChatColor.GREEN.toString( ) + ChatColor.YELLOW.toString( ) );
    }

    private void setupScoreboardObjective( final Objective objective ) {
        objective.getScore( " " ).setScore( 9 );
        objective.getScore( "§7Karte§8:" ).setScore( 8 );
        objective.getScore( ChatColor.LIGHT_PURPLE.toString( ) ).setScore( 7 );
        objective.getScore( "§f" ).setScore( 6 );
        objective.getScore( "§7Spieler§8:" ).setScore( 5 );
        objective.getScore( ChatColor.DARK_PURPLE.toString( ) ).setScore( 4 );
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

        final Team online_players = scoreboard.registerNewTeam( "onlineplayers" );
        online_players.setPrefix( " §8• §a" + i + "§8/§c2" );
        online_players.addEntry( ChatColor.DARK_PURPLE.toString( ) );

        setupScoreboardTeams( scoreboard, player );
        setupScoreboardObjective( objective );

        player.setScoreboard( scoreboard );
    }

    public void updatePlayers( final Player player, final int i, final Scoreboard scoreboard ) {
        scoreboard.getTeam( "onlineplayers" ).setPrefix( " §8• §a" + i + "§8/§c2" );
    }

    public void updateTeam( final Player player, final Scoreboard scoreboard ) {
        if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) ) != null ) {
            scoreboard.getTeam( "team" ).setPrefix( " §8• " + MLGRush.getInstance( ).getSession( )
                    .getTeamManagerMap( ).get(
                    MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) )
            ).getPrefix( ) + MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) ) );
        } else {
            scoreboard.getTeam( "team" ).setPrefix( " §8• §cKeins" );
        }
    }

    public void updateMap( final Player player, final Scoreboard scoreboard, final MLGRushMap map ) {
        scoreboard.getTeam( "map" ).setPrefix( " §8• §e" + map.getName( ) );
    }

    private void setupTablistTeams( final Scoreboard scoreboard, final Player player ) {
        if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) ) == null ) {
            if ( scoreboard.getTeam( "0003default" ) == null ) {
                registerTeams( scoreboard );
            }
            return;
        }
        if ( scoreboard.getTeam( "0001Rot" ) == null ) {
            registerTeams( scoreboard );
        }
    }

    private void setupTablist( final Scoreboard scoreboard, final Player player ) {
        if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) ) == null ) {
            scoreboard.getTeam( "0003default" ).addPlayer( player );

            Bukkit.getOnlinePlayers( ).forEach( ( all -> {
                final Scoreboard allScoreboard = all.getScoreboard( );

                if ( allScoreboard.getTeam( "0003default" ) == null ) {
                    registerTeams( allScoreboard );
                }

                scoreboard.getTeam( "0003default" ).addPlayer( all );
                allScoreboard.getTeam( "0003default" ).addPlayer( player );
                all.setScoreboard( allScoreboard );
            } ) );
            player.setScoreboard( scoreboard );
            player.setPlayerListName( "§7" + player.getName( ) );
            return;
        }

        scoreboard.getTeam( MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get(
                MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) )
        ).getTeamname( ) ).addPlayer( player );

        Bukkit.getOnlinePlayers( ).forEach( ( all -> {
            final Scoreboard allScoreboard = all.getScoreboard( );

            if ( allScoreboard.getTeam( "0001Rot" ) == null ) {
                registerTeams( allScoreboard );
            }
            if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( all.getUniqueId( ) ) != null ) {
                scoreboard.getTeam( MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get(
                        MLGRush.getInstance( ).getSession( ).getTeam( ).get( all.getUniqueId( ) )
                ).getTeamname( ) ).addPlayer( all );
            } else {
                allScoreboard.getTeam( "0003default" ).addPlayer( all );
            }
            if ( MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) ) != null ) {
                allScoreboard.getTeam( MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get(
                        MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) )
                ).getTeamname( ) ).addPlayer( player );
            } else {
                allScoreboard.getTeam( "0003default" ).addPlayer( player );
            }
            all.setScoreboard( allScoreboard );
        } ) );
        player.setScoreboard( scoreboard );
        player.setPlayerListName( MLGRush.getInstance( ).getSession( ).getTeamManagerMap( ).get(
                MLGRush.getInstance( ).getSession( ).getTeam( ).get( player.getUniqueId( ) )
        ).getPrefix( ) + player.getName( ) );

    }

    public void sendTablist( final Player player ) {
        final Scoreboard scoreboard = player.getScoreboard( );
        setupTablistTeams( scoreboard, player );
        setupTablist( scoreboard, player );
    }
    
}
