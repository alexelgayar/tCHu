package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.List;

public class Serdes {

    public static final Serde<Integer> intSerde = Serde.of(
            i -> Integer.toString(i),
            Integer::parseInt);

   // public static final Serde<String> stringSerde;

    public static final Serde<PlayerId> playerIdSerde = Serde.oneOf(PlayerId.ALL);

    public static final Serde<Player.TurnKind> turnKindSerde = Serde.oneOf(Player.TurnKind.ALL);

    public static final Serde<Card> cardSerde = Serde.oneOf(Card.ALL);

    public static final Serde<Route> routeSerde = Serde.oneOf(ChMap.routes());

    public static final Serde<Ticket> ticketSerde = Serde.oneOf(ChMap.tickets());

   // public static final Serde<List<String>> stringListSerde = Serde.listOf(stringSerde, ",");

    public static final Serde<List<Card>> cardListSerde = Serde.listOf(cardSerde, ",");

    public static final Serde<List<Route>> routeListSerde = Serde.listOf(routeSerde, ",");

    public static final Serde<SortedBag<Card>> cardBagSerde = Serde.bagOf(cardSerde, ",");

    public static final Serde<SortedBag<Ticket>> ticketBagSerde = Serde.bagOf(ticketSerde, ",");

    public static final Serde<List<SortedBag<Card>>> cardBagListSerde = Serde.listOf(cardBagSerde, ";");

    public static void main(String[] args) {


        List<SortedBag<Card>> listee = List.of(SortedBag.of(Card.ALL.subList(4,8)), SortedBag.of(Card.ALL.subList(0, 5)));

        String serialized = cardBagListSerde.serialize(listee);

        System.out.println(serialized);

        List<SortedBag<Card>> deserialized = cardBagListSerde.deserialize(serialized);

        for(SortedBag<Card> c : deserialized){
            for(Card d: c.toList()){
                System.out.println(d);
            }
        }


    }
}
