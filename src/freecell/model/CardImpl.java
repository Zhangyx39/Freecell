package freecell.model;

import java.util.Objects;

/**
 * This class represents a card with its value and suit.
 */
public class CardImpl implements Card {
  private int value;
  private Suit suit;

  /**
   * Constructor for CardImpl.
   * @param value the card value
   * @param suit the card suit
   * @throws IllegalArgumentException when value isn't between 1 and 13 or
   *         suit is null.
   */
  public CardImpl(int value, Suit suit) throws IllegalArgumentException {
    if (value < 1 || value > 13) {
      throw new IllegalArgumentException("Input value: " + value + "\nValue " +
              "should not be less than 1 or greater then 13.");
    }
    if (suit == null) {
      throw new IllegalArgumentException("Suit cannot be null.");
    }
    this.value = value;
    this.suit = suit;
  }

  @Override
  public int getValue() {
    return value;
  }

  @Override
  public Suit getSuit() {
    return suit;
  }

  @Override
  public String toString() {
    return valueToString() + suitToString();
  }

  @Override
  public boolean differentColor(Card other) {
    if (this.suit == Suit.DIAMOND || this.suit == Suit.HEART) {
      return other.getSuit() == Suit.CLUB || other.getSuit() == Suit.SPADE;
    } else {
      return other.getSuit() == Suit.DIAMOND || other.getSuit() == Suit.HEART;
    }
  }

  @Override
  public boolean oneGreater(Card other) {
    return this.value - other.getValue() == 1;
  }

  @Override
  public boolean sameSuit(Card other) {
    return this.suit == other.getSuit();
  }

  @Override
  public Card clone() {
    return new CardImpl(this.value, this.suit);
  }

  /**
   * Helper method to turn the suit to a string.
   *
   * @return a one character symbol of that suit.
   */
  private String suitToString() {
    switch (this.suit) {
      case DIAMOND:
        return "♦";
      case CLUB:
        return "♣";
      case HEART:
        return "♥";
      case SPADE:
        return "♠";
      default:
        return "";
    }
  }

  /**
   * Helper method to turn the value to a string.
   *
   * @return "A", "J", "Q", "K" or other value between 2 and 10 in string
   */
  private String valueToString() {
    switch (this.value) {
      case 1:
        return "A";
      case 11:
        return "J";
      case 12:
        return "Q";
      case 13:
        return "K";
      default:
        return this.value + "";
    }
  }

  /**
   * Two cards are equal if they have the same value and are of the same suit.
   * @param o Another card
   * @return true if equal else false
   */
  @Override
  public boolean equals(Object o) {

    if (o == this) {
      return true;
    }
    if (!(o instanceof Card)) {
      return false;
    }

    Card card = (Card) o;

    return card.sameSuit(this) &&
        this.getValue() == card.getValue();
  }

  /**
   * Get the hashcode of this card.
   * @return hash value of the card
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.value,this.suit);
  }

}
