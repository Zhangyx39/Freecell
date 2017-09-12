package freecell.model;

/**
 * The interface for a Card.
 */
public interface Card extends Cloneable {

  /**
   * Get the value of this card.
   *
   * @return an integer between 1 and 13
   */
  int getValue();

  /**
   * Get the suit of this card.
   *
   * @return one of the four suits.
   */
  Suit getSuit();

  /**
   * To check whether this card's value is one greater than that of the input
   * card.
   *
   * @param other another card
   * @return true if the value of this card is one greater then the other's
   */
  boolean oneGreater(Card other);

  /**
   * To check whether the suit of this card and another card is the same.
   *
   * @param other another card
   * @return true if they are the same, false otherwise.
   */
  boolean sameSuit(Card other);

  /**
   * To check whether the color of this card and and another card are different.
   * Hearts and Diamonds are in red.
   * Clubs and Spades are in black.
   *
   * @param other another card
   * @return true if their colors are the same, false otherwise.
   */
  boolean differentColor(Card other);

  /**
   * Turn this card to a string like: "A♦" or "3♣".
   *
   * @return a formatted string
   */
  String toString();

  /**
   * Clone this card.
   *
   * @return a new card with same value and same suit as this card
   */
  Card clone();
}
