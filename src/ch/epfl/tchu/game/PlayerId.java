package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 */
public enum PlayerId {

    PLAYER_1,
    PLAYER_2;


    private static final PlayerId[] AllArray = PlayerId.values();

    /**
     * List containing all players
     */
    public static final List<PlayerId> ALL = List.of(AllArray);

    /**
     * Size of ALL
     */
    public static final int COUNT = ALL.size();

    /**
     * Returns the identity of the other player
     *
     * @return the identity of the other player
     */
    public PlayerId next() {
        return (this == PLAYER_1) ? PLAYER_2 : PLAYER_1;
    }

}
