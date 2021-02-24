package ch.epfl.tchu.game;

import java.util.List;

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

    Card(Color color){
        this.color = color;
    }

    private final Color color;

    public static final Card[] AllArray = Card.values();

    public static final List<Card> ALL = List.of(AllArray);

    public static final int COUNT = ALL.size();

    public static final List<Card> CARS = List.of(BLACK, VIOLET, BLUE,GREEN, YELLOW, ORANGE, RED, WHITE);

    public static Card of(Color color){

        for(int i = 0; i < COUNT; ++i){
           if (color == CARS.get(i).color)
               return CARS.get(i);
        }
        return null;
    }

    public Color color(){
        return this.color;
    }

}
