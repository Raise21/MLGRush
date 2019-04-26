package de.daniel.mlgrush.session;

import de.daniel.mlgrush.configuration.map.MLGRushMap;

public interface ISession {

    void load( );

    MLGRushMap getRandomMap( );

    void prepareSetup( );

    void prepareMaps( );

    void setUpTeams();

    void forceMap( final MLGRushMap map );

    void loadEvents();
}
