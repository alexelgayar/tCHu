package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.*;

/**
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 * Class which represents a game of tCHu. It offers only one single public, static method
 * Public, Final and Non-Instantiable
 */
public final class Game {

    private static Map<PlayerId, Info> infos = new HashMap<>();
    private static GameState gameState;

    /**
     * Method which plays a game of tCHu for the given players, who names appear in the table playerNames.
     * The tickets available for this game are those of tickets, and
     * the random generator tng is used to create the initial state of the game and to shuffle the cards from the discard pile to make a new draw when necessary
     *
     * @param players     the players who will play the game of tCHu
     * @param playerNames the name of the players
     * @param tickets     the tickets available for the game of tCHu
     * @param rng         the random number generator rng is used to create the initial state of the game and to shuffle the cards from the discards pile to make a new draw when necessary
     * @throws IllegalArgumentException if one of the two associative tables has a size other than 2.
     */
    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng) {
        Preconditions.checkArgument(players.size() == 2 && playerNames.size() == 2);

        players.forEach(((playerId, player) -> infos.put(playerId, new Info(playerNames.get(playerId)))));

        //1.1:Initial Players
        players.forEach((id, player) -> player.initPlayers(id, playerNames));
        gameState = GameState.initial(tickets, rng); //method picks first player randomly
        sendInformation(players, infos.get(gameState.currentPlayerId()).willPlayFirst());

        //1.2:Set Initial Ticket Choice, Pick Initial Tickets
        for (PlayerId playerId : PlayerId.ALL) {

            players.get(playerId).setInitialTicketChoice(gameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
            gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        }

        updateStates(players);

        players.forEach(((playerId, player) -> gameState.withInitiallyChosenTickets(playerId, player.chooseInitialTickets())));
        infos.forEach(((playerId, info) -> sendInformation(players, info.keptTickets(gameState.playerState(playerId).ticketCount()))));

        //2.0: First Turn begins (Game starts)
        //Until the end of the game => Each player must play a role, for each current player
        boolean runGame = true;

        //1. While + break or while + update runGame when game is over
        while (runGame) {

            updateStates(players);
            Player currentPlayer = players.get(gameState.currentPlayerId());
            updateStates(players);
            Player.TurnKind turnKind = currentPlayer.nextTurn();
            sendInformation(players, infos.get(gameState.currentPlayerId()).canPlay());

            runGame = !gameState.lastTurnBegins(); //Update boolean runGame, returns false if lastTurnBegins

            if (!runGame){
                sendInformation(players, infos.get(gameState.lastPlayer()).lastTurnBegins(gameState.playerState(gameState.lastPlayer()).carCount()));
            }

            switch (turnKind) {

                case DRAW_TICKETS: {

                    SortedBag<Ticket> drawnTickets = gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
                    SortedBag<Ticket> chosenTickets = currentPlayer.chooseTickets(drawnTickets);
                    sendInformation(players, infos.get(gameState.currentPlayerId()).drewTickets(Constants.IN_GAME_TICKETS_COUNT));
                    gameState = gameState.withChosenAdditionalTickets(drawnTickets, chosenTickets);
                    sendInformation(players, infos.get(gameState.currentPlayerId()).keptTickets(chosenTickets.size()));

                    break;
                }
                case DRAW_CARDS:

                    for (int i = 0; i < 2; ++i) {
                        if (i == 1) {
                            updateStates(players);
                        }

                        int cardSlot = currentPlayer.drawSlot();


                        if (cardSlot == Constants.DECK_SLOT) {
                            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                            gameState = gameState.withBlindlyDrawnCard();
                            sendInformation(players, infos.get(gameState.currentPlayerId()).drewBlindCard());
                        } else {
                            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                            sendInformation(players, infos.get(gameState.currentPlayerId()).drewVisibleCard(gameState.cardState().faceUpCard(cardSlot)));
                            gameState = gameState.withDrawnFaceUpCard(cardSlot);
                        }

                    }
                    break;

                case CLAIM_ROUTE:

                    Route claimedRoute = currentPlayer.claimedRoute();
                    SortedBag<Card> initialClaimCards = currentPlayer.initialClaimCards();

                    if (claimedRoute.level() == Route.Level.OVERGROUND) {
                        if (gameState.currentPlayerState().canClaimRoute(claimedRoute)) {
                            gameState = gameState.withClaimedRoute(claimedRoute, initialClaimCards);
                            sendInformation(players, infos.get(gameState.currentPlayerId()).claimedRoute(claimedRoute, initialClaimCards));

                        }

                    } else if (claimedRoute.level() == Route.Level.UNDERGROUND) {

                        sendInformation(players, infos.get((gameState.currentPlayerId())).attemptsTunnelClaim(claimedRoute, initialClaimCards));

                        if (gameState.currentPlayerState().canClaimRoute(claimedRoute)) {

                            SortedBag<Card> drawnCards = SortedBag.of();

                            for (int i = 0; i < 3; ++i) {
                                gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                                drawnCards = drawnCards.union(SortedBag.of(gameState.topCard()));
                                gameState = gameState.withoutTopCard();
                            }

                            int count = claimedRoute.additionalClaimCardsCount(initialClaimCards, drawnCards);

                            sendInformation(players, infos.get((gameState.currentPlayerId())).drewAdditionalCards(drawnCards, count));

                            if (count == 0) {
                                gameState = gameState.withClaimedRoute(claimedRoute, initialClaimCards);
                                sendInformation(players, infos.get(gameState.currentPlayerId()).claimedRoute(claimedRoute, initialClaimCards));

                            } else {
                                List<SortedBag<Card>> possibleAdditionalCards = gameState.currentPlayerState().possibleAdditionalCards(count, initialClaimCards, drawnCards);
                                SortedBag<Card> additionalCards = currentPlayer.chooseAdditionalCards(possibleAdditionalCards);

                                if (additionalCards.size() == 0) {
                                    sendInformation(players, infos.get((gameState.currentPlayerId())).didNotClaimRoute(claimedRoute));

                                } else {
                                    gameState = gameState.withClaimedRoute(claimedRoute, initialClaimCards.union(additionalCards));
                                    sendInformation(players, infos.get((gameState.currentPlayerId())).claimedRoute(claimedRoute, initialClaimCards.union(additionalCards)));

                                }
                            }
                            gameState = gameState.withMoreDiscardedCards(drawnCards);
                        }

                    }
                    break;
            }
            gameState = gameState.forNextTurn();
        }

        //Compute construction points + ticket points
        Map<PlayerId, Integer> playerPoints = new HashMap<>();

        players.forEach(((playerId, player) -> playerPoints.put(playerId, gameState.playerState(playerId).finalPoints())));

        //Compute points for the longest trail, announce to both players
        Map<PlayerId, Trail> playerLongestTrail = new TreeMap<>();
        players.forEach(((playerId, player) -> playerLongestTrail.put(playerId, Trail.longest(gameState.playerState(playerId).routes()))));

        if (playerLongestTrail.get(PlayerId.PLAYER_1).length() > playerLongestTrail.get(PlayerId.PLAYER_2).length()){
            playerPoints.put(PlayerId.PLAYER_1, playerPoints.get(PlayerId.PLAYER_1) + 10);
            sendInformation(players, infos.get(PlayerId.PLAYER_1).getsLongestTrailBonus(playerLongestTrail.get(PlayerId.PLAYER_1)));
        }
        else if (playerLongestTrail.get(PlayerId.PLAYER_2).length() > playerLongestTrail.get(PlayerId.PLAYER_1).length()){
            playerPoints.put(PlayerId.PLAYER_2, playerPoints.get(PlayerId.PLAYER_2) + 10);
            sendInformation(players, infos.get(PlayerId.PLAYER_2).getsLongestTrailBonus(playerLongestTrail.get(PlayerId.PLAYER_2)));
        }
        else {
            playerPoints.forEach(((playerId, score) -> playerPoints.put(playerId, playerPoints.get(playerId) + 10)));
            infos.forEach(((playerId, info) -> sendInformation(players, info.getsLongestTrailBonus(playerLongestTrail.get(playerId)))));
        }

        //Announce the winner(s)
        updateStates(players);

        if (playerPoints.get(PlayerId.PLAYER_1) > playerPoints.get(PlayerId.PLAYER_2))
            sendInformation(players, infos.get(PlayerId.PLAYER_1).won(playerPoints.get(PlayerId.PLAYER_1), playerPoints.get(PlayerId.PLAYER_2))); //PLAYER 1 WINS
        else if (playerPoints.get(PlayerId.PLAYER_2) > playerPoints.get(PlayerId.PLAYER_1))
            sendInformation(players, infos.get(PlayerId.PLAYER_2).won(playerPoints.get(PlayerId.PLAYER_2), playerPoints.get(PlayerId.PLAYER_1))); //PLAYER 2 WINS
        else {
            List<String> playerStrings = new ArrayList<>(playerNames.values());
            sendInformation(players, Info.draw(playerStrings, playerPoints.get(PlayerId.PLAYER_1))); //DRAW
        }
    }

    //Method which sends information to all the players, by calling the method receiveInfo for each
    private static void sendInformation(Map<PlayerId, Player> players, String info) {
        players.forEach((playerId, player) -> player.receiveInfo(info));
    }

    //Method which informs all players of a change of state, calling the method updateState of each of them
    private static void updateStates(Map<PlayerId, Player> players) {
        players.forEach((playerId, player) -> player.updateState(gameState, gameState.playerState(playerId)));
    }

}
