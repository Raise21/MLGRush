package de.daniel.mlgrush.session;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import de.daniel.mlgrush.MLGRush;
import de.daniel.mlgrush.builder.ItemBuilder;
import de.daniel.mlgrush.configuration.MLGRushConfiguration;
import de.daniel.mlgrush.configuration.MLGRushMap;
import de.daniel.mlgrush.configuration.player.MLGRushPlayer;
import de.daniel.mlgrush.inventories.PlayerGameInventory;
import de.daniel.mlgrush.inventories.PlayerJoinInventory;
import de.daniel.mlgrush.inventories.VotingInventory;
import de.daniel.mlgrush.session.voting.MapVoting;
import de.daniel.mlgrush.state.GameState;
import de.daniel.mlgrush.team.TeamManager;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Data
public class Session implements ISession {

    private File file;

    private MLGRushConfiguration configuration;

    private Random random = new Random( );

    private MLGRushMap currentMap;

    private GameState currentState;

    private int maxplayers = 2;

    private boolean MapForced;

    private MapVoting voting;

    private final Map< String, TeamManager > teamManagerMap = new HashMap<>( );

    private final Cache< Player, Player > cache = CacheBuilder.newBuilder( ).expireAfterAccess( 4, TimeUnit.SECONDS ).build( );

    private final Map< UUID, String > team = new HashMap<>( );

    private final List< String > teams = new ArrayList<>( );

    private final Map< String, MLGRushPlayer > playerMap = new HashMap<>( );

    private final List< UUID > players = new ArrayList<>( );

    private final List< UUID > spectators = new ArrayList<>( );

    private final List< Location > blocks = new ArrayList<>( );

    private String cachedWinner;

    private VotingInventory votingInventory;

    private PlayerJoinInventory playerJoinInventory;

    private PlayerGameInventory playerGameInventory;

    @Override
    public void load( ) {
        if ( !MLGRush.getInstance( ).getDataFolder( ).exists( ) ) {
            MLGRush.getInstance( ).getDataFolder( ).mkdir( );
        }
        file = new File( MLGRush.getInstance( ).getDataFolder( ), "config.json" );

        try {
            if ( file.exists( ) ) {
                configuration = new Gson( ).fromJson( new FileReader( file ), MLGRushConfiguration.class );
            } else {
                file.createNewFile( );
                configuration = new MLGRushConfiguration( );
                configuration.save( file );
            }
        } catch ( IOException exception ) {
            exception.printStackTrace( );
        }

    }

    @Override
    public MLGRushMap getRandomMap( ) {
        final List< MLGRushMap > list = Lists.newArrayList( MLGRush.getInstance( ).getSession( ).getConfiguration( )
                .getMaps( ).values( ) );
        if ( list.isEmpty( ) ) {
            return null;
        }
        return list.get( random.nextInt( list.size( ) ) );
    }

    @Override
    public void prepareSetup( ) {
        try {
            Bukkit.getWorlds( ).forEach( ( all ) -> {
                all.setTime( 1000 );
                all.setStorm( false );
                all.setGameRuleValue( "doMobSpawning", "false" );
                all.setGameRuleValue( "doDaylightCycle", "false" );
            } );
            this.forceMap( this.getRandomMap( ) );
            GameState.toGameState( GameState.WAITING );
            playerJoinInventory = new PlayerJoinInventory( );
            playerGameInventory = new PlayerGameInventory( );
        } catch ( final Exception ignored ) {
        }
    }

    @Override
    public void prepareMaps( ) {
        try {
            voting = new MapVoting( );
            voting.prepareVoting( );
            votingInventory = new VotingInventory( );
        } catch ( Exception ignored ) {
        }
    }

    @Override
    public void setUpTeams( ) {
        final int maxplayers = 1;

        this.teamManagerMap.put( "Rot", new TeamManager( "§c", new ItemBuilder( Material.STAINED_CLAY, 14,
                "§cTeam Rot", null, 1 ).build( ), Color.RED, "0001Rot", "Rot" ) );

        this.teamManagerMap.put( "Blau", new TeamManager( "§9", new ItemBuilder( Material.STAINED_CLAY, 11,
                "§9Team Blau", null, 1 ).build( ), Color.BLUE, "0002Blau", "Blau" ) );

        this.maxplayers = maxplayers;

        for ( String string : Arrays.asList( "Rot", "Blau" ) ) {
            teams.add( string );
        }
    }

    @Override
    public void forceMap( final MLGRushMap map ) {
        this.currentMap = map;
    }

    @Override
    public void loadEvents( ) {
        new Reflections( "de.daniel.mlgrush.listener" ).getSubTypesOf( Listener.class ).forEach( events -> {
            try {
                Bukkit.getPluginManager( ).registerEvents( events.newInstance( ), MLGRush.getInstance( ) );
            } catch ( final InstantiationException | IllegalAccessException event ) {
                event.printStackTrace( );
            }
        } );
    }

    public void showPlayers( final Player player ) {
        Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {
            all.showPlayer( player );
            player.showPlayer( all );
        } );
    }

    public void hidePlayers( final Player player ) {
        Bukkit.getOnlinePlayers( ).forEach( ( all ) -> {
            if ( !MLGRush.getInstance( ).getSession( ).getSpectators( ).contains( all.getUniqueId( ) ) ) {
                all.hidePlayer( player );
            }
        } );
    }

    public void setSpectator( final Player player ) {
        player.setAllowFlight( true );
        player.setHealth( 20 );
        player.setFoodLevel( 40 );
        player.setFlying( true );
        player.sendMessage( MLGRush.getPREFIX( ) + "§e§lDu bist nun Spectator§8." );
        player.getInventory( ).clear( );
        player.getInventory( ).setArmorContents( null );

        MLGRush.getSERVICE( ).execute( ( ) -> {
            Bukkit.getScheduler( ).runTask( MLGRush.getInstance( ), ( ) -> {
                player.teleport( MLGRush.getInstance( ).getSession( ).getCurrentMap( ).getSpectatorLocation( )
                        .toLocation( ) );
            } );
        } );
    }

}
