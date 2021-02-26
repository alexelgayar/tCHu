package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
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
     * List containing all colors
     */
    public static final List<Color> ALL = List.of(AllArray);

    /**
     * Size of ALL
     */
    public static final int COUNT = ALL.size();

}




