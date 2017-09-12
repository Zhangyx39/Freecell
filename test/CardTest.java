import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import freecell.model.Card;
import freecell.model.CardImpl;
import freecell.model.Suit;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for the Card class.
 */
public class CardTest {

  //Test for Invalid Number Exception
  @Test(expected = IllegalArgumentException.class)
  public void InvalidCard0() {
    Card c = new CardImpl(0, Suit.CLUB);
  }

  //Test for Invalid Number Exception
  @Test(expected = IllegalArgumentException.class)
  public void InvalidCard14() {
    Card c = new CardImpl(14, Suit.CLUB);
  }


  //Test for value Getter
  @Test
  public void getValue() {
    for (int i = 1; i < 14; i++) {
      for (int j = 0; j < 4; j++) {
        Card c = new CardImpl(i, Suit.values()[j]);
        assertEquals(i, c.getValue());
      }
    }
  }

  //Test for suit Getter
  @Test
  public void getSuit() throws Exception {
    for (int i = 1; i < 14; i++) {
      for (int j = 0; j < 4; j++) {
        Card c = new CardImpl(i, Suit.values()[j]);
        assertEquals(Suit.values()[j], c.getSuit());
      }
    }
  }

  /**
   * Go through all cards value 1-13 with four different colors,
   * the bigger card is always 1 larger than the small one.
   */
  @Test
  public void oneGreater() {
    Card big = new CardImpl(1, Suit.HEART);
    Card small;
    for (int i = 2; i < 14; i++) {
      small = big;
      for (int j = 0; j < 4; j++) {
        big = new CardImpl(i, Suit.values()[j]);
        assertTrue(big.oneGreater(small));
        assertFalse(small.oneGreater(big));
      }
    }
  }

  /**
   * Test if two cards have the same suit.
   */
  @Test
  public void sameSuit() {
    Card oldSuit;
    Card newSuit;

    //check for same suit, return true
    for (int j = 0; j < 4; j++) {
      oldSuit = new CardImpl(1, Suit.values()[j]);
      for (int i = 1; i < 14; i++) {
        newSuit = new CardImpl(i, Suit.values()[j]);
        assertTrue(newSuit.sameSuit(oldSuit));
      }
    }

    //check for not the same suit, return false
    oldSuit = new CardImpl(1, Suit.values()[3]);
    for (int j = 2; j >= 0; j--) {
      for (int i = 1; i < 14; i++) {
        newSuit = new CardImpl(i, Suit.values()[j]);
        assertFalse(newSuit.sameSuit(oldSuit));
      }
    }
  }

  /**
   * Test all four colors with all 13 values each,
   * that card Diamond and Heart has the same red color,
   * card Club and Space has the same black color.
   */

  @Test
  public void differentColor() {
    List<Suit> redColor = new ArrayList<>();
    redColor.add(Suit.DIAMOND);
    redColor.add(Suit.HEART);

    List<Suit> blackColor = new ArrayList<>();
    blackColor.add(Suit.SPADE);
    blackColor.add(Suit.CLUB);

    for (int j = 0; j < 2; j++) {
      for (int i = 0; i < 2; i++) {
        for (int m = 1; m < 14; m++) {
          Card redCard = new CardImpl(m, redColor.get(i));
          Card blackCard = new CardImpl(m, blackColor.get(j));
          assertTrue(redCard.differentColor(blackCard));
          //test same color as the other red suit
          assertFalse(redCard.differentColor(new CardImpl(m, redColor.get(Math.abs(1 - i)))));
          //test same color as the other black suit
          assertFalse(blackCard.differentColor(new CardImpl(m, blackColor.get(Math.abs(1 - i)))));
        }
      }
    }
  }

  @Test
  public void testToString() throws Exception {
    String suit;
    String val;
    String result;
    for (int i = 0; i < 4; i++) {

      switch (Suit.values()[i]) {
        case DIAMOND:
          suit = "♦";
          break;
        case CLUB:
          suit = "♣";
          break;
        case HEART:
          suit = "♥";
          break;
        case SPADE:
          suit = "♠";
          break;
        default:
          suit = "";
          break;
      }

      for (int j = 1; j < 14; j++) {
        switch (j) {
          case 1:
            val = "A";
            break;
          case 11:
            val = "J";
            break;
          case 12:
            val = "Q";
            break;
          case 13:
            val = "K";
            break;
          default:
            val = "" + j;
            break;
        }
        result = val + suit;
        Card testCard = new CardImpl(j, Suit.values()[i]);
        assertEquals(result, testCard.toString());
      }
    }
  }


  @Test
  public void TestClone() {
    Card original;
    Card newCard;
    for (int j = 0; j < 4; j++) {
      for (int i = 1; i < 14; i++) {
        original = new CardImpl(i, Suit.values()[j]);
        newCard = original.clone();
        boolean sameSuit = original.sameSuit(newCard);
        boolean sameValue = original.getValue() == newCard.getValue();
        assertTrue(sameSuit && sameValue);
      }
    }
  }

}