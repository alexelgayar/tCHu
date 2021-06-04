package ch.epfl.tchu.bot;

import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Game;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Station;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    void generateConnectingNodes() {
        System.out.println(ChMap.nodes().size() + " stations:" + ChMap.stations().size());

        Graph.generateConnectingNodes(ChMap.routes());

        //Bern
        Node node = ChMap.nodes().get(3);
        System.out.println("Initial node:" + node.station());

        node.getAdjacentNodes().forEach((adjacentNode, route) -> System.out.println("Node: " + adjacentNode.station() + " Route:" + route.stations() + " length:" + route.length()));

        assertEquals(1,node.getAdjacentNodes());
    }

    //Obtains the connecting node with the least edgeWeight
    @Test
    void getLeastDistanceNode(){
        Node startNode = ChMap.nodes().get(0); //Baden
        startNode.setDistance(0);

        Node olten = ChMap.nodes().get(20); //Olten
        Node zurich = ChMap.nodes().get(33); //Zurich
        Node bale = ChMap.nodes().get(1); //Bale

        Graph.generateConnectingNodes(ChMap.routes());

        startNode.getAdjacentNodes().forEach((adjacentNode, route) -> System.out.println("Node: " + adjacentNode.station() + " Route:" + route.stations() + " length:" + route.length()));

        Set<Node> unvisitedNodes = startNode.getAdjacentNodes().keySet();

        unvisitedNodes.forEach((node) -> System.out.println("Node:" + node.station()));

        startNode.getAdjacentNodes().forEach((adjacentNode, route) -> {
            Graph.calculateMinimumDistance(adjacentNode, route.length(), startNode);
        });
//        Graph.calculateMinimumDistance(olten, 2, startNode); //Correct distance
//        Graph.calculateMinimumDistance(zurich, 3, olten); //Correct distance
//        Graph.calculateMinimumDistance(bale, 3, olten); //Correct distance

        Node leastNode = Graph.getLeastDistanceNode(unvisitedNodes);

        assertEquals(zurich.station(), leastNode.station());
    }

    //Changes the distance value of the node
    @Test
    void calculateMinimumDistance(){
        Node startNode = ChMap.nodes().get(0); //Baden
        startNode.setDistance(0);

        Node midNode1 = ChMap.nodes().get(20); //Olten
        Node midNode2 = ChMap.nodes().get(16); //Lucerne
        Node endNode = ChMap.nodes().get(5); //Brusio

        Graph.generateConnectingNodes(ChMap.routes());
        Graph.calculateMinimumDistance(midNode1, 2, startNode); //Correct distance
        Graph.calculateMinimumDistance(midNode2, 3, midNode1); //Correct distance


        System.out.println(
                "startNode: " + startNode.station()
                        + "   midNode 1: " + midNode1.station()
                        + "   midNode2: " + midNode2.station()
                        + "   endNode: " + endNode.station());


        System.out.println(
                "distance sN: " + startNode.getDistance()
                        + "   distance mN1: " + midNode1.getDistance()
                        + "   distance mN2:" + midNode2.getDistance()
                        + "   distance eN:" + endNode.getDistance());

        assertEquals(5, midNode2.getDistance());
    }

    @Test
    void calculateMinimumDistance2(){
        Graph graph = new Graph();

        for (Node node : ChMap.nodes()){
            graph.addNode(node);
        }

        Node startNode = ChMap.nodes().get(0); //Baden
        startNode.setDistance(0);

        Node midNode1 = ChMap.nodes().get(20); //Olten
        Node midNode2 = ChMap.nodes().get(16); //Lucerne
        Node endNode = ChMap.nodes().get(5); //Brusio

        Graph.generateConnectingNodes(ChMap.routes());

        startNode.getAdjacentNodes().forEach((adjacentNode, route) -> {
            System.out.println("Edge weight: " + route.length() + "   initialDist: " + adjacentNode.getDistance());
            Graph.calculateMinimumDistance(adjacentNode, route.length(), startNode);
            System.out.println("  adjacentNode: " + adjacentNode.station() + " " + adjacentNode.toString() + "  distance: " + adjacentNode.getDistance());
            System.out.print("  " + startNode.getDistance() + " + " + route.length() + " < " + adjacentNode.getDistance());
            System.out.println();
            System.out.println();
        });

        System.out.println(
                "startNode: " + startNode.station()
                        + "   midNode 1: " + midNode1.station() + " " + midNode1
                        + "   midNode2: " + midNode2.station() + " " + midNode2
                        + "   endNode: " + endNode.station() + " " + endNode);


        System.out.println(
                "distance sN: " + startNode.getDistance()
                        + "   distance mN1: " + midNode1.getDistance() + " " + midNode1
                        + "   distance mN2:" + midNode2.getDistance() + " " + midNode2
                        + "   distance eN:" + endNode.getDistance() + " " + endNode);

        System.out.println();
        System.out.println("- - - - - - - - -");
        System.out.println();

        //midNode1.setDistance(2); //TODO: The distance is not stored correctly (Different instances of nodes?)
        midNode1.getAdjacentNodes().forEach((adjacentNode, route) -> {
            System.out.println("Edge weight: " + route.length() + "   initialDist: " + adjacentNode.getDistance());
            Graph.calculateMinimumDistance(adjacentNode, route.length(), midNode1);
            System.out.println("  adjacentNode: " + adjacentNode.station() + "  distance: " + adjacentNode.getDistance());
            System.out.print("  " + midNode1.getDistance() + " + " + route.length() + " < " + adjacentNode.getDistance());
            System.out.println();
            System.out.println();
        });

        System.out.println(
                "startNode: " + startNode.station()
                        + "   midNode 1: " + midNode1.station()
                        + "   midNode2: " + midNode2.station()
                        + "   endNode: " + endNode.station());


        System.out.println(
                "distance sN: " + startNode.getDistance()
                        + "   distance mN1: " + midNode1.getDistance()
                        + "   distance mN2:" + midNode2.getDistance()
                        + "   distance eN:" + endNode.getDistance());

        assertEquals(0, startNode.getDistance());
        assertEquals(2, midNode1.getDistance());
        assertEquals(5, midNode2.getDistance());

        System.out.println();
        System.out.println();
        System.out.println("Path analysis: Start node: " + startNode.station() + " midNode:" + midNode1.station());

        for (Node node : midNode1.getShortestPath()){
            System.out.println(node.station());
        }

        for (Route route : midNode1.getShortestRoute()){
            if (route == null) {
                System.out.println("Route is null");
            } else {
                System.out.println(route.stations());
            }
        }

        //Checking the routes are correct
        assertEquals(1, midNode1.getShortestPath());
        assertEquals(2, midNode2.getShortestRoute());
    }

    @Test
    void calculateShortestPathFromSource(){
        Graph graph = new Graph();

        for (Node node : ChMap.nodes()){
            graph.addNode(node);
        }

        Node startNode = ChMap.nodes().get(0); //Baden
        Node midNode1 = ChMap.nodes().get(20); //Olten
        Node midNode2 = ChMap.nodes().get(16); //Lucerne
        Node endNode = ChMap.nodes().get(5); //Brusio

        Graph.generateConnectingNodes(ChMap.routes());
        graph = Graph.calculateShortestPathFromSource(graph, startNode);

        System.out.println(
                "startNode: " + startNode.station()
                + "   midNode 1: " + midNode1.station()
                + "   midNode2: " + midNode2.station()
                + "   endNode: " + endNode.station());


        System.out.println(
                "distance sN: " + startNode.getDistance()
                + "   distance mN1: " + midNode1.getDistance()
                + "   distance mN2:" + midNode2.getDistance()
                + "   distance eN:" + endNode.getDistance());

        assertEquals(0, startNode.getDistance());
        assertEquals(null, endNode.getDistance());
        assertEquals(null, endNode.getShortestRoute());
    }

    @Test
    void getShortestPathBetweenTwoRoutes(){

    }

}