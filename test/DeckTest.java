import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import freecell.model.Card;
import freecell.model.CardImpl;
import freecell.model.Deck;
import freecell.model.DeckImpl;
import freecell.model.Suit;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for the DeckClass.
 */
public class DeckTest {

  /**
   * Method gets a valid deck of cards.
   */
  @Test
  public void getDeck() {
    Deck d = new DeckImpl();
    List<Card> testDeck = d.getDeck();
    List<String> validSuitString = new ArrayList<>();
    validSuitString.add("HEART");
    validSuitString.add("DIAMOND");
    validSuitString.add("CLUB");
    validSuitString.add("SPADE");

    assertTrue(testDeck.size() == 52);

    Set<Card> set = new HashSet<>();
    for (Card c : testDeck) {
      assertTrue(set.add(c));
    }

    for (Card i : testDeck) {
      assertTrue(i.getValue() > 0);
      assertTrue(i.getValue() < 14);
      assertTrue(validSuitString.contains(i.getSuit().toString()));
    }
  }

  /**
   * For Get method, throws exception if index not found.
   */
  @Test(expected = IllegalArgumentException.class)
  public void get_IndexNotFound() {
    Deck d = new DeckImpl();
    d.get(53);
  }

  /**
   * For Get method succuss case.
   */
  @Test
  public void get_Success() {
    Deck d = new DeckImpl();
    assertEquals("A♦", d.get(0).toString());
    assertEquals("2♦", d.get(1).toString());
  }

  /**
   * Generate a valid deck of card for testing.
   *
   * @return a list of cards
   */
  private List<Card> helperGenerateDeckofValidCard() {
    List<Card> deckTest = new ArrayList<>();
    Suit[] suits = {Suit.DIAMOND, Suit.CLUB, Suit.HEART, Suit.SPADE};
    for (int i = 1; i <= 13; i++) {
      for (int j = 0; j < 4; j++) {
        deckTest.add(new CardImpl(i, suits[j]));
      }
    }
    return deckTest;
  }

  /**
   * Check the is Valid Deck method.
   * False if not 52 cards, or contain duplicates.
   */
  @Test
  public void isValidDeck() {
    List<Card> deckTest = helperGenerateDeckofValidCard();
    Deck newDeck = new DeckImpl(deckTest);
    assertTrue(newDeck.isValidDeck());

    //duplicate
    deckTest.remove(0);
    deckTest.add(new CardImpl(13, Suit.DIAMOND));
    newDeck = new DeckImpl(deckTest);
    assertFalse(newDeck.isValidDeck());

    //less than 52 cards
    deckTest.remove(10);
    deckTest.remove(20);
    newDeck = new DeckImpl(deckTest);
    assertFalse(newDeck.isValidDeck());
  }

  /**
   * Deck shuffle returns false if deck not valid.
   */
  @Test
  public void shuffle_false() {
    //card number incorrect
    List<Card> deckTest = helperGenerateDeckofValidCard();
    deckTest.remove(0);
    Deck newDeck = new DeckImpl(deckTest);
    assertFalse(newDeck.shuffle());

    //duplicates
    List<Card> deckTestDup = helperGenerateDeckofValidCard();
    deckTestDup.remove(0);
    deckTestDup.remove(1);
    deckTestDup.add(new CardImpl(1, Suit.HEART));
    deckTestDup.add(new CardImpl(1, Suit.HEART));

    Deck newDeckDup = new DeckImpl(deckTestDup);
    assertFalse(newDeckDup.isValidDeck());
    assertFalse(newDeckDup.shuffle());
  }

  /**
   * Deck is shuffled if suit and number are not the
   * same as the original deck.
   */
  @Test
  public void shuffle_success() {
    List<Card> deckOriginal = helperGenerateDeckofValidCard();
    List<Card> deckTest = helperGenerateDeckofValidCard();

    Deck newDeck = new DeckImpl(deckTest);
    newDeck.shuffle();

    //check if shuffled deck and original deck is exactly the same
    Boolean sameColor = true;
    Boolean sameVal = true;
    for (int j = 0; j < 53; j++) {
      sameColor = sameColor
              && newDeck.get(j).sameSuit(deckOriginal.get(j));
      sameVal = sameVal
              && (newDeck.get(j).getValue() == deckOriginal.get(j).getValue());

    }
    assertFalse(sameColor);
    assertFalse(sameVal);
  }

  /**
   * Check a deck initialized by null.
   */
  @Test
  public void emptyDeck() {
    Deck newDeck = new DeckImpl(null);
    assertFalse(newDeck.isValidDeck());
  }

}