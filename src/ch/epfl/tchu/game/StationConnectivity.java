package ch.epfl.tchu.game;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public interface, represents the "connectivity" of a player's network
 * (i.e. whether two stations in the tCHu network are connected or not by the player's network in question
 */
public interface StationConnectivity {

    /**
     * Returns true if and only if the given stations are connected by the player's network
     * @param s1 First station
     * @param s2 Second station
     * @return true if both stations are connected
     */
    public abstract boolean connected(Station s1, Station s2); //TODO: public abstract is redundant, can we remove?
}
