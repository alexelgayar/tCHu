package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public, final, immutable. Allows to generate texts describing the progress of the game.
 */
public final class Info {

    private final String playerName;

    /**
     * Builds a generator of messages related to the player with the given name
     *
     * @param playerName the name of the player
     */
    public Info(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Returns the name (french) of the data card, the singular iff the absolute value of the second argument is 1
     *
     * @param card  the data card
     * @param count the number of cards
     * @return the name (french) of the data card, the singular iff the absolute value of the second argument is 1
     */
    public static String cardName(Card card, int count) {
        switch (card) {
            case BLACK:
                return StringsFr.BLACK_CARD + StringsFr.plural(count);
            case BLUE:
                return StringsFr.BLUE_CARD + StringsFr.plural(count);
            case RED:
                return StringsFr.RED_CARD + StringsFr.plural(count);
            case VIOLET:
                return StringsFr.VIOLET_CARD + StringsFr.plural(count);
            case GREEN:
                return StringsFr.GREEN_CARD + StringsFr.plural(count);
            case WHITE:
                return StringsFr.WHITE_CARD + StringsFr.plural(count);
            case YELLOW:
                return StringsFr.YELLOW_CARD + StringsFr.plural(count);
            case ORANGE:
                return StringsFr.ORANGE_CARD + StringsFr.plural(count);
            case LOCOMOTIVE:
                return StringsFr.LOCOMOTIVE_CARD + StringsFr.plural(count);
            default:
                throw new Error();
        }
    }

    /**
     * Returns a message declaring that the players, whose names are those given, have finished the game tied by having each won the given points
     *
     * @param playerNames the names of the two players that have drawn
     * @param points      the points that the players obtained (to draw)
     * @return a message declaring that the players, whose names are those given, have finished the game tied by having each won the given points
     */
    public static String draw(List<String> playerNames, int points) {
        return String.format(StringsFr.DRAW, playerNames.get(0) + " et " + playerNames.get(1), points);
    }

    /**
     * Returns the name of the player that will play first
     *
     * @return the name of the player that will play first
     */
    public String willPlayFirst() {
        return String.format(StringsFr.WILL_PLAY_FIRST, playerName);
    }

    public String keptTickets(int count) {
        return String.format(StringsFr.KEPT_N_TICKETS, playerName, count, StringsFr.plural(count));
    }

    /**
     * Returns a message declaring that a player can play
     *
     * @return a message declaring that a player can play
     */
    public String canPlay() {
        return String.format(StringsFr.CAN_PLAY, playerName);
    }

    /**
     * Returns a message declaring that a player drew tickets, and saying which tickets have been drawn
     *
     * @param count number of tickets
     * @return a message declaring that a player drew tickets, and saying which tickets have been drawn
     */
    public String drewTickets(int count) {
        return String.format(StringsFr.DREW_TICKETS, playerName, count, StringsFr.plural(count));
    }

    /**
     * Returns a message saying that a player drew a card from the deck
     *
     * @return a message saying that a player drew a card from the deck
     */
    public String drewBlindCard() {
        return String.format(StringsFr.DREW_BLIND_CARD, playerName);
    }

    /**
     * Returns a message saying that a player drew a visible card, and saying which card it is
     *
     * @param card the crad that the player drew
     * @return a message saying that a player drew a visible card, and saying which card it is
     */
    public String drewVisibleCard(Card card) {
        return String.format(StringsFr.DREW_VISIBLE_CARD, playerName, cardName(card, 1));
    }

    /**
     * Returns a message saying that a players has claimed a route, then saying which route it is and with which cards it was claimed
     *
     * @param route the route claimed by the player
     * @param cards the cards used to claim the route
     * @return a message saying that a players has claimed a route, then saying which route it is and with which cards it was claimed
     */
    public String claimedRoute(Route route, SortedBag<Card> cards) {
        return String.format(StringsFr.CLAIMED_ROUTE, playerName, routeName(route), cardsName(cards));
    }

    /**
     * Returns a message saying that a player attempted to claim a tunnel, then saying with which cards this attempt was made
     *
     * @param route        the tunnel that the player attempted to claim
     * @param initialCards the initial cards used to attempt to claim the tunnel
     * @return a message saying that a player attempted to claim a tunnel, then saying with which cards this attempt was made
     */
    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards) {
        return String.format(StringsFr.ATTEMPTS_TUNNEL_CLAIM, playerName, routeName(route), cardsName(initialCards));
    }

    /**
     * Returns a message saying that the player drew the 3 additional cards, then saying if it requires an additional cost, and if it does, this cost is specified
     *
     * @param drawnCards     the 3 cards that the player drew
     * @param additionalCost the additional cost needed to claim the tunnel
     * @return a message saying that the player drew the 3 additional cards, then saying if it requires an additional cost, and if it does, this cost is specified
     */
    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost) {
        String text = String.format(StringsFr.ADDITIONAL_CARDS_ARE, cardsName(drawnCards));

        text += (additionalCost > 0)
                ? String.format(StringsFr.SOME_ADDITIONAL_COST, additionalCost, StringsFr.plural(additionalCost))
                : StringsFr.NO_ADDITIONAL_COST;

        return text;
    }

    /**
     * Returns a message saying that a player couldn't (or didn't want to) claim a tunnel, then specifying which tunnel it is
     *
     * @param route the route that the player didn't claim
     * @return a message saying that a player couldn't (or didn't want to) claim a tunnel, then specifying which tunnel it is
     */
    public String didNotClaimRoute(Route route) {
        return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, playerName, routeName(route));
    }

    /**
     * Returns a message saying that a player has only a specified number of cars left (less or equal than 2), then saying that the last turn begins
     *
     * @param carCount the number of cars left with the player
     * @return a message saying that a player has only a specified number of cars left (less or equal than 2), then saying that the last turn begins
     */
    public String lastTurnBegins(int carCount) {
        return String.format(StringsFr.LAST_TURN_BEGINS, playerName, carCount, StringsFr.plural(carCount));
    }

    /**
     * Returns a message saying that a player gets a bonus for the longest trail, then specifying which trail it is
     *
     * @param longestTrail the longest trail
     * @return a message saying that a player gets a bonus for the longest trail, then specifying which trail it is
     */
    public String getsLongestTrailBonus(Trail longestTrail) {
        return String.format(StringsFr.GETS_BONUS, playerName, longestTrail.station1() + StringsFr.EN_DASH_SEPARATOR + longestTrail.station2());
    }

    /**
     * Returns a message saying that a player has won the game, then specifying his points and then the number of points of the loser
     *
     * @param points      the number of points that the winner has
     * @param loserPoints the number of points that the loser has
     * @return a message saying that a player has won the game, then specifying his points and then the number of points of the loser
     */
    public String won(int points, int loserPoints) {
        return String.format(StringsFr.WINS, playerName, points, StringsFr.plural(points), loserPoints, StringsFr.plural(loserPoints));
    }


    private static String routeName(Route route) {
        return route.station1().name() + StringsFr.EN_DASH_SEPARATOR + route.station2().name();
    }

    private static String cardsName(SortedBag<Card> cards) { //TODO: See if this can be optimised (too many if-else + for-loops)

        StringBuilder cardsName = new StringBuilder();
        List<String> cardList = new ArrayList<>();

        for (Card w : cards.toSet()) {
            int n = cards.countOf(w);
            cardList.add(n + " " + cardName(w, n));
        }

        if (cardList.size() == 1) {
            return cardList.get(0);
        } else if (cardList.size() == 2) {
            return cardList.get(0) + StringsFr.AND_SEPARATOR + cardList.get(1);
        } else {
            for (int i = 0; i < cardList.size() - 2; ++i) {
                cardsName.append(cardList.get(i)).append(", ");
            }
            cardsName.append(cardList.get(cardList.size() - 2)).append(StringsFr.AND_SEPARATOR).append(cardList.get(cardList.size() - 1));
            return cardsName.toString();
        }
    }


}
