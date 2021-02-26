package ch.epfl.tchu.game;

import java.util.List;

/**
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 * Description of a card
 */
public enum Card {

    BLACK(Color.BLACK),
    VIOLET(Color.VIOLET),
    BLUE(Color.BLUE),
    GREEN(Color.GREEN),
    YELLOW(Color.YELLOW),
    ORANGE(Color.ORANGE),
    RED(Color.RED),
    WHITE(Color.WHITE),
    LOCOMOTIVE(null);

    Card(Color color) {
        this.color = color;
    }

    private final Color color;

    private static final Card[] AllArray = Card.values();

    /**
     * List of all cards
     */
    public static final List<Card> ALL = List.of(AllArray);

    /**
     * Size of ALL
     */
    public static final int COUNT = ALL.size();

    /**
     * List of all Cards except LOCOMOTIVE
     */
    public static final List<Card> CARS = List.of(BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE);

    /**
     *
     * @param color color of this
     * @return the Card corresponding the the color or LOCOMOTIVE if color is null
     */
    public static Card of(Color color) {

        for (int i = 0; i < COUNT; ++i) {
            if (color == CARS.get(i).color)
                return CARS.get(i);
        }
        return null;
    }

    /**
     *
     * @return the color of this
     */
    public Color color() {
        return this.color;
    }

}


