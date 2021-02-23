package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

public enum Color {
    BLACK,
    VIOLET,
    BLUE,
    GREEN,
    YELLOW,
    ORANGE,
    RED,
    WHITE;


public static final Color[] AllArray = Color.values();

public static final List<Color> ALL = List.of(AllArray);

public static final int COUNT = ALL.size();

}




