package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;

/**
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 * Public, final, immutable class
 */
public final class StationPartition implements StationConnectivity{

    private int[] stationRepresentant;

    private StationPartition(int[] stationRepresentant){

        this.stationRepresentant = stationRepresentant.clone();

    }


    @Override
    public boolean connected(Station s1, Station s2) {

       if(s1.id() > stationRepresentant.length || s2.id() > stationRepresentant.length){
           if(s1.id() == s2.id()){
               return true;
           }
           else return false;
       }

       else {

           if (stationRepresentant[s1.id()] == stationRepresentant[s2.id()]) {
               return true;
           }
           else return false;
       }


    }

    /**
     * Nested Class Builder
     * public, static, final class
     */
   public static final class Builder{

       private int[] stationRepresentant;
       private int stationCount;

        public Builder(int stationCount){

            Preconditions.checkArgument(stationCount >= 0);

            this.stationCount = stationCount;

            stationRepresentant = new int[stationCount];

            for(int i = 0; i < stationCount; ++i){
                stationRepresentant[i] = i;
            }

        }

        public Builder connect(Station s1, Station s2){

           stationRepresentant[s2.id()] = representative(s1.id());

           return




        }

        public StationPartition build(){

            for(int i = 0; i < stationRepresentant.length; ++i){

                stationRepresentant[i] = representative(stationRepresentant[i]);
            }

            return new StationPartition(stationRepresentant);



        }

        private int representative(int id){

            int a = id;

            while(stationRepresentant[a] != stationRepresentant[stationRepresentant[a]]){
                a = stationRepresentant[a];
            }

            return stationRepresentant[a];
        }

   }
}
