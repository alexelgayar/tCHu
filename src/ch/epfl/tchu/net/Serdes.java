package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static ch.epfl.tchu.game.PlayerId.*;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Class containing serdes each capable of (de)serializing a type of object
 */
public final class Serdes {

    private static final String COMMA_SEPARATOR = ",";
    private static final String SEMICOLON_SEPARATOR = ";";
    private static final String COLON_SEPARATOR = ":";

    /**
     * Serde capable of (de)serializing an integer
     */
    public static final Serde<Integer> INT_SERDE = Serde.of(
            i -> Integer.toString(i),
            Integer::parseInt
    );

    /**
     * Serde capable of (de)serializing a string
     */
    public static final Serde<String> STRING_SERDE = Serde.of(
            (String s) -> Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8)),
            (String s) -> new String(Base64.getDecoder().decode(s),StandardCharsets.UTF_8)
    );

    /**
     * Serde capable of (de)serializing a PlayerId
     */
    public static final Serde<PlayerId> PLAYER_ID_SERDE = new Serde<>() {
        @Override
        public String serialize(PlayerId playerId) {
          return (playerId == null) ? "" : Serde.oneOf(PlayerId.ALL).serialize(playerId);
        }

        @Override
        public PlayerId deserialize(String serializedObject) {
            return (serializedObject.equals("")) ? null : Serde.oneOf(PlayerId.ALL).deserialize(serializedObject);
        }
    };

    /**
     * Serde capable of (de)serializing a TurnKind
     */
    public static final Serde<Player.TurnKind> TURN_KIND_SERDE = Serde.oneOf(Player.TurnKind.ALL);

    /**
     * Serde capable of (de)serializing a card
     */
    public static final Serde<Card> CARD_SERDE = Serde.oneOf(Card.ALL);

    /**
     * Serde capable of (de)serializing a route
     */
    public static final Serde<Route> ROUTE_SERDE = Serde.oneOf(ChMap.routes());

    /**
     * Serde capable of (de)serializing a ticket
     */
    public static final Serde<Ticket> TICKET_SERDE = Serde.oneOf(ChMap.tickets());

    /**
     * Serde capable of (de)serializing a list of strings
     */
    public static final Serde<List<String>> STRING_LIST_SERDE = Serde.listOf(STRING_SERDE, COMMA_SEPARATOR);

    /**
     * Serde capable of (de)serializing a list of cards
     */
    public static final Serde<List<Card>> CARD_LIST_SERDE = Serde.listOf(CARD_SERDE, COMMA_SEPARATOR);

    /**
     * Serde capable of (de)serializing a list of routes
     */
    public static final Serde<List<Route>> ROUTE_LIST_SERDE = Serde.listOf(ROUTE_SERDE, COMMA_SEPARATOR);

    /**
     * Serde capable of (de)serializing a sorted bag of cards
     */
    public static final Serde<SortedBag<Card>> CARD_BAG_SERDE = Serde.bagOf(CARD_SERDE, COMMA_SEPARATOR);

    /**
     * Serde capable of (de)serializing a sorted bag of tickets
     */
    public static final Serde<SortedBag<Ticket>> TICKET_BAG_SERDE = Serde.bagOf(TICKET_SERDE, COMMA_SEPARATOR);

    /**
     * Serde capable of (de)serializing list of sorted bags of cards
     */
    public static final Serde<List<SortedBag<Card>>> CARD_BAG_LIST_SERDE = Serde.listOf(CARD_BAG_SERDE, SEMICOLON_SEPARATOR);

    /**
     * Serde capable of (de)serializing a PublicCardState
     */
    public static final Serde<PublicCardState> PUBLIC_CARD_STATE_SERDE = new Serde<>() {

        /**
         * Methods that serializes a PublicCardState
         * @param plainObject the PublicCardSate to be serialized
         * @return the serialized PublicCardState
         */
        @Override
        public String serialize(PublicCardState plainObject) {

            return String.join(SEMICOLON_SEPARATOR,
                    CARD_LIST_SERDE.serialize(plainObject.faceUpCards()),
                    INT_SERDE.serialize(plainObject.deckSize()),
                    INT_SERDE.serialize(plainObject.discardsSize()));
        }

        /**
         * Method that deserializes a PublicCardState
         * @param serializedObject the serialized string that must be converted back into the PublicCardState
         * @return the deserialized PublicCardState
         */
        @Override
        public PublicCardState deserialize(String serializedObject) {
            String[] s = serializedObject.split(Pattern.quote(SEMICOLON_SEPARATOR), -1);

            return new PublicCardState(
                    CARD_LIST_SERDE.deserialize(s[0]),
                    INT_SERDE.deserialize(s[1]),
                    INT_SERDE.deserialize(s[2]));
        }
    };

    /**
     * Serde capable of (de)serializing a PublicPlayerState
     */
    public static final Serde<PublicPlayerState> PUBLIC_PLAYER_STATE_SERDE = new Serde<>() {

        /**
         * Methods that serializes a PublicPlayerState
         * @param plainObject the PublicPlayerState to be serialized
         * @return the serialized PublicPlayerState
         */
        @Override
        public String serialize(PublicPlayerState plainObject) {

            return String.join(SEMICOLON_SEPARATOR,
                    INT_SERDE.serialize(plainObject.ticketCount()),
                    INT_SERDE.serialize(plainObject.cardCount()),
                    ROUTE_LIST_SERDE.serialize(plainObject.routes()));
        }

        /**
         * Method that deserializes a PublicPlayerState
         * @param serializedObject the serialized string that must be converted back into the PublicPlayerState
         * @return the deserialized PublicPlayerState
         */
        @Override
        public PublicPlayerState deserialize(String serializedObject) {

            String[] s = serializedObject.split(Pattern.quote(SEMICOLON_SEPARATOR), -1);

            return new PublicPlayerState(
                    INT_SERDE.deserialize(s[0]),
                    INT_SERDE.deserialize(s[1]),
                    ROUTE_LIST_SERDE.deserialize(s[2]));
        }
    };

    /**
     * Serde capable of (de)serializing a PlayerState
     */
    public static final Serde<PlayerState> PLAYER_STATE_SERDE = new Serde<>() {

        /**
         * Methods that serializes a PlayerState
         * @param plainObject the PlayerState to be serialized
         * @return the serialized PlayerState
         */
        @Override
        public String serialize(PlayerState plainObject) {

            return String.join(SEMICOLON_SEPARATOR,
                    TICKET_BAG_SERDE.serialize(plainObject.tickets()),
                    CARD_BAG_SERDE.serialize(plainObject.cards()),
                    ROUTE_LIST_SERDE.serialize(plainObject.routes()));
        }

        /**
         * Method that deserializes a PlayerState
         * @param serializedObject the serialized string that must be converted back into the PlayerState
         * @return the deserialized PlayerState
         */
        @Override
        public PlayerState deserialize(String serializedObject) {

            String[] s = serializedObject.split(Pattern.quote(SEMICOLON_SEPARATOR), -1);

            return new PlayerState(
                    TICKET_BAG_SERDE.deserialize(s[0]),
                    CARD_BAG_SERDE.deserialize(s[1]),
                    ROUTE_LIST_SERDE.deserialize(s[2]));
        }
    };

    /**
     * Serde capable of (de)serializing a PublicGameState
     */
    public static final Serde<PublicGameState> PUBLIC_GAME_STATE_SERDE = new Serde<>() {

        /**
         * Methods that serializes a PublicGameState
         * @param plainObject the PublicGameState to be serialized
         * @return the serialized PublicGameState
         */
        @Override
        public String serialize(PublicGameState plainObject) {

            return String.join(COLON_SEPARATOR,
                    INT_SERDE.serialize(plainObject.ticketsCount()),
                    PUBLIC_CARD_STATE_SERDE.serialize(plainObject.cardState()),
                    PLAYER_ID_SERDE.serialize(plainObject.currentPlayerId()),
                    PUBLIC_PLAYER_STATE_SERDE.serialize(plainObject.playerState(PLAYER_1)),
                    PUBLIC_PLAYER_STATE_SERDE.serialize(plainObject.playerState(PlayerId.PLAYER_2)),
                    PLAYER_ID_SERDE.serialize(plainObject.lastPlayer()));
        }

        /**
         * Method that deserializes a PublicGameState
         * @param serializedObject the serialized string that must be converted back into the PublicGameState
         * @return the deserialized PublicGameState
         */
        @Override
        public PublicGameState deserialize(String serializedObject) {

            String[] s = serializedObject.split(Pattern.quote(COLON_SEPARATOR), -1);

            Map<PlayerId, PublicPlayerState> playerState = Map.of(PLAYER_1, PUBLIC_PLAYER_STATE_SERDE.deserialize(s[3]),
                    PlayerId.PLAYER_2, PUBLIC_PLAYER_STATE_SERDE.deserialize(s[4]));

            return new PublicGameState(
                    INT_SERDE.deserialize(s[0]),
                    PUBLIC_CARD_STATE_SERDE.deserialize(s[1]),
                    PLAYER_ID_SERDE.deserialize(s[2]),
                    playerState,
                    PLAYER_ID_SERDE.deserialize(s[5]));

        }
    };
}

