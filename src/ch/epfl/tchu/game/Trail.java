package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public, final and immutable class. Represents a trail in a player network
 */
public final class Trail {

    private final Station s1;
    private final Station s2;
    private final List<Route> routes;
    private final int length;

    private Trail(Station s1, Station s2, List<Route> r) {
        this.s1 = s1;
        this.s2 = s2;
        this.routes = r;
        this.length = computeTrailLength(r);
    }

    /**
     * Returns the longest trail belonging to the network formed from the given routes
     * @param routes All the routes that are owned by the player
     * @return Returns the longest trail belonging to the network formed from the given routes.
     */
    public static Trail longest(List<Route> routes){
        //1. Create 2 trails for each route
        List<Trail> shortTrails = generateShortTrails(routes);
        Trail longestTrail;

        //2. Extend each trail
        if (routes.isEmpty()){
            longestTrail = new Trail(null, null, routes);
        }
        else{
            longestTrail = shortTrails.get(0);
            while (!shortTrails.isEmpty()){
                List<Trail> tempTrail = new ArrayList<>();
                for (Trail trail : shortTrails){
                    //Ensures if routes are disconnected, we are returned the longest trail
                    longestTrail = computeLongestTrail(longestTrail, trail);

                    List<Route> possibleRouteExtensions = computePossibleRouteExtensions(trail, routes);

                    for(Route trailExtension:possibleRouteExtensions){
                        tempTrail.add(computeExtendedTrail(trail, trailExtension));
                    }
                }
                shortTrails = tempTrail;
            }
        }

        //3. Return the longest trail from the generated Trails
        return longestTrail;
    }

    //Generates 2 trails for each route belonging to the routes
    private static List<Trail> generateShortTrails(List<Route> routes){
        List<Trail> shortTrails = new ArrayList<>();
        for (Route route : routes){
            shortTrails.add(new Trail(route.station1(), route.station2(), List.of(route)));
            shortTrails.add(new Trail(route.station2(), route.station1(), List.of(route)));
        }

        return shortTrails;
    }

    //Determines the longest trail given the current longest trail and other trail
    private static Trail computeLongestTrail(Trail currentTrail, Trail otherTrail){
        return (currentTrail.length() < otherTrail.length() ? otherTrail : currentTrail);
    }

    //Computes the list of routes that could serve as a route extension
    private static List<Route> computePossibleRouteExtensions(Trail trail, List<Route> routes){
        List<Route> possibleRouteExtensions = new ArrayList<>();
        for (Route route : routes){
            assert trail.station2() != null;
            boolean routeCanExtendTrail = (trail.station2().equals(route.station1()) || trail.station2().equals(route.station2())) && (!trail.routes.contains(route));
            if (routeCanExtendTrail){
                possibleRouteExtensions.add(route);
            }
        }
        return possibleRouteExtensions;
    }

    //Computes the trail with the added route to the end
    private static Trail computeExtendedTrail(Trail trail, Route trailExtension){
        List<Route> extendedRoute = new ArrayList<>(trail.routes);
        extendedRoute.add(trailExtension);

        assert trail.station2() != null;
        return new Trail(trail.station1(), trailExtension.stationOpposite(trail.station2()), extendedRoute);
    }


    /**
     * Returns the length of the trail
     * @return the length of the trail
     */
    public int length() {
        return length;
    }

    //Method to compute the length of the trail given the routes belonging to the trail
    private static int computeTrailLength(List<Route> routes){
        int length = 0;
        //For-each loop not executed if routes.isEmpty => returns length 0
        for (Route route : routes) {
            length += route.length();
        }

        return length;
    }

    /**
     * Returns the first station of the path, else null (IFF) trail has length 0
     * @return the first station of the path, else null (IFF) trail has length 0
     */
    public Station station1() {
        return ((length() > 0) ? s1 : null);
    }

    /**
     * Returns the last station of the path, else null (IFF) trail has length 0
     * @return the last station of the path, else null (IFF) trail has length 0
     */
    public Station station2() {
        return ((length() > 0) ? s2 : null);
    }

    /**
     * Redefinition of toString, and returns a textual representation of the trail containing at least the name of the first and last station and the length of the trail in parenthesis
     * @return a textual representation of the trail containing at least the name of the first and last station and the length of the trail in parenthesis
     */
    @Override
    public String toString() { //TODO: Is there a less complicated method for the toString?
        String text = "Null - Null (0)";
        List<String> stations = new ArrayList<>();
        Station temp = null;

        for(int i = 0; i < routes.size() - 1; ++i){
            if(routes.get(i).station1().equals(routes.get(i+1).station1()) || routes.get(i).station1().equals(routes.get(i+1).station2())){
                temp = routes.get(i).station1();
            }

            if(routes.get(i).station2().equals(routes.get(i+1).station1()) || routes.get(i).station2().equals(routes.get(i+1).station2())){
                temp = routes.get(i).station2();
            }

            assert temp != null;
            stations.add(temp.toString());
        }

        if (s1 != null || s2 != null) {
            text = s1 + " - " + String.join(" - ", stations) + " - " + s2 + " (" + length() + ")";
        }

        return text;
    }

}
