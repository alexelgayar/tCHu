package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Public, final and immutable class
 * Contains no constructor, but contains a static method to compute the longest trail of a given route network
 *
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 */
public final class Trail {

    private int length = 0;
    private final List<Route> routes;
    private final Station s1;
    private final Station s2;

    public Trail(List<Route> routes, Station s1, Station s2) {
        this.routes = routes;
        this.s1 = s1;
        this.s2 = s2;
    }

    /**
     * Receives list of all player-owned routes, returns the longest route of the player
     *
     * @param routes All the routes that are owned by the player
     * @return Returns the longest path of the network made up of the given routes. If multiple paths of max length => Returned path is not specified
     */
    public static Trail longest(List<Route> routes) {
        //TODO: Create 2 paths for each route at the initialisation of the algorithm!!!
        /*
        - Create a modifiable copy of the list of all the usable routes
        - Remove all currently used routes, using removeAll
        - Run through the remaining routes to see if they can extend the route (provided they are owned)
         */

        //1. Create a local, modifiable copy of routes owned by the player
        //----List of trails constituted of single routes, as long as smallestTrails is not empty----
        List<List<Route>> cs = new ArrayList<>();

        //Here we create two paths for each route
        for (Route route : routes) {
            List<Route> routeContainer = new ArrayList<>();
            //Route in forward direction
            routeContainer.add(route);
            cs.add(routeContainer);
            routeContainer.remove(0);
            //Route in backward direction
            Route backRoute = new Route(route.id(), route.station2(), route.station1(), route.length(), route.level(), route.color());
            routeContainer.add(backRoute);
            cs.add(routeContainer);
        }
        //TODO: Create two paths for each route when algorithm is initialized, one in each direction!!

        //Will be used to save the final paths stored in cs
        List<List<Route>> csSaver = new ArrayList<>();

        //While loop, to extend the paths stored in cs
        while (cs.size() > 0) {
            List<List<Route>> cs1 = new ArrayList<>();

            for (List<Route> c : cs) {
                /* rs:
                    - Routes belonging to player
                    - Not belonging to c
                    - Can extend c
                 */
                List<Route> rs = new ArrayList<>(routes); //Routes belonging to player
                rs.removeAll(c); //Removes all routes that belong to path c

                List<Route> rsCopy = new ArrayList<>(rs); //Using a copy to avoid ConcurrentModificationException, as we can't directly alter rs while iterating through
                //Check if routes in rs can extend c?: (Therefore path.station2 must = route.station1 or path.station1 must = route.station2)

                for (Route r : rsCopy) {
                    boolean routeExtendsPathForward = c.get(c.size() - 1).station2().equals(r.station1());

                    if (!routeExtendsPathForward) {
                        //Checks if the end station = start station, else route cannot extend path
                        rs.remove(r); //Removes all routes that can't extend path c
                    }
                }

                //Extend the path c with the route rs
                for (Route r : rs) {
                    List<Route> cExtended = new ArrayList<>(c);
                    boolean routeExtendsPathForward = c.get(c.size() - 1).station2().equals(r.station1());
                    boolean routeExtendsPathBackward = c.get(0).station1().equals(r.station2());
                    cExtended.add(r);
                    cs1.add(cExtended);
                }
            }

            csSaver = new ArrayList<>(cs);
            cs = cs1;
        }

        int maxPathLength = 0;
        int maxPathLengthIndex = 0;

        //TODO: Incorporate this method into above algorithm
        //Iterates through each path of the longestPaths, and retrieves path with longest length
        for (int i = 0; i < csSaver.size(); ++i) {
            int pathLength = 0;

            for (Route route : csSaver.get(i)) {
                pathLength += route.length();
            }

            if (maxPathLengthIndex < pathLength) {
                maxPathLengthIndex = pathLength;
                maxPathLengthIndex = i;
            }
        }
        Station station1 = csSaver.get(maxPathLengthIndex).get(0).station1();
        Station station2 = csSaver.get(maxPathLengthIndex).get(csSaver.get(maxPathLengthIndex).size() - 1).station2();

        return new Trail(csSaver.get(maxPathLengthIndex), station1, station2);
    }

    /**
     * @return returns the length of the trail
     */
    public int length() {

        for (Route w : routes) {
            length += w.length();
        }

        return length;
    }

    /**
     * @return Returns the first station of the path, else null (IFF) trail has length 0
     */
    public Station station1() {
        return ((length() > 0) ? s1 : null);
    }

    /**
     * @return Returns the last station of the path, else null (IFF) trail has length 0
     */
    public Station station2() {
        return ((length() > 0) ? s2 : null);
    }

    /**
     * @return Returns textual representation of the path, containing at least the name of first and last station (in order) as well as the length of the path in parenthesis
     */
    @Override
    public String toString() {

        String text = null;
        List<String> stations = new ArrayList<>();

        for (Route w : routes) {
            stations.add(w.station2().toString());
        }

        text = routes.get(0).station1().toString() + " - " + String.join(" - ", stations) + " (" + length() + ")";

        return text;
    }

}
