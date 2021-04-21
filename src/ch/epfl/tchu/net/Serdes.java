package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

import static ch.epfl.tchu.game.Card.*;
import static ch.epfl.tchu.game.PlayerId.*;

public class Serdes {

    public static final Serde<Integer> intSerde = Serde.of(
            i -> Integer.toString(i),
            Integer::parseInt);



    public static final Serde<String> stringSerde = Serde.of(
            (String s) -> Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8)),
            (String s) -> new String(Base64.getDecoder().decode(s),StandardCharsets.UTF_8));


    public static final Serde<PlayerId> playerIdSerde = new Serde<PlayerId>() {
        @Override
        public String serialize(PlayerId yes) {
          return (yes == null) ? "" : Serde.oneOf(PlayerId.ALL).serialize(yes);
        }

        @Override
        public PlayerId deserialize(String alsoYes) {
            return (alsoYes == "") ? null : (PlayerId) Serde.oneOf(PlayerId.ALL).deserialize(alsoYes);
        }
    };

    public static final Serde<Player.TurnKind> turnKindSerde = Serde.oneOf(Player.TurnKind.ALL);

    public static final Serde<Card> cardSerde = Serde.oneOf(Card.ALL);

    public static final Serde<Route> routeSerde = Serde.oneOf(ChMap.routes());

    public static final Serde<Ticket> ticketSerde = Serde.oneOf(ChMap.tickets());

    public static final Serde<List<String>> stringListSerde = Serde.listOf(stringSerde, ",");

    public static final Serde<List<Card>> cardListSerde = Serde.listOf(cardSerde, ",");

    public static final Serde<List<Route>> routeListSerde = Serde.listOf(routeSerde, ",");

    public static final Serde<SortedBag<Card>> cardBagSerde = Serde.bagOf(cardSerde, ",");

    public static final Serde<SortedBag<Ticket>> ticketBagSerde = Serde.bagOf(ticketSerde, ",");

    public static final Serde<List<SortedBag<Card>>> cardBagListSerde = Serde.listOf(cardBagSerde, ";");

    public static final Serde<PublicCardState> publicCardStateSerde = new Serde<PublicCardState>() {

        @Override
        public String serialize(PublicCardState yes) {

            return String.join(";",
                    cardListSerde.serialize(yes.faceUpCards()),
                    intSerde.serialize(yes.deckSize()),
                    intSerde.serialize(yes.discardsSize()));
        }

        @Override
        public PublicCardState deserialize(String alsoYes) {

            String[] s = alsoYes.split(Pattern.quote(";"), -1);

            return new PublicCardState(
                    cardListSerde.deserialize(s[0]),
                    intSerde.deserialize(s[1]),
                    intSerde.deserialize(s[2]));
        }
    };

    public static final Serde<PublicPlayerState> publicPlayerStateSerde = new Serde<PublicPlayerState>() {

        @Override
        public String serialize(PublicPlayerState yes) {

            return String.join(";",
                    intSerde.serialize(yes.ticketCount()),
                    intSerde.serialize(yes.cardCount()),
                    routeListSerde.serialize(yes.routes()));
        }

        @Override
        public PublicPlayerState deserialize(String alsoYes) {

            String[] s = alsoYes.split(Pattern.quote(";"), -1);

            return new PublicPlayerState(
                    intSerde.deserialize(s[0]),
                    intSerde.deserialize(s[1]),
                    routeListSerde.deserialize(s[2]));
        }
    };

    public static final Serde<PlayerState> playerStateSerde = new Serde<PlayerState>() {

        @Override
        public String serialize(PlayerState yes) {

            return String.join(";",
                    ticketBagSerde.serialize(yes.tickets()),
                    cardBagSerde.serialize(yes.cards()),
                    routeListSerde.serialize(yes.routes()));
        }

        @Override
        public PlayerState deserialize(String alsoYes) {

            String[] s = alsoYes.split(Pattern.quote(";"), -1);

            return new PlayerState(
                    ticketBagSerde.deserialize(s[0]),
                    cardBagSerde.deserialize(s[1]),
                    routeListSerde.deserialize(s[2]));
        }
    };

    public static final Serde<PublicGameState> publicGameStateSerde = new Serde<PublicGameState>() {
        @Override
        public String serialize(PublicGameState yes) {

            return String.join(":",
                    intSerde.serialize(yes.ticketsCount()),
                    publicCardStateSerde.serialize(yes.cardState()),
                    playerIdSerde.serialize(yes.currentPlayerId()),
                    publicPlayerStateSerde.serialize(yes.playerState(PLAYER_1)),
                    publicPlayerStateSerde.serialize(yes.playerState(PlayerId.PLAYER_2)),
                    playerIdSerde.serialize(yes.lastPlayer()));
        }

        @Override
        public PublicGameState deserialize(String alsoYes) {

            String[] s = alsoYes.split(Pattern.quote(":"), -1);

            Map<PlayerId, PublicPlayerState> playerState = Map.of(PLAYER_1, publicPlayerStateSerde.deserialize(s[3]),
                    PlayerId.PLAYER_2, publicPlayerStateSerde.deserialize(s[4]));

            return new PublicGameState(
                    intSerde.deserialize(s[0]),
                    publicCardStateSerde.deserialize(s[1]),
                    playerIdSerde.deserialize(s[2]),
                    playerState,
                    playerIdSerde.deserialize(s[5]));

        }
    };


    public static void main(String[] args) {

        List<Card> fu = List.of(RED, WHITE, BLUE, BLACK, RED);
        PublicCardState cs = new PublicCardState(fu, 30, 31);
        List<Route> rs1 = ChMap.routes().subList(0, 2);
        Map<PlayerId, PublicPlayerState> ps = Map.of(
                PLAYER_1, new PublicPlayerState(10, 11, rs1),
                PLAYER_2, new PublicPlayerState(20, 21, List.of()));
        PublicGameState gs =
                new PublicGameState(40, cs, PLAYER_2, ps, null);


        System.out.println(publicGameStateSerde.serialize(gs).equals("40:6,7,2,0,6;30;31:1:10;11;0,1:20;21;:"));

       String s = stringSerde.serialize("Charles");

        System.out.println(stringSerde.deserialize(s));




    }
}

