package de.daniel.mlgrush.configuration;

import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import de.daniel.mlgrush.configuration.map.MLGRushMap;
import de.daniel.mlgrush.location.SerializableLocation;
import lombok.Data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@Data
public class MLGRushConfiguration {

    private Map< String, MLGRushMap > maps = Maps.newConcurrentMap( );

    private SerializableLocation lobbyLocation;

    private SerializableLocation holoLocation;

    private int endTime = 15;

    public void save( final File file ) {
        try {

            final FileWriter fileWriter = new FileWriter( file );
            fileWriter.write( new GsonBuilder( ).setPrettyPrinting( ).create( ).toJson( this ) );
            fileWriter.flush( );
            fileWriter.close( );

        } catch ( final IOException exception ) {
            exception.printStackTrace( );
        }
    }
}
