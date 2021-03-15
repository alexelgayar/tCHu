package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;

/**
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 * Public, final, immutable class
 */
public final class StationPartition implements StationConnectivity{

    private ArrayList<Integer> representant = new ArrayList<>();

    private StationPartition(ArrayList<Integer> representant){

        this.representant = representant;

    }


    @Override
    public boolean connected(Station s1, Station s2) {

        return false;
    }


   static class Builder{

        public Builder(int stationCount){

            Preconditions.checkArgument(stationCount >= 0);
        }

   }
}
