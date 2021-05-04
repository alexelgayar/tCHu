package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.PlayerState;
import ch.epfl.tchu.game.PublicGameState;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Instanciable class, represents the observable state of a game in tCHu
 */
public class ObservableGameState {
    private PublicGameState publicGameState;
    private PlayerState playerState;
    private PlayerId playerId;

    private ObjectProperty<> test1;
    private ObjectProperty<> test2;

    public ObservableGameState(PlayerId playerId){
        //All the state properties are set to null, 0 or false
        this.playerId = playerId;
    }

    public void setState(PublicGameState newGameState, PlayerState newPlayerState){
        this.publicGameState = newGameState;
        this.playerState = newPlayerState;

        test1 = publicGameState.cardState();
    }

    //1. PublicGameState Properties
    /*
    Percentage of tickets remaining
    Percentage of cards remaining
    Five properties for each slot, the face-up card it contains (example)
    As many properties as there are routes, containing for each of them the id of owning player (or null if not owned)
     */

    public Property ReadOnlyTicketPercentage(){
        return Property; //Use an associative table for everything, except the list of tickets
    }

    //2. PublicPlayerState Properties
    /*
    One property per player containing number of tickets in hand
    One property per player containing number of cards in hand
    One property per player containing number of wagons they have
    One property per player containing number of construction points they have obtained
     */

    //3. PrivatePlayerState Properties
    /*
    Property containing list of player tickets
    Nine properties, for each type of wagon/locomotive card, number of cards of this type that player has in hand
    As many properties as there are routes in the tCHu network, each containing bool value (true IFF the player can seize the route): - player is current, road is free, player has cards
     */


    //Additional methods
    /*
    Correspond to methods of PublicGameState or PlayerState
    - Call them on thecurrent state: Eg. to canDrawTickets and canDrawCards as well as possibleClaimCards
     */

    //Example: Face-up cards


}
