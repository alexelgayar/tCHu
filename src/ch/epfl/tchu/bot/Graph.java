package ch.epfl.tchu.bot;

import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Station;

import java.util.*;

public class Graph {

    private Set<Node> nodes = new HashSet<>();

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void removeNode(Node node) {
        nodes.remove(node);
    }

    public Set<Node> getNodes() {
        return nodes;
    }


    //For each node (station), generate a list of the immediately connecting nodes in the form of Routes
    public static void generateConnectingNodes() {

        //Iterate through all nodes
        for (Node node : ChMap.nodes()) {
            Map<Node, Route> nodeRouteMap = node.getAdjacentNodes();

            //Iterate through all routes
            for (Route route : ChMap.routes()) {

                //(If route.contains(station) then: station.isConnected to route.stationOpposite(station) + route.length
                if (route.stations().contains(node.station())) {

                    Node getNode = ChMap.mappedNodes().get(route.stationOpposite(node.station()));

                    nodeRouteMap.put(getNode, route);

                    node.setAdjacentNodes(nodeRouteMap);
                }
            }
        }
    }

    //Implementing Graph structure (for Dijkstra algorithm)
    public static Graph calculateShortestPathFromSource(Graph graph, Node source) {
        source.setDistance(0); //the distance away from the initial node is 0

        Set<Node> visitedNodes = new HashSet<>();
        Set<Node> unvisitedNodes = new HashSet<>();

        unvisitedNodes.add(source); //Start iteration with source node, look for connecting node

        while (unvisitedNodes.size() != 0) {
            Node currentNode = getLeastDistanceNode(unvisitedNodes); //Start with node, with lowest distance from the unvisited nodes set

            unvisitedNodes.remove(currentNode); //It 0: A is removed

            for (Map.Entry<Node, Route> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue().length();
                if (!visitedNodes.contains(adjacentNode) && adjacentNode != null) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unvisitedNodes.add(adjacentNode);
                }
            }

            visitedNodes.add(currentNode);
        }
        return graph;
    }

    ////Get route with lowest distance from the unsettled nodes set
    public static Node getLeastDistanceNode(Set<Node> unvisitedNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;

        for (Node node : unvisitedNodes) {
            int nodeDistance = node.getDistance(); //Distance is the distance of the node from the source
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }

        return lowestDistanceNode;
    }

    //Calculates the actual distance with the newly calculated one, while following the newly explored path
    public static void calculateMinimumDistance(Node evaluationNode, Integer edgeWeight, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance(); //distance = 0 for original node, then increases for all other nodes

        if (evaluationNode == null) {
            System.out.println("evaluationNode is null");
        }

        if (sourceDistance + edgeWeight < evaluationNode.getDistance()) { //If this new path is the shortest new path
            evaluationNode.setDistance(sourceDistance + edgeWeight);

            //Update the shortestPath length
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);

            LinkedList<Route> shortestRoutes = new LinkedList<>(sourceNode.getShortestRoute());

            shortestRoutes.add(sourceNode.getAdjacentNodes().get(evaluationNode));
            evaluationNode.setShortestRoute(shortestRoutes);
        }
    }

    private static Node getChMapNode(Node alteredNode) {
        for (Node node : ChMap.nodes()) {

        }
        return null;
    }

    public static List<Route> getShortestPathBetweenTwoRoutes(Graph graph, Node initialNode, Node finalNode) {
        graph = calculateShortestPathFromSource(graph, initialNode);

        return (finalNode.getShortestRoute() != null)
                ? finalNode.getShortestRoute()
                : new ArrayList<>();
    }
}
