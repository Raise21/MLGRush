package de.daniel.mlgrush;

import de.daniel.mlgrush.commands.ForceMapCommand;
import de.daniel.mlgrush.commands.SetupCommand;
import de.daniel.mlgrush.commands.StartCommand;
import de.daniel.mlgrush.session.Session;
import de.daniel.mlgrush.team.Team;
import de.dytanic.cloudnet.bridge.CloudServer;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MLGRush extends JavaPlugin {

    @Getter
    private static MLGRush instance;

    @Getter
    private Session session;


    @Getter
    private static final String PREFIX = "§8» §b§lMLGRush §8● §7";

    @Getter
    private static final String NO_PERMISSION = PREFIX + "§7Dazu hast du keine §cBerechtigung.";

    @Getter
    private static final ExecutorService SERVICE = Executors.newCachedThreadPool( );

    @Getter
    private Team team;

    @Override
    public void onLoad( ) {
        super.onLoad( );
        instance = this;
        session = new Session( );
        team = new Team( );
    }

    @Override
    public void onEnable( ) {
        session.load( );
        session.prepareSetup( );
        session.setUpTeams( );
        session.prepareMaps( );
        session.loadEvents( );

        getCommand( "forcemap" ).setExecutor( new ForceMapCommand( ) );
        getCommand( "setup" ).setExecutor( new SetupCommand( ) );
        getCommand( "start" ).setExecutor( new StartCommand( ) );

        CloudServer.getInstance( ).setMaxPlayers( 2 );
        CloudServer.getInstance( ).setMotdAndUpdate( "Voting " + session.getCurrentMap( ).getType( ) );
    }

    @Override
    public void onDisable( ) {
    }
}
