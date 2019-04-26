package de.daniel.mlgrush.state;

import de.daniel.mlgrush.countdown.ingame.IngameCountdown;
import de.daniel.mlgrush.countdown.lobby.LobbyCountdown;
import de.daniel.mlgrush.countdown.restart.RestartCountdown;
import de.daniel.mlgrush.countdown.waiting.WaitingCountdown;
import de.daniel.mlgrush.MLGRush;
import de.daniel.mlgrush.countdown.MLGRushCountdown;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GameState {

    LOBBY( 60, false, LobbyCountdown.class ),

    WAITING( 20, false, WaitingCountdown.class ),

    IN_GAME( MLGRush.getInstance( ).getSession( ).getConfiguration( ).getEndTime( ) * 60, true, IngameCountdown.class ),

    RESTART( 21, false, RestartCountdown.class );

    public static MLGRushCountdown CURRENT_COUNTDOWN;

    private final int seconds;

    private final boolean inGame;

    private final Class< ? extends MLGRushCountdown > clazz;

    public static void toGameState( final GameState state ) {
        MLGRush.getInstance( ).getSession( ).setCurrentState( state );
        if ( CURRENT_COUNTDOWN != null && CURRENT_COUNTDOWN.isStarted( ) ) {
            CURRENT_COUNTDOWN.stop( );
        }
        if ( state.getClazz( ) == null ) {
            return;
        }
        try {
            final MLGRushCountdown countdown = state.getClazz( ).newInstance( );

            countdown.start( );
            CURRENT_COUNTDOWN = countdown;
        } catch ( final InstantiationException | IllegalAccessException exception ) {
            exception.printStackTrace( );
        }
    }

}
