package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrailTest {

    @Test
    void textIsCorrect(){
        var s1 = new Station(11, "Interlaken");
        var s2 = new Station(33, "Zürich");
        List<Route> routes = List.of(
                new Route("INT_LUC_1", new Station(11, "Interlaken"), new Station(16, "Lucerne"), 4, Route.Level.OVERGROUND, Color.VIOLET),
                new Route("LUC_ZOU_1", new Station(16, "Lucerne"), new Station(32, "Zoug"), 1, Route.Level.OVERGROUND, Color.ORANGE),
                new Route("ZOU_ZUR_1", new Station(32, "Zoug"), new Station(33, "Zürich"), 1, Route.Level.OVERGROUND, Color.GREEN));

        var r1 = new Trail(routes, s1, s2);

        assertEquals(
                "Interlaken - Lucerne - Zoug - Zürich (6)", r1.toString());
    }

    @Test
    void LongestTestNonTrivialTest() {

        List<Route> routes = List.of(ChMap.routes().get(64),
                ChMap.routes().get(55), ChMap.routes().get(49),
                ChMap.routes().get(43), ChMap.routes().get(65),
                ChMap.routes().get(66));
        List<Route> routes2 = List.of(ChMap.routes().get(13),
                ChMap.routes().get(16), ChMap.routes().get(18),
                ChMap.routes().get(19), ChMap.routes().get(65),
                ChMap.routes().get(66), ChMap.routes().get(64),
                ChMap.routes().get(55), ChMap.routes().get(47),
                ChMap.routes().get(44), ChMap.routes().get(48),
                ChMap.routes().get(56));
        Trail longest = Trail.longest(routes2);
        for (Route route:routes2){
            System.out.println(route.station1() + " to " + route.station2());
        }
        assertEquals("Lucerne - Sion (38)", longest.toString());

    }

    @Test
    void LongestTestTrivialTest() {
        //            new Route("GEN_LAU_2", GEN, LAU, 4, Level.OVERGROUND, Color.BLUE), 47
        //            new Route("GEN_YVE_1", GEN, YVE, 6, Level.OVERGROUND, null), 49
        //            new Route("LAU_NEU_1", LAU, NEU, 4, Level.OVERGROUND, null), 57
        List<Route> routes = List.of(ChMap.routes().get(47), ChMap.routes().get(48), ChMap.routes().get(56));
        for (Route route:routes){
            System.out.println(route.station1() + " to " + route.station2());
        }
        Trail longest = Trail.longest(routes);
        assertEquals("Yverdon - Genève - Lausanne - Neuchâtel (14)", longest.toString());
    }
}
