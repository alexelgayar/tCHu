package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Public, final and immutable class
 * Contains no constructor, but contains a static method to compute the longest trail of a given route network
 *
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 */
public final class Trail {

    private final int length;
    private final List<Route> routes;
    private final Station s1;
    private final Station s2;

    private Trail(Station s1, Station s2, List<Route> routes, int length) {
        this.s1 = s1;
        this.s2 = s2;
        this.routes = routes;
        this.length = length;
    }

    private static List<Trail> generateTrails(List<Route> routes){
        List<Trail> trailContainer = new ArrayList<>();
        for (Route route : routes){
            trailContainer.add(new Trail(route.station1(), route.station2(), List.of(route), route.length()));
            trailContainer.add(new Trail(route.station2(), route.station1(), List.of(route), route.length()));
        }

        return trailContainer;
    }

    private static Trail extendTrail(Trail trail, List<Route> routes){
        List<Route> rs = new ArrayList<>(routes);
        int length = 0;

        //Remove all routes that are already part of the trail
        rs.removeAll(trail.routes);

        List<Route> rsCopy = new ArrayList<>(rs);

        //Prevents ConcurrentModificationException
        for (Route r: rsCopy){
            boolean routeExtendsPathForward = trail.station2().equals(r.station1());

            if (!routeExtendsPathForward){
                rs.remove(r);
            }
        }

        Trail extendedTrail = new Trail(null, trail.station2(), trail.routes, length);

        for (Route r: rs){
            length += r.length();

            List<Route> extendedTrailRoutes = new ArrayList<>();
            extendedTrailRoutes.addAll(trail.routes);
            extendedTrailRoutes.add(r);

            extendedTrail = new Trail(trail.station1(), r.station2(), extendedTrailRoutes, length);
        }

        return extendedTrail;
    }

    /**
     * Receives list of all player-owned routes, returns the longest route of the player
     *
     * @param routes All the routes that are owned by the player
     * @return Returns the longest path of the network made up of the given routes. If multiple paths of max length => Returned path is not specified
     */
    public static Trail longest(List<Route> routes) {
        //Step 1: Create trail for each route, two trails for each route
        List<Trail> cs = generateTrails(routes);
        Trail longestTrail = cs.get(0);

        int iterationCounter = 0;
        //Step 2: Extend each trail
        while (cs.size() > 0) {
            System.out.println("Number of iterations: " + iterationCounter);
            ++iterationCounter;
            List<Trail> cs1 = new ArrayList<>();

            for (Trail c : cs) {
                Trail extendedTrail = extendTrail(c, routes);
                if (longestTrail.length() < extendedTrail.length()){
                    longestTrail = extendedTrail;
                }

                if (extendedTrail.station1() != null){
                    cs1.add(extendedTrail);
                }
            }
            cs = cs1;
        }

        return longestTrail;
    }

    /**
     * @return returns the length of the trail
     */
    public int length() {
        int lengthHolder = length;
        for (Route w : routes) {
            lengthHolder += w.length();
        }

        return lengthHolder;
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
