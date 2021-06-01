package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.*;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public, Final and Non-Instantiable. Represents a game of tCHu. It offers only one single public, static method
 */
public final class Game {

    private static final Map<PlayerId, Info> infos = new HashMap<>();
    private static GameState gameState;

    /**
     * Private constructor class for the Game class, we will leave it empty (as we want this class to be non-instantiable)
     */
    private Game(){ }

    /**
     * Method which plays a game of tCHu for the given players, who names appear in the table playerNames.
     * The tickets available for this game are those of tickets, and
     * the random generator tng is used to create the initial state of the game and to shuffle the cards from the discard pile to make a new draw when necessary
     *
     * @param players     the players who will play the game of tCHu
     * @param tickets     the tickets available for the game of tCHu
     * @param rng         the random number generator rng is used to create the initial state of the game and to shuffle the cards from the discards pile to make a new draw when necessary
     * @throws IllegalArgumentException if one of the two associative tables has a size other than 2.
     */
    public static void play(Map<PlayerId, Player> players, SortedBag<Ticket> tickets, Random rng) {


       Map<PlayerId, String> playerNames = new HashMap<>();
       players.forEach((playerId, Player) -> Player.setPlayerName());
       players.forEach((playerId, Player) -> playerNames.put(playerId, Player.choosePlayerName()));
       List<String> randomNames = new ArrayList<>(List.of("The Polar Express", "Arnie", "Alex", "Toy Story",
               "Mircea", "Sherlock", "Schinz", "Salim", "DaBaby", "Cécile", "Sharif", "Eugenio", "Takanori", "Durazo"));
       if(playerNames.get(PlayerId.PLAYER_1).equals("")) playerNames.put(PlayerId.PLAYER_1, randomNames.get(rng.nextInt(randomNames.size())));
       randomNames.remove(playerNames.get(PlayerId.PLAYER_1));
       if(playerNames.get(PlayerId.PLAYER_2).equals("")) playerNames.put(PlayerId.PLAYER_2, randomNames.get(rng.nextInt(randomNames.size())));


        Preconditions.checkArgument(players.size() == PlayerId.COUNT && playerNames.size() == PlayerId.COUNT);

        players.forEach(((playerId, player) -> infos.put(playerId, new Info(playerNames.get(playerId)))));

        //Initial Players
        players.forEach((id, player) -> player.initPlayers(id, playerNames));
        gameState = GameState.initial(tickets, rng); //method picks first player randomly
        sendInformation(players, infos.get(gameState.currentPlayerId()).willPlayFirst());

        //Set Initial Ticket Choice, Pick Initial Tickets
        for (PlayerId playerId : PlayerId.ALL) {
            players.get(playerId).setInitialTicketChoice(gameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
            gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        }

        updateStates(players);
        players.forEach(((playerId, player) -> gameState = gameState.withInitiallyChosenTickets(playerId, player.chooseInitialTickets())));
        infos.forEach(((playerId, info) -> sendInformation(players, info.keptTickets(gameState.playerState(playerId).ticketCount()))));

        //Game starts
        boolean runGame = true;
        boolean lastTurnStarted = false;
        int lastTurnsRemaining = PlayerId.COUNT;

        while (runGame) {
            if (lastTurnStarted && lastTurnsRemaining == PlayerId.COUNT - 1){
                sendInformation(players, infos.get(gameState.lastPlayer()).lastTurnBegins(gameState.playerState(gameState.lastPlayer()).carCount()));
            }

            Player currentPlayer = players.get(gameState.currentPlayerId());
            Player.TurnKind turnKind = playTurn(currentPlayer, players);

            switch (turnKind) {
                case DRAW_TICKETS:
                    drawTickets(currentPlayer, players);
                    break;

                case DRAW_CARDS:
                    drawCards(currentPlayer, players, rng);
                    break;

                case CLAIM_ROUTE:
                    claimRoute(currentPlayer, players, rng);
                    break;
            }

            //Check if game should end
            runGame = shouldRunGame(lastTurnsRemaining, lastTurnStarted, runGame);
            if (gameState.lastTurnBegins() || lastTurnStarted){
                lastTurnStarted = true;
                --lastTurnsRemaining;
            }

            gameState = gameState.forNextTurn();
        }

        //Compute points, determine winner
        determineWinner(players, playerNames);
    }

    //Method which sends information to all the players, by calling the method receiveInfo for each
    private static void sendInformation(Map<PlayerId, Player> players, String info) {
        players.forEach((playerId, player) -> player.receiveInfo(info));
    }

    //Method which informs all players of a change of state, calling the method updateState of each of them
    private static void updateStates(Map<PlayerId, Player> players) {
        players.forEach((playerId, player) -> player.updateState(gameState, gameState.playerState(playerId)));
    }

    private static Player.TurnKind playTurn(Player currentPlayer, Map<PlayerId, Player> players){
        updateStates(players);
        sendInformation(players, infos.get(gameState.currentPlayerId()).canPlay());
        Player.TurnKind turnKind = currentPlayer.nextTurn();

        return turnKind;
    }

    private static void drawTickets(Player currentPlayer, Map<PlayerId, Player> players){
        SortedBag<Ticket> drawnTickets = gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
        SortedBag<Ticket> chosenTickets = currentPlayer.chooseTickets(drawnTickets);

        sendInformation(players, infos.get(gameState.currentPlayerId()).drewTickets(Constants.IN_GAME_TICKETS_COUNT));

        gameState = gameState.withChosenAdditionalTickets(drawnTickets, chosenTickets);

        sendInformation(players, infos.get(gameState.currentPlayerId()).keptTickets(chosenTickets.size()));
    }

    private static void drawCards(Player currentPlayer, Map<PlayerId, Player> players, Random rng){
        for (int i = 0; i < PlayerId.COUNT; ++i) {
            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
            if (i == 1) updateStates(players);

            int cardSlot = currentPlayer.drawSlot();

            if (cardSlot == Constants.DECK_SLOT) {
                gameState = gameState.withBlindlyDrawnCard();
                sendInformation(players, infos.get(gameState.currentPlayerId()).drewBlindCard());
            } else {
                sendInformation(players, infos.get(gameState.currentPlayerId()).drewVisibleCard(gameState.cardState().faceUpCard(cardSlot)));
                gameState = gameState.withDrawnFaceUpCard(cardSlot);
            }
        }
    }

    private static void claimRoute(Player currentPlayer, Map<PlayerId, Player> players, Random rng){
        Route claimedRoute = currentPlayer.claimedRoute();
        SortedBag<Card> initialClaimCards = currentPlayer.initialClaimCards();

        if (claimedRoute.level() == Route.Level.OVERGROUND) {
            if (gameState.currentPlayerState().canClaimRoute(claimedRoute)) {
                gameState = gameState.withClaimedRoute(claimedRoute, initialClaimCards);
                sendInformation(players, infos.get(gameState.currentPlayerId()).claimedRoute(claimedRoute, initialClaimCards));
            }
        }

        else if (claimedRoute.level() == Route.Level.UNDERGROUND) {
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


                if (count <= 0) {
                    gameState = gameState.withClaimedRoute(claimedRoute, initialClaimCards);
                    sendInformation(players, infos.get(gameState.currentPlayerId()).claimedRoute(claimedRoute, initialClaimCards));
                } else {
                    if(gameState.currentPlayerState().possibleAdditionalCards(count, initialClaimCards).isEmpty()){
                        sendInformation(players, infos.get((gameState.currentPlayerId())).didNotClaimRoute(claimedRoute));
                    }
                    else {
                        List<SortedBag<Card>> possibleAdditionalCards = gameState.currentPlayerState().possibleAdditionalCards(count, initialClaimCards);

                        SortedBag<Card> additionalCards = currentPlayer.chooseAdditionalCards(possibleAdditionalCards);


                        if (additionalCards.size() == 0) {
                            sendInformation(players, infos.get((gameState.currentPlayerId())).didNotClaimRoute(claimedRoute));
                        } else {
                            gameState = gameState.withClaimedRoute(claimedRoute, initialClaimCards.union(additionalCards));
                            sendInformation(players, infos.get((gameState.currentPlayerId())).claimedRoute(claimedRoute, initialClaimCards.union(additionalCards)));
                        }
                    }
                }
                gameState = gameState.withMoreDiscardedCards(drawnCards);
            }
        }
    }

    private static boolean shouldRunGame(int lastTurnsRemaining, boolean lastTurnStarted, boolean runGame){
        if ((gameState.lastTurnBegins() && lastTurnsRemaining <= 0) || (lastTurnStarted && lastTurnsRemaining <= 0)) {
            runGame = false;
        }
        return runGame;
    }

    private static void determineWinner(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames){
        //Compute construction points + ticket points
        Map<PlayerId, Integer> playerPoints = new HashMap<>();

        players.forEach(((playerId, player) -> playerPoints.put(playerId, gameState.playerState(playerId).finalPoints())));

        //Compute points for the longest trail, announce to both players
        Map<PlayerId, Trail> playerLongestTrail = new TreeMap<>();
        players.forEach(((playerId, player) -> playerLongestTrail.put(playerId, Trail.longest(gameState.playerState(playerId).routes()))));

        if (playerLongestTrail.get(PlayerId.PLAYER_1).length() > playerLongestTrail.get(PlayerId.PLAYER_2).length()) {
            playerPoints.put(PlayerId.PLAYER_1, playerPoints.get(PlayerId.PLAYER_1) + Constants.LONGEST_TRAIL_BONUS_POINTS);
            sendInformation(players, infos.get(PlayerId.PLAYER_1).getsLongestTrailBonus(playerLongestTrail.get(PlayerId.PLAYER_1)));
        } else if (playerLongestTrail.get(PlayerId.PLAYER_2).length() > playerLongestTrail.get(PlayerId.PLAYER_1).length()) {
            playerPoints.put(PlayerId.PLAYER_2, playerPoints.get(PlayerId.PLAYER_2) + Constants.LONGEST_TRAIL_BONUS_POINTS);
            sendInformation(players, infos.get(PlayerId.PLAYER_2).getsLongestTrailBonus(playerLongestTrail.get(PlayerId.PLAYER_2)));
        } else {
            playerPoints.forEach(((playerId, score) -> playerPoints.put(playerId, playerPoints.get(playerId) + Constants.LONGEST_TRAIL_BONUS_POINTS)));
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
}
