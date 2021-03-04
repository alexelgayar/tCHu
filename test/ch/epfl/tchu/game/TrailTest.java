package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrailTest {

    @Test
    void LongestTest() {

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
        assertEquals("Lucerne - Sion (38)", longest.toString());

    }

}
