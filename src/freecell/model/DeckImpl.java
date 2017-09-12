package freecell.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represent a deck of 52 unique cards.
 */
public class DeckImpl implements Deck {
  private List<Card> deck;

  /**
   * Default constructor for DeckImpl.
   */
  public DeckImpl() {
    this.deck = new ArrayList<>();
    for (int i = 1; i <= 13; i++) {
      deck.add(new CardImpl(i, Suit.DIAMOND));
    }
    for (int i = 1; i <= 13; i++) {
      deck.add(new CardImpl(i, Suit.CLUB));
    }
    for (int i = 1; i <= 13; i++) {
      deck.add(new CardImpl(i, Suit.HEART));
    }
    for (int i = 1; i <= 13; i++) {
      deck.add(new CardImpl(i, Suit.SPADE));
    }
  }

  /**
   * Construct a DeckImpl instance with the given list of cards.
   * @param deck a list of cards
   */
  public DeckImpl(List<Card> deck) {
    this.deck = new ArrayList<>();
    if (deck == null) {
      return;
    }
    for (Card c: deck) {
      this.deck.add(c);
    }
  }

  @Override
  public List<Card> getDeck() {
    return deck;
  }

  @Override
  public Card get(int index) throws IllegalArgumentException {
    if (index < 0 || index > 51) {
      throw new IllegalArgumentException("Wrong deck index: " + index);
    }
    return deck.get(index);
  }

  @Override
  public boolean isValidDeck() {
    if (deck.size() != 52) {
      return false;
    }

    Set<Card> memo = new HashSet<>();
    for (Card c: deck) {
      if (memo.contains(c)) {
        return false;
      } else if (c.getValue() < 1 || c.getValue() > 13) {
        return false;
      } else if (c.getSuit() == null) {
        return false;
      } else {
        memo.add(c);
      }
    }
    return true;
  }

  @Override
  public boolean shuffle() {
    //if deck only has 50 cards, or has duplicates do not shuffle
    if (!this.isValidDeck()) {
      return false;
    }

    List<Card> newDeck = new ArrayList<>();
    for (int i = 52; i > 0; i--) {
      int random = (int) (Math.random() * i);
      newDeck.add(deck.get(random));
      deck.remove(random);
    }
    this.deck = newDeck;
    return isValidDeck();
  }
}
