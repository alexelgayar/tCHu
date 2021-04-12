package ch.epfl.tchu.game;


import ch.epfl.tchu.Preconditions;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public, final, immutable. Represents a station
 */
public final class Station {

    private final int id;
    private final String name;

    /**
     * Public Constructor for the Station class, creates a station from a name and unique id
     * @param id a unique integer to represent each station in the game, a number between 0 and 50 (inclusive)
     * @param name the station's name
     */
    public Station(int id, String name) {
        Preconditions.checkArgument(id >= 0);
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the unique id of the station
     * @return the unique id of the station
     */
    public int id() {
        return id;
    }

    /**
     * Returns the name of the station
     * @return the name of the station
     */
    public String name() {
        return name;
    }

    /**
     * Redefines the toString method to return the name of the Station
     * @return the name of the station
     */
    @Override
    public String toString() {
        return name;
    }
}
