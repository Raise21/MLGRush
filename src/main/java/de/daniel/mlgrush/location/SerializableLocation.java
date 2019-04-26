package de.daniel.mlgrush.location;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@RequiredArgsConstructor
public class SerializableLocation {

    private final double x, y, z;
    private final float yaw, pitch;
    private final String world;

    public Location toLocation( ) {
        return new Location( Bukkit.getWorld( world ), x, y, z, yaw, pitch );
    }

    public static SerializableLocation toLocation( final Location location ) {
        return new SerializableLocation( location.getX( ), location.getY( ), location.getZ( ),
                location.getYaw( ), location.getPitch( ), location.getWorld( ).getName( ) );
    }
}
