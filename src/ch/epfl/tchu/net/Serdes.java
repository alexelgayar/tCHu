package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static ch.epfl.tchu.game.Card.*;
import static ch.epfl.tchu.game.PlayerId.*;

public class Serdes {

    public static final Serde<Integer> intSerde = Serde.of(
            i -> Integer.toString(i),
            Integer::parseInt
    );

    public static final Serde<String> stringSerde = Serde.of(
            (String s) -> Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8)),
            (String s) -> new String(Base64.getDecoder().decode(s),StandardCharsets.UTF_8)
    );

    public static final Serde<PlayerId> playerIdSerde = new Serde<>() {
        @Override
        public String serialize(PlayerId playerId) {
          return (playerId == null) ? "" : Serde.oneOf(PlayerId.ALL).serialize(playerId);
        }

        @Override
        public PlayerId deserialize(String serializedObject) {
            return (serializedObject.equals("")) ? null : Serde.oneOf(PlayerId.ALL).deserialize(serializedObject);
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

    public static final Serde<PublicCardState> publicCardStateSerde = new Serde<>() {

        @Override
        public String serialize(PublicCardState plainObject) {

            return String.join(";",
                    cardListSerde.serialize(plainObject.faceUpCards()),
                    intSerde.serialize(plainObject.deckSize()),
                    intSerde.serialize(plainObject.discardsSize()));
        }

        @Override
        public PublicCardState deserialize(String serializedObject) {
            String[] s = serializedObject.split(Pattern.quote(";"), -1);

            return new PublicCardState(
                    cardListSerde.deserialize(s[0]),
                    intSerde.deserialize(s[1]),
                    intSerde.deserialize(s[2]));
        }
    };

    public static final Serde<PublicPlayerState> publicPlayerStateSerde = new Serde<>() {

        @Override
        public String serialize(PublicPlayerState plainObject) {

            return String.join(";",
                    intSerde.serialize(plainObject.ticketCount()),
                    intSerde.serialize(plainObject.cardCount()),
                    routeListSerde.serialize(plainObject.routes()));
        }

        @Override
        public PublicPlayerState deserialize(String serializedObject) {

            String[] s = serializedObject.split(Pattern.quote(";"), -1);

            return new PublicPlayerState(
                    intSerde.deserialize(s[0]),
                    intSerde.deserialize(s[1]),
                    routeListSerde.deserialize(s[2]));
        }
    };

    public static final Serde<PlayerState> playerStateSerde = new Serde<>() {

        @Override
        public String serialize(PlayerState plainObject) {

            return String.join(";",
                    ticketBagSerde.serialize(plainObject.tickets()),
                    cardBagSerde.serialize(plainObject.cards()),
                    routeListSerde.serialize(plainObject.routes()));
        }

        @Override
        public PlayerState deserialize(String serializedObject) {

            String[] s = serializedObject.split(Pattern.quote(";"), -1);

            return new PlayerState(
                    ticketBagSerde.deserialize(s[0]),
                    cardBagSerde.deserialize(s[1]),
                    routeListSerde.deserialize(s[2]));
        }
    };

    public static final Serde<PublicGameState> publicGameStateSerde = new Serde<>() {
        @Override
        public String serialize(PublicGameState plainObject) {

            return String.join(":",
                    intSerde.serialize(plainObject.ticketsCount()),
                    publicCardStateSerde.serialize(plainObject.cardState()),
                    playerIdSerde.serialize(plainObject.currentPlayerId()),
                    publicPlayerStateSerde.serialize(plainObject.playerState(PLAYER_1)),
                    publicPlayerStateSerde.serialize(plainObject.playerState(PlayerId.PLAYER_2)),
                    playerIdSerde.serialize(plainObject.lastPlayer()));
        }

        @Override
        public PublicGameState deserialize(String serializedObject) {

            String[] s = serializedObject.split(Pattern.quote(":"), -1);

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
}

