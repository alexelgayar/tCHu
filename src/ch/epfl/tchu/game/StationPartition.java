package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public, final, immutable class
 */
public final class StationPartition implements StationConnectivity {

    private final int[] stationRepresentant;

    private StationPartition(int[] stationRepresentant) {
        this.stationRepresentant = stationRepresentant.clone();
    }

    /**
     * Returns true if both stations are connected, and if at least one of the stations is outside the bounds of the array, returns true iff both stations have the same id.
     * @param s1 First station
     * @param s2 Second station
     * @return true if both stations are connected, and if at least one of the stations is outside the bounds of the array, returns true iff both stations have the same id
     */
    @Override
    public boolean connected(Station s1, Station s2) {
        return (s1.id() >= stationRepresentant.length || s2.id() >= stationRepresentant.length)
                ? s1.id() == s2.id()
                : stationRepresentant[s1.id()] == stationRepresentant[s2.id()];
    }

    /**
     * Nested Class Builder
     * public, static, final class
     */
    public static final class Builder {

        private int[] stationRepresentant; //TODO: Is this immutable?

        /**
         * Constructs a new builder and creates a list where every station is representative of itself
         * @param stationCount the number of stations contained in the list
         * @throws IllegalArgumentException if stationCount is strictly inferior than 0
         */
        public Builder(int stationCount) {
            Preconditions.checkArgument(stationCount >= 0);

            stationRepresentant = new int[stationCount];

            for (int i = 0; i < stationCount; ++i) {
                stationRepresentant[i] = i;
            }

        }

        /**
         * Connects 2 stations by assigning the first station's representative to the second
         * @param s1 First station
         * @param s2 Second Station
         * @return a state identical to the receptor but with both stations connected
         */
        public Builder connect(Station s1, Station s2) {

            stationRepresentant[representative(s2.id())] = stationRepresentant[representative(s1.id())];

            return this;
        }

        /**
         * Flattens the list then returns a stationPartition created with the new list
         * @return a StationPartition with a flattened list of representatives
         */
        public StationPartition build() {
            for (int i = 0; i < stationRepresentant.length; ++i) {
                stationRepresentant[i] = representative(stationRepresentant[i]);
            }
            return new StationPartition(stationRepresentant);
        }


        private int representative(int id) {
            int a = id;
            while (stationRepresentant[a] != stationRepresentant[stationRepresentant[a]]) {
                a = stationRepresentant[a];
            }
            return stationRepresentant[a];
        }

    }
}
