package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public, enum class represents the different types of game cards: the eight wagon cards (one per color) and the locomotive card
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

    private final Color color;

    /**
     * Constructor class for Card, to set the card colour
     * @param color the color that the rail card should be initialized with
     */
    Card(Color color) {
        this.color = color;
    }


    private static final Card[] AllArray = Card.values();

    /**
     * An immutable list containing all the values of this enum type, in their order of definition
     */
    public static final List<Card> ALL = List.of(AllArray);

    /**
     * Int which contains the number of Card enum type values stored in ALL
     */
    public static final int COUNT = ALL.size();

    /**
     * An immutable list of all Cards except LOCOMOTIVE
     */
    public static final List<Card> CARS = List.of(BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE);

    /**
     * Returns the type of wagon card corresponding to the given color
     * @param color color of the requested wagon card
     * @return the wagon card corresponding to the color, or LOCOMOTIVE if color is null
     */
    public static Card of(Color color) {
        if (color == null){
            return LOCOMOTIVE;
        } else{
            switch (color) {
                case BLACK: return BLACK;
                case VIOLET: return VIOLET;
                case BLUE: return BLUE;
                case GREEN: return GREEN;
                case YELLOW: return YELLOW;
                case ORANGE: return ORANGE;
                case RED: return RED;
                case WHITE: return WHITE;
                default: throw new Error();
            }
        }
    }

    /**
     * Returns the color of the type of the card to which it is applied if it is a wagon type, or null if it's a locomotive type
     * @return the color of the type of the card to which it is applied if it is a wagon type, or null if it's a locomotive type
     */
    public Color color() {
        return color;
    }

}


