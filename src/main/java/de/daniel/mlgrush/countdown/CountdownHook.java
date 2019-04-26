package de.daniel.mlgrush.countdown;

@FunctionalInterface
public interface CountdownHook {

    void run( int count );
}
