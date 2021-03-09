package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;

import java.util.ArrayList;
import java.util.List;

public final class Info {

    private final String playerName;

    /**
     * Constructor for Info takes a player's name
     * @param playerName
     */
    public Info(String playerName) {

        this.playerName = playerName;

    }

    /**
     * Returns card name in french, adds an s if it's more than 1 card
     * @param card
     * @param count
     * @return
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
                throw new IllegalArgumentException();

        }
    }

    /**
     * Returns message saying that the game is a draw
     * @param playerNames
     * @param points
     * @return
     */
    public static String draw(List<String> playerNames, int points) {

        return String.format(StringsFr.DRAW, playerNames.get(0) + " et " + playerNames.get(1), points);
    }

    /**
     * Returns the name of the player that will play first
     * @return
     */
    public String willPlayFirst() {

        return String.format(StringsFr.WILL_PLAY_FIRST, playerName);
    }

    public String keptTickets(int count){

        return String.format(StringsFr.KEPT_N_TICKETS, playerName, count, StringsFr.plural(count));
    }

    /**
     * Returns message declaring that a player can play
     * @return
     */
    public String canPlay() {

        return String.format(StringsFr.CAN_PLAY, playerName);
    }

    /**
     * Returns message declaring that a player drew tickets, and saying which tickets have been drawn
     * @param count
     * @return
     */
    public String drewTickets(int count) {

        return String.format(StringsFr.DREW_TICKETS, playerName, count, StringsFr.plural(count));
    }

    /**
     * Returns message saying that a player drew a card from the deck
     * @return
     */
    public String drewBlindCard() {

        return String.format(StringsFr.DREW_BLIND_CARD, playerName);
    }

    /**
     * Returns a message saying that a player drew a visible card, and saying which card it is
     * @param card
     * @return
     */
    public String drewVisibleCard(Card card) {

        return String.format(StringsFr.DREW_VISIBLE_CARD, playerName, card.name());
    }

    /**
     * Returns a message saying that a players has claimed a route, then saying which route it is and with which cards it was claimed
     * @param route
     * @param cards
     * @return
     */
    public String claimedRoute(Route route, SortedBag<Card> cards) {

        return String.format(StringsFr.CLAIMED_ROUTE, playerName, routeName(route), cardsName(cards));
    }

    /**
     * Returns a message saying that a player attempted to claim a tunnel, then saying with which cards this attempt was made
     * @param route
     * @param initialCards
     * @return
     */
    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards) {

        return String.format(StringsFr.ATTEMPTS_TUNNEL_CLAIM, playerName, routeName(route), cardsName(initialCards));
    }

    /**
     * Returns a message saying that the player drew the 3 additional cards, then saying if it requires an additional cost,
     * and if it does, this cost is specified
     * @param drawnCards
     * @param additionalCost
     * @return
     */
    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost) {

        String text = String.format(StringsFr.ADDITIONAL_CARDS_ARE, cardsName(drawnCards)) + "\n";

        if (additionalCost == 0) {
            text += String.format(StringsFr.NO_ADDITIONAL_COST);
        } else {
            text += String.format(StringsFr.SOME_ADDITIONAL_COST, additionalCost, StringsFr.plural(additionalCost));
        }


        return text;
    }

    /**
     * Returns a message saying that a player couldn't (or didn't want to) claim a tunnel, then specifying which tunnel it is
     * @param route
     * @return
     */
    public String didNotClaimRoute(Route route){

        return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, playerName, routeName(route));
    }

    /**
     * Returns a messgae saying that a player has only a specified number of cars left (less or equal than 2),
     * then saying that the last turn begins
     *
     * @param carCount
     * @return
     */
    public String lastTurnBegins(int carCount) {

        return String.format(StringsFr.LAST_TURN_BEGINS, playerName, carCount, StringsFr.plural(carCount));
    }

    /**
     * Returns a message saying that a player gets a bonus for the longest trail, then specifying which trail it is
     * @param longestTrail
     * @return
     */
    public String getsLongestTrailBonus(Trail longestTrail) {

        return String.format(StringsFr.GETS_BONUS, playerName, longestTrail.station1().name() + StringsFr.EN_DASH_SEPARATOR + longestTrail.station2().name());

    }

    /**
     * Returns a message saying that a player has won the game, then specifying his points and then the number of points of the loser
     * @param points
     * @param loserPoints
     * @return
     */
    public String won(int points, int loserPoints) {

        return String.format(StringsFr.WINS, playerName, points, StringsFr.plural(points), loserPoints, StringsFr.plural(loserPoints));
    }


    private static String routeName(Route route) {

        return route.station1().name() + StringsFr.EN_DASH_SEPARATOR + route.station2().name();

    }

    private static String cardsName(SortedBag<Card> cards) {

        String name = null;
        List<String> cardList = new ArrayList<>();

        for (Card w : cards.toSet()) {
            int n = cards.countOf(w);
            cardList.add(n + " " + cardName(w, n));
        }

        for (int i = 0; i < cardList.size() - 2; ++i) {
            name += cardList.get(i) + ", ";
        }

        name += cardList.get(cardList.size() - 2) + StringsFr.AND_SEPARATOR + cardList.get(cardList.size() - 1);

        return name;
    }


}
