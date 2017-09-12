package freecell.model;

import java.util.List;

/**
 * The interface for a deck of cards.
 */
public interface Deck {

  /**
   * Get the whole deck of cards.
   *
   * @return a list of cards.
   */
  List<Card> getDeck();


  /**
   * Get the ith cards from the deck.
   *
   * @param index a integer between 0 and 51
   * @return a card in that index
   * @throws IllegalArgumentException when the index is invalid
   */
  Card get(int index) throws IllegalArgumentException;

  /**
   * To check whether the deck is valid. An invalid deck is defined as a deck
   * that has one or more of these flaws: <ul> <li>It does not have 52
   * cards</li> <li>It has duplicate cards</li> <li>It has at least one invalid
   * card (invalid suit or invalid number) </li> </ul>
   *
   * @return true if it is valid, false otherwise.
   */
  boolean isValidDeck();

  /**
   * Shuffle the deck randomly.
   *
   * @return true if the deck is valid after being shuffled, false otherwise.
   */
  boolean shuffle();


}
