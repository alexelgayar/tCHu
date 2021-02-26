package ch.epfl.tchu.game;


import ch.epfl.tchu.Preconditions;

/**
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 */
public final class Station {

    private int id;
    private String name;

    /**
     *
     * @param id a unique integer to represent each station in the game
     * @param name the station's name
     */
   public Station(int id, String name) {
        this.id = id;
        this.name = name;
        Preconditions.checkArgument(id >= 0);
    }

    /**
     *
     * @return id of this
     */
    public int id() {
        return this.id;
    }

    /**
     *
     * @return name of this
     */
    public String name() {
        return this.name;
    }

    /**
     *
     * @return name of this
     */
    @Override
    public String toString() {
        return this.name;
    }
}
