package de.daniel.mlgrush.countdown;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.daniel.mlgrush.state.GameState;
import de.daniel.mlgrush.MLGRush;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

@RequiredArgsConstructor
public abstract class MLGRushCountdown implements Runnable {

    private final Multimap< Integer, CountdownHook > hooks = ArrayListMultimap.create( );

    @Getter
    private final GameState state;

    private final GameState next;

    @Getter
    private int cacheTime;

    private BukkitTask task;

    @Getter
    private boolean started = false;

    protected void addCountdownHook( final CountdownHook hook, final int... counts ) {
        for ( final int eachOne : counts ) {
            hooks.put( eachOne, hook );
        }
    }

    public void start( ) {
        started = true;
        cacheTime = state.getSeconds( );

        task = Bukkit.getScheduler( ).runTaskTimer( MLGRush.getInstance( ), this, 0, 20 );
    }

    public void stop( ) {
        started = false;
        cacheTime = state.getSeconds( );

        task.cancel( );
    }

    public void forceTime( final int time ) {
        this.cacheTime = time;
    }

    @Override
    public void run( ) {
        cacheTime--;

        if ( hooks.containsKey( cacheTime ) ) {
            hooks.get( cacheTime ).forEach( hook -> hook.run( cacheTime ) );
        }
        onTick( cacheTime );

        if ( cacheTime == 0 ) {
            stop( );

            if ( next != null ) {
                GameState.toGameState( next );
            }
        }
    }

    public void onTick( final int time ) {
    }
}
