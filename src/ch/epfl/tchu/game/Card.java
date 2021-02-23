package ch.epfl.tchu.game;

import java.util.List;

public enum Card {

    BLACK,
    VIOLET,
    BLUE,
    GREEN,
    YELLOW,
    ORANGE,
    RED,
    WHITE,
    LOCOMOTIVE;

    public static final Card[] AllArray = Card.values();

    public static final List<Card> ALL = List.of(AllArray);

    public static final int COUNT = ALL.size();

    public static final List<Card> CARS = List.of(BLACK, VIOLET, BLUE,GREEN, YELLOW, ORANGE, RED, WHITE);

}
