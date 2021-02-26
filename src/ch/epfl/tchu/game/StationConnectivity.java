package ch.epfl.tchu.game;

/**
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 */
public interface StationConnectivity {

    /**
     *
     * @param s1 First station
     * @param s2 Second station
     * @return true if both stations are connected
     */
    public abstract boolean connected(Station s1, Station s2);
}
