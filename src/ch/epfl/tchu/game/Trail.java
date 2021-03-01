package ch.epfl.tchu.game;

import java.util.List;

/**
 * Public, final and immutable class
 * Contains no constructor, but contains a static method to compute the longest trail of a given route network
 */
public final class Trail {

    static Trail longest(List<Route> routes){
        //Use method List.contains to check if object inputted is contained in the receptor (this)
        if (routes.contains(this)){

        }

        //Use method List.removeAll to remove all elements of collection that we pass in as argument => Allows us to calculate total routes still not used
    }

    /**
     *
     * @return returns the length of the path
     */
    public int length(){
        //TODO: Code this
        return 0;
    }

    /**
     *
     * @return Returns the first station of the path, else null (IFF) route has length 0
     */
    public Station station1(){
        //TODO: Code this
        return null;
    }

    /**
     *
     * @return Returns the last station of the path, else null (IFF) route has length 0
     */
    public Station station2(){
        //TODO: Code this
        return null;
    }

    /**
     *
     * @return Returns textual representation of the path, containing at least the name of first and last station (in order) as well as the length of the path in parenthesis
     */
    @Override
    public String toString() {
        //Use a stringbuilder representation here

        //Lucerne - Fribourg (13)

        //Recommended for debugging purposes
        //Lucerne - Berne - Neuchâtel - Soleure - Berne - Fribourg (13)
    }
}
