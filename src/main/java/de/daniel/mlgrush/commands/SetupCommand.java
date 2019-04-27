package de.daniel.mlgrush.commands;

import de.daniel.mlgrush.configuration.MLGRushConfiguration;
import de.daniel.mlgrush.configuration.MLGRushBed;
import de.daniel.mlgrush.configuration.MLGRushMap;
import de.daniel.mlgrush.location.SerializableLocation;
import de.daniel.mlgrush.MLGRush;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCommand implements CommandExecutor {

    private final MLGRushConfiguration configuration = MLGRush.getInstance( ).getSession( ).getConfiguration( );

    @Override
    public boolean onCommand( final CommandSender sender, final Command command, final String label, final String[] args ) {

        final Player player = ( Player ) sender;

        if ( !player.hasPermission( "mlgrush.command.setup" ) ) {
            player.sendMessage( MLGRush.getNO_PERMISSION( ) );
            return true;
        }

        if ( args.length == 1 ) {
            if ( args[ 0 ].equalsIgnoreCase( "setLobby" ) ) {
                MLGRush.getInstance( ).getSession( ).getConfiguration( ).setLobbyLocation( SerializableLocation.toLocation( player.getLocation( ) ) );
                player.sendMessage( MLGRush.getPREFIX( ) + "Du hast die §aLobby §7gesetzt§8." );
            } else if ( args[ 0 ].equalsIgnoreCase( "finish" ) ) {
                MLGRush.getInstance( ).getSession( ).getConfiguration( ).save( MLGRush.getInstance( ).getSession( ).getFile( ) );
                player.sendMessage( MLGRush.getPREFIX( ) + "§cDu hast alles gespeichert§8." );
            }
        } else if ( args.length == 2 ) {
            String map;
            if ( args[ 0 ].equalsIgnoreCase( "createMap" ) ) {
                map = args[ 1 ];
                if ( configuration.getMaps( ).containsKey( map ) ) {
                    player.sendMessage( MLGRush.getPREFIX( ) + "Die Map existiert §cbereits§8." );
                    return true;
                }
                final MLGRushMap MLGRushMap = new MLGRushMap( );
                MLGRushMap.setName( map );
                MLGRushMap.setType( "2x1" );
                MLGRushMap.setDeath( ( int ) player.getLocation( ).getY( ) );
                MLGRushMap.setSpectatorLocation( SerializableLocation.toLocation( player.getLocation( ) ) );
                configuration.getMaps( ).put( map, MLGRushMap );
                player.sendMessage( MLGRush.getPREFIX( ) + "Du hast eine neue §aKarte §7hinzugefügt§8." );

            } else if ( args[ 0 ].equalsIgnoreCase( "deleteMap" ) ) {
                map = args[ 1 ];
                if ( !this.configuration.getMaps( ).containsKey( map ) ) {
                    player.sendMessage( MLGRush.getPREFIX( ) + "Die Map existiert §cnicht§8." );
                    return true;
                }

                this.configuration.getMaps( ).remove( map );
                player.sendMessage( MLGRush.getPREFIX( ) + "Du hast die §aKarte §7erfolgreich gelöscht§8." );

            } else if ( args[ 0 ].equalsIgnoreCase( "setSpectator" ) ) {
                map = args[ 1 ];
                if ( !this.configuration.getMaps( ).containsKey( map ) ) {
                    player.sendMessage( MLGRush.getPREFIX( ) + "Die Map existiert §cnicht§8." );
                    return true;
                }

                this.configuration.getMaps( ).get( map ).setSpectatorLocation( SerializableLocation.toLocation( player.getLocation( ) ) );
                player.sendMessage( MLGRush.getPREFIX( ) + "Du hast den §aSpectatorSpawn §7erfolgreich gesetzt§8." );
            } else if ( args[ 0 ].equalsIgnoreCase( "setDeath" ) ) {
                map = args[ 1 ];
                if ( !this.configuration.getMaps( ).containsKey( map ) ) {
                    player.sendMessage( MLGRush.getPREFIX( ) + "Die Map existiert §cnicht§8." );
                    return true;
                }
                configuration.getMaps( ).get( map ).setDeath( ( int ) player.getLocation( ).getY( ) );
                player.sendMessage( MLGRush.getPREFIX( ) + "Du hast die Todeshöhe auf §e" + player.getLocation( ).getY( ) + " §7gesetzt§8." );
            } else if ( args[ 0 ].equalsIgnoreCase( "setLimit" ) ) {
                map = args[ 1 ];
                if ( !this.configuration.getMaps( ).containsKey( map ) ) {
                    player.sendMessage( MLGRush.getPREFIX( ) + "Die Map existiert §cnicht§8." );
                    return true;
                }
                configuration.getMaps( ).get( map ).setBlocklimit( ( int ) player.getLocation( ).getY( ) );
                player.sendMessage( MLGRush.getPREFIX( ) + "Du hast die Max Blockhöhe auf §e" + player.getLocation( ).getY( ) + " §7gesetzt§8." );
            }

        } else if ( args.length == 3 ) {
            String map;
            if ( args[ 0 ].equalsIgnoreCase( "setTeamSpawn" ) ) {
                map = args[ 1 ];
                if ( !this.configuration.getMaps( ).containsKey( map ) ) {
                    player.sendMessage( MLGRush.getPREFIX( ) + "Die Map existiert §cnicht§8." );
                    return true;
                }
                configuration.getMaps( ).get( map ).getTeamSpawnsLocations( ).put( args[ 2 ], SerializableLocation.toLocation( player.getLocation( ) ) );
                player.sendMessage( MLGRush.getPREFIX( ) + "Du hast den §aTeamspawn für §e" + args[ 2 ] + " §7erfolgreich gesetzt§8." );
            } else if ( args[ 0 ].equalsIgnoreCase( "setFirstBed" ) ) {
                map = args[ 1 ];
                if ( !this.configuration.getMaps( ).containsKey( map ) ) {
                    player.sendMessage( MLGRush.getPREFIX( ) + "Die Map existiert §cnicht§8." );
                    return true;
                }
                if ( !configuration.getMaps( ).get( map ).getBedLocations( ).containsKey( args[ 2 ] ) ) {
                    configuration.getMaps( ).get( map ).getBedLocations( ).put( args[ 2 ], new MLGRushBed( ) );
                }
                configuration.getMaps( ).get( map ).getBedLocations( ).get( args[ 2 ] ).setFirstLocation( SerializableLocation.toLocation( player
                        .getLocation( ).getBlock( ).getLocation( ) ) );

                player.sendMessage( MLGRush.getPREFIX( ) + "Du hast eine §aBedhälfte §7erfolgreich gesetzt§8." );
            } else if ( args[ 0 ].equalsIgnoreCase( "setSecondBed" ) ) {
                map = args[ 1 ];
                if ( !this.configuration.getMaps( ).containsKey( map ) ) {
                    player.sendMessage( MLGRush.getPREFIX( ) + "Die Map existiert §cnicht§8." );
                    return true;
                }
                if ( !configuration.getMaps( ).get( map ).getBedLocations( ).containsKey( args[ 2 ] ) ) {
                    configuration.getMaps( ).get( map ).getBedLocations( ).put( args[ 2 ], new MLGRushBed( ) );
                }

                configuration.getMaps( ).get( map ).getBedLocations( ).get( args[ 2 ] ).setSecondLocation( SerializableLocation.toLocation( player
                        .getLocation( ).getBlock( ).getLocation( ) ) );
                player.sendMessage( MLGRush.getPREFIX( ) + "Du hast eine §aBedhälfte §7erfolgreich gesetzt§8." );
            }
        }


        return true;
    }
}
