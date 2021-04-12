package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public, enum, represents the 8 colors used in the game to color the wagon cards and routes.
 */
public enum Color {
    BLACK,
    VIOLET,
    BLUE,
    GREEN,
    YELLOW,
    ORANGE,
    RED,
    WHITE;


    private static final Color[] AllArray = Color.values();

    /**
     * An immutable list containing all the values of this enum type, in their order of definition
     */
    public static final List<Color> ALL = List.of(AllArray);

    /**
     * Int which contains the number of Color enum type values stored in ALL
     */
    public static final int COUNT = ALL.size();

}




