package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Public, final and immutable class
 * Contains no constructor, but contains a static method to compute the longest trail of a given route network
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 */
public final class Trail {

    int length = 0;
    List<Route> routes;
    Station s1;
    Station s2;

    private Trail(List<Route> routes, Station s1, Station s2){
        this.routes = routes;
        this.s1 = s1;
        this.s2 = s2;


    }

    /**
     *
     * @param routes
     * @return
     */
//    static Trail longest(List<Route> routes){
//        //Use method List.contains to check if object inputted is contained in the receptor (this)
//
//        }
//
//        //Use method List.removeAll to remove all elements of collection that we pass in as argument => Allows us to calculate total routes still not used
//    }

    /**
     *
     * @return returns the length of the path
     */
    public int length(){

        for(Route w: routes){
            length += w.length();
        }

        return length;
    }

    /**
     *
     * @return Returns the first station of the path, else null (IFF) route has length 0
     */
    public Station station1(){

        if(length() > 0){
            return s1;
        }
        else{
            return null;
        }
    }

    /**
     *
     * @return Returns the last station of the path, else null (IFF) route has length 0
     */
    public Station station2(){

        if(length() > 0){
            return s2;
        }
        else{
            return null;
        }
    }

    /**
     *
     * @return Returns textual representation of the path, containing at least the name of first and last station (in order) as well as the length of the path in parenthesis
     */
    @Override
    public String toString() {

        String text = null;
        List<String> stations = new ArrayList<>();

        for(Route w: routes){
            stations.add(w.station2().toString());
        }

        text = routes.get(0).station1().toString() + " - " + String.join( " - " ,  stations) + " (" + length() + ")";

        return text;
    }

}
