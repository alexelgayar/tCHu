package ch.epfl.tchu.bot;

import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Station;

import java.util.*;

/**
 * For each station, we create a node so that we can apply the dijkstra algorithm
 */
public final class Node {
    private final int id;
    private final Station station;

    private Map<Node,Route> adjacentNodes; //Nodes that do not have integer distance = infinity
    //private Map<Node, Route> routes;

    private LinkedList<Node> shortestPath = new LinkedList<>(); //The shortest path between this and the source route
    private LinkedList<Route> shortestRoute = new LinkedList<>();
    private Integer distance = Integer.MAX_VALUE;


    public Node(Station station){
        this.id = station.id();
        this.station = station;

        this.adjacentNodes = new HashMap<>();
    }

    public void setDistance(Integer distance) {
        this.distance = distance; //TODO: The distance is set but something changes?
    }

    public Integer getDistance() {
        return distance;
    }

    public void setShortestPath(LinkedList<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public LinkedList<Node> getShortestPath() {
        return shortestPath;
    }

    public Map<Node, Route> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setAdjacentNodes(Map<Node, Route> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    public Station station() {
        return station;
    }

    public int id() {
        return id;
    }

//    public static List<Node> of(List<Route> routes){
//        List<Node> nodes = new ArrayList<>();
//
//        for (Route route: routes){
//            Node routeNode = new Node(route);
//            nodes.add(routeNode);
//        }
//
//        return nodes;
//    }

    public boolean isConnected(Node connecting){
        return adjacentNodes.containsKey(connecting);
    }

    public LinkedList<Route> getShortestRoute() {
        return shortestRoute;
    }

    public void setShortestRoute(LinkedList<Route> shortestRoute) {
        this.shortestRoute = shortestRoute;
    }
}
